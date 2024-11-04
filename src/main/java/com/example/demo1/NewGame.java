package com.example.demo1;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
//Нова гра життя
public class NewGame extends GameOfLife{ //Спадкує від класичної гри Життя
    ArrayList<Cell> cells = new ArrayList<>(); // масив для зберігання живих клітин
    NewGame(int rows, int columns){
        HEIGHT=rows;
        WIDTH=columns;
        gameBoard = new int[rows][columns];
        generateBoard();
    }

    //Генерація стану поля
    void generateBoard() {
        Random random = new Random();
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                gameBoard[row][col] = random.nextInt(2);
            }
        }
    }

    @Override
    void nextStep(){
        ArrayList<Cell> Next_cells = new ArrayList<>();
        ArrayList<Cell> NewBorn = checkMeetCells();//Перевіряємо новонароджені клітини

        for (Cell cell:cells) {
            Cell newCell = new Cell(cell.row,cell.column,cell.logic_operation,cell.status,cell.important_neighbours, cell.current_iteration);
            int row=cell.row;
            int col=cell.column;
            int direction = check_important_neighbours(row,col,newCell);//повертає напрямок
            // Масив зсувів для перевірки 8 сусідніх клітин
            int[] dx = {0, 1, 1, 1, 0, -1, -1, -1};
            int[] dy = {-1, -1, 0, 1, 1, 1, 0, -1};
            int newRow = (row + dy[direction] + HEIGHT) % HEIGHT;
            int newCol = (col + dx[direction] + WIDTH) % WIDTH;
            //змінюємо позицію клітини
            newCell.row=newRow;
            newCell.column=newCol;
            newCell.change_important_neighbours();// за потреби змінюємо сусідів, за якими проводимо обчислення
            Next_cells.add(newCell);

        }
        Next_cells.addAll(NewBorn);

        cells=Next_cells;
        printAllCells();
    }

    // Функція для оновлення стану клітини, повертає напрямок та змінює поточний стан
    int check_important_neighbours(int row, int col, Cell cell) {

        // Масив зсувів для перевірки 8 сусідніх клітин
        int[][] important_neighbours = cell.current_neighbours(gameBoard.length,gameBoard.length);

        // Початковий стан залежить від типу логічної операції
        boolean status;
        switch (cell.logic_operation) {
            case 0: // AND
                status = true;
                break;
            case 1: // OR
                status = false;
                break;
            case 2: // XOR
                status = false;
                break;
            default:
                throw new IllegalArgumentException("Unknown logic operation: " + cell.logic_operation);
        }
        int direction=0;
        int n =0;

        for (int[]neigh:important_neighbours) {
            // Якщо сусід "живий", додаємо значення в direction
            boolean n_status=(gameBoard[neigh[0]][neigh[1]]==1);
            if (n_status) {
                direction += Math.pow(2,n);
            }
            n++;
            // Виконуємо логічні операції
            switch (cell.logic_operation) {
                case 0: // AND
                    status = status && n_status;
                    break;
                case 1: // OR
                    status = status || n_status;
                    break;
                case 2: // XOR
                    status = status ^ n_status; // Поточний статус буде true, якщо кількість живих сусідів непарна
                    break;
                default:
                    throw new IllegalArgumentException("Unknown logic operation: " + cell.logic_operation);
            }

        }
        if (status==true) cell.status=1;
        else cell.status=0;
        System.out.println("dir "+direction);
        return direction;
    }

    ArrayList<Cell> checkMeetCells() {
        ArrayList<Cell> NewBorn = new ArrayList<>();
        ArrayList<Cell> uniqueCells = new ArrayList<>(); // Початково порожній список для унікальних клітин
        Set<String> birthPositions = new HashSet<>(); // Зберігає позиції, де вже народжені клітини
        Iterator<Cell> iterator = cells.iterator();

        while (iterator.hasNext()) {
            Cell cell = iterator.next(); // Отримуємо наступну клітину
            boolean duplicateFound = false;
            ArrayList<Cell> cellsToRemove = new ArrayList<>();

            for (Cell uniqueCell : uniqueCells) {
                if (uniqueCell.row == cell.row && uniqueCell.column == cell.column) {
                    duplicateFound = true;

                    if (uniqueCell.status == cell.status) {
                        // Якщо статуси однакові, обидві клітини гинуть
                        cellsToRemove.add(uniqueCell);
                        iterator.remove(); // Видаляємо поточну клітину
                        break; // Виходимо з циклу, оскільки клітину вже видалено
                    } else {
                        // Якщо статуси різні, створюємо нову клітину лише один раз для кожної позиції
                        String positionKey = cell.row + "," + cell.column;
                        if (!birthPositions.contains(positionKey)) {
                            Cell newCell = new Cell(uniqueCell); // Копія батьківської клітини
                            NewBorn.add(newCell);
                            birthPositions.add(positionKey); // Запам'ятовуємо позицію для запобігання подвійного народження
                        }
                        break; // Виходимо з циклу, оскільки знайшли дублікат
                    }
                }
            }
            uniqueCells.removeAll(cellsToRemove);
            // Додаємо поточну клітину до uniqueCells, якщо вона не має дублікатів
            if (!duplicateFound) {
                uniqueCells.add(cell);
            }
        }

        return NewBorn;
    }

    @Override
    public void setGameBoard(int[][] gameBoard) {
        this.gameBoard = gameBoard;
        generateBoard();
    }

    @Override
    public void addCell (Cell cell){
        cells.add(cell);
        printAllCells();
    }
    @Override
    public Cell findCell(int row, int column) {
        for (Cell cell : cells) {
            if (cell.row == row && cell.column == column) {
                return cell; // Повертаємо клітину, якщо індекси збігаються
            }
        }
        return null; // Повертаємо null, якщо клітину не знайдено
    }


    @Override
    public void setCells(ArrayList<Cell> cells) {
        this.cells=cells;
    }

    public ArrayList<Cell> getCells(){
        return cells;
    }

    public void removeCell(int row, int column) {
        Cell cell = findCell(row, column);
        cells.remove(cell);
    }

    public void printAllCells() {
        for (Cell cell : cells) {
            System.out.println("Cell at (" + cell.row + ", " + cell.column +
                    ") - Logic Operation: " + cell.logic_operation +
                    ", Status: " + cell.status);
        }
    }
}
