package com.example.demo1;

import java.util.ArrayList;

//Класична гра Життя
public class GameOfLife {
    int HEIGHT;
    int WIDTH;
    double CellSize;
    int [][] gameBoard;
    GameOfLife(int rows, int columns){
        HEIGHT=rows;
        WIDTH=columns;
        gameBoard = new int[rows][columns];
    }

    GameOfLife(){

    }


    void nextStep() {
        int[][] newBoard = new int[HEIGHT][WIDTH];
        //Проходимось по полю

        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                //Для кожної клітини рахуємо її живих сусідів
                int liveNeighbors = countLiveNeighbors(row, col);

                if (gameBoard[row][col] == 1) { // Жива клітина
                    if (liveNeighbors == 2 || liveNeighbors == 3) {
                        newBoard[row][col] = 1; // Залишається жити
                    } else {
                        newBoard[row][col] = 0; // Помирає
                    }
                } else { // Мертва клітина
                    if (liveNeighbors == 3) {
                        newBoard[row][col] = 1; // Оживає
                    } else {
                        newBoard[row][col] = 0; // Залишається мертвою
                    }
                }
            }
        }

        gameBoard = newBoard;
    }

    // Функція для підрахунку сусідів
    int countLiveNeighbors(int row, int col) {
        int count = 0;

        // Масив зсувів для перевірки 8 сусідніх клітин
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int newRow = row + dx[i];
            int newCol = col + dy[i];

            // Перевіряємо, чи нові координати в межах поля
            if (newRow >= 0 && newRow < HEIGHT && newCol >= 0 && newCol < WIDTH) {
                count += gameBoard[newRow][newCol];
            }
        }

        return count;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public void setCellSize(double CELL_SIZE) {
        this.CellSize = CELL_SIZE;
    }

    public void setWIDTH(int WIDTH) {
        this.WIDTH = WIDTH;
    }

    public void setHEIGHT(int HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    public void addCell(Cell cell){

    }

    public void setCells(ArrayList<Cell> cells){
    }

    public ArrayList<Cell> getCells(){
        return null;
    }

    public void removeCell(int row, int column){

    }

    public Cell findCell(int row, int column) {
        return null;
    }

    public void setGameBoard(int[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public double getCELL_SIZE() {
        return CellSize;
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public void printMatrix() {
        int [][]matrix=gameBoard;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
