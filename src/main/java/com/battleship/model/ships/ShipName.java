package com.battleship.model.ships;

public enum ShipName {
    AIRCRAFT_CARRIER(5, "Aircraft Carrier"),
    BATTLESHIP(4, "Battleship"),
    SUBMARINE(3, "Submarine"),
    CRUISER(3, "Cruiser"),
    DESTROYER(2, "Destroyer");

    private final int shipSize;
    private final String shipName;

    ShipName(int size, String name) {
        shipSize = size;
        shipName = name;
    }

    public int getShipSize() {
        return shipSize;
    }

    public String getShipName() {
        return shipName;
    }
}
