package com.battleship.model.field;

public class Field {
    private final Cell[][] battlefield;

    public Field(int size) {
        battlefield = new Cell[size][size];
        createField();
    }

    public void createField() {
        for (int i = 0; i < battlefield.length; i++) {
            for (int j = 0; j < battlefield.length; j++) {
                battlefield[i][j] = Cell.FOG;
            }
        }
    }

    public void printField(boolean myField) {
        for (int i = 0; i <= battlefield.length; i++) {
            for (int j = 0; j <= battlefield.length; j++) {
                if (i == 0 && j == 0) System.out.print("  ");
                else if (i == 0) System.out.print(j + " ");
                else if (j == 0) System.out.print((char) (65 + i - 1) + " ");
                else if (!myField && battlefield[i - 1][j - 1].getSign() == Cell.TAKEN.getSign()) {
                    System.out.print(Cell.FOG.getSign() + " ");
                } else System.out.print(battlefield[i - 1][j - 1].getSign() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public Cell getCell(Coordinate p) {
        return battlefield[p.getLetter() - 65][p.getNumber() - 1];
    }

    public void setCell(Coordinate p, Cell condition) {
        battlefield[p.getLetter() - 65][p.getNumber() - 1] = condition;
    }
}
