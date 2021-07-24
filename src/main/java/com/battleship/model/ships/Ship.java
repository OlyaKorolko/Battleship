package com.battleship.model.ships;

import com.battleship.model.player.Message;
import com.battleship.model.field.Coordinate;

import java.util.Collections;
import java.util.List;

public class Ship {
    private final ShipName shipName;
    private final int size;
    private Coordinate coordinate1;
    private Coordinate coordinate2;
    private ShipStatus shipStatus;
    private int HP;

    public Ship(ShipName shipName) {
        this.shipName = shipName;
        this.size = shipName.getShipSize();
        this.shipStatus = ShipStatus.NOT_PLACED;
        this.HP = size;
    }

    public Message tryPutShipOnField(List<Coordinate> parsedCoordinates, List<Ship> otherShips) {
        Coordinate first = parsedCoordinates.get(0);
        Coordinate second = parsedCoordinates.get(1);
        if (first.getLetter() != second.getLetter() && first.getNumber() != second.getNumber()) {
            return Message.IMPOSSIBLE_LOCATION;
        } else if (second.getLetter() - first.getLetter() + 1 != size &&
                second.getNumber() - first.getNumber() + 1 != size) {
            return Message.WRONG_LENGTH;
        } else if (!checkPosition(parsedCoordinates, otherShips)) {
            return Message.WRONG_DISTANCE;
        } else {
            putShipOnField(parsedCoordinates);
            return Message.SUCCESS;
        }
    }

    private void putShipOnField(List<Coordinate> parsedCoordinates) {
        coordinate1 = parsedCoordinates.get(0);
        coordinate2 = parsedCoordinates.get(1);
        shipStatus = ShipStatus.PLACED;
    }

    public void updateShip() {
        if (HP == size) shipStatus = ShipStatus.HIT;
        HP--;
        if (HP == 0) shipStatus = ShipStatus.DEAD;
    }

    private boolean checkPosition(List<Coordinate> parsedCoordinates, List<Ship> otherShips) {
        for (Ship ship : otherShips) {
            if (getMinHorizontalDistance(parsedCoordinates, ship) < 2 &&
                    getMinVerticalDistance(parsedCoordinates, ship) < 2) return false;
        }
        return true;
    }

    private int getMinHorizontalDistance(List<Coordinate> parsedCoordinates, Ship ship) {
        return Collections.min(List.of(Math.abs(parsedCoordinates.get(0).getNumber() - ship.coordinate1.getNumber()),
                Math.abs(parsedCoordinates.get(0).getNumber() - ship.coordinate2.getNumber()),
                Math.abs(parsedCoordinates.get(1).getNumber() - ship.coordinate1.getNumber()),
                Math.abs(parsedCoordinates.get(1).getNumber() - ship.coordinate2.getNumber())));
    }

    private int getMinVerticalDistance(List<Coordinate> parsedCoordinates, Ship ship) {
        return Collections.min(List.of(Math.abs(parsedCoordinates.get(0).getLetter() - ship.coordinate1.getLetter()),
                Math.abs(parsedCoordinates.get(0).getLetter() - ship.coordinate2.getLetter()),
                Math.abs(parsedCoordinates.get(1).getLetter() - ship.coordinate1.getLetter()),
                Math.abs(parsedCoordinates.get(1).getLetter() - ship.coordinate2.getLetter())));
    }

    public ShipName getShipName() {
        return shipName;
    }

    public int getSize() {
        return size;
    }

    public Coordinate getCoord1() {
        return coordinate1;
    }

    public void setCoordinate1(Coordinate coordinate1) {
        this.coordinate1 = coordinate1;
    }

    public Coordinate getCoord2() {
        return coordinate2;
    }

    public void setCoordinate2(Coordinate coordinate2) {
        this.coordinate2 = coordinate2;
    }

    public ShipStatus getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(ShipStatus shipStatus) {
        this.shipStatus = shipStatus;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }
}
