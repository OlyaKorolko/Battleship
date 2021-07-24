package com.battleship;

import com.battleship.model.field.Field;
import com.battleship.model.ships.ShipName;
import com.battleship.ui.Game;

public class Main {
    public static void main(String[] args) {
        final int SIZE = 10;
        try {
            Game game = new Game(new Field(SIZE), new Field(SIZE),
                    new ShipName[]{ShipName.AIRCRAFT_CARRIER, ShipName.BATTLESHIP, ShipName.SUBMARINE, ShipName.CRUISER,
                            ShipName.DESTROYER});
            game.startGame();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}