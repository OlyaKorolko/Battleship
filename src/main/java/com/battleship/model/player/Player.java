package com.battleship.model.player;

import com.battleship.model.field.Field;
import com.battleship.model.ships.ShipName;
import com.battleship.model.ships.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player {
    private final int number;
    private final Field field;
    private final List<Ship> ships;
    private int sunkShips;

    public Player(int num, Field field, ShipName[] shipNames) {
        this.number = num;
        this.field = field;
        this.ships = new ArrayList<>(shipNames.length);
        for (ShipName s : shipNames) this.ships.add(new Ship(s));
    }

    public int getNumber() {
        return number;
    }

    public Field getField() {
        return field;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public int getSunkShips() {
        return sunkShips;
    }

    public void setSunkShips(int sunkShips) {
        this.sunkShips = sunkShips;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return number == player.number &&
                sunkShips == player.sunkShips &&
                Objects.equals(field, player.field) &&
                Objects.equals(ships, player.ships);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, field, ships, sunkShips);
    }
}
