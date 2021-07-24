package com.battleship.model.field;

import java.util.Objects;

public class Coordinate implements Comparable<Coordinate> {
    private final char letter;
    private final int number;

    public Coordinate(char letter, int number) {
        this.letter = letter;
        this.number = number;
    }

    public char getLetter() {
        return letter;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate pair = (Coordinate) o;
        return letter == pair.letter &&
                number == pair.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, number);
    }

    @Override
    public int compareTo(Coordinate o) {
        if (letter < o.letter) return 1;
        else if (letter > o.letter) return -1;
        else return Integer.compare(o.number, number);
    }
}
