package com.battleship.model.field;

public enum Cell {
    FOG('~'),
    TAKEN('O'),
    HIT('X'),
    MISS('M');

    private final char sign;

    Cell(char s) {
        sign = s;
    }

    public char getSign() {
        return sign;
    }
}
