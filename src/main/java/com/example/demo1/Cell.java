package com.example.demo1;
import java.util.ArrayList;
import java.util.Random;
//Клас клітини для Нової гри Життя
public class Cell {
    int row;
    int column;
    int logic_operation; // 0 - and, 1 - or, 2 - xor
    int status; // 0 або 1
    int current_iteration=0;
    int change_important_neighbours;//кількість ітерацій для зміни сусідів, від яких ведуться розрахунки
    boolean [] important_neighbours;//положення сусідів, від яких ведуться розрахунки, відносно поточної клітини
    Cell(int r, int c, int logic, int st, boolean [] neighbours, int current_iteration){
        row=r;
        column=c;
        logic_operation=logic;
        if(logic_operation==2)change_important_neighbours=3;
        else change_important_neighbours=2;
        status=st;
        important_neighbours = neighbours;
        this.current_iteration=current_iteration;
    }

    public Cell(Cell other) {
        this.row = other.row;
        this.column = other.column;
        this.logic_operation = other.logic_operation;
        this.status = other.status;
        this.current_iteration = other.current_iteration;
        this.change_important_neighbours = other.change_important_neighbours;
        this.important_neighbours = other.important_neighbours.clone(); // Копіюємо масив
    }


//Повертає координати поточних важливих сусідів на ігровому полі
    int[][] current_neighbours(int HEIGHT, int WIDTH) {
        int[] dx = {0, 1, 1, 1, 0, -1, -1, -1};
        int[] dy = {-1, -1, 0, 1, 1, 1, 0, -1};

        ArrayList<int[]> importantCoords = new ArrayList<>();

        // Проходимося по всіх 8 сусідах
        for (int i = 0; i < 8; i++) {
            if (important_neighbours[i]) {
                // Обробка виходу за межі масиву
                int newRow = (row + dy[i] + HEIGHT) % HEIGHT;
                int newCol = (column + dx[i] + WIDTH) % WIDTH;

                // Додаємо координати важливих сусідів до списку
                importantCoords.add(new int[]{newRow, newCol});
            }
        }

        // Повертаємо список важливих сусідів як масив
        return importantCoords.toArray(new int[importantCoords.size()][]);
    }
 //Змінює сусідів, від яких ведуться розрахунки
    void change_important_neighbours(){
        current_iteration++;
        Random random = new Random();
        if(current_iteration==change_important_neighbours){
            current_iteration=0;
            for (int i = 0; i < 8; i++) {
                important_neighbours[i] = false;
            }
            int count = 0;
            while (count < 3) {
                int index = random.nextInt(8);
                if (!important_neighbours[index]) {  // Перевіряємо, чи елемент ще не був обраний
                    important_neighbours[index] = true;
                    count++;
                }
            }


        }
    }


}
