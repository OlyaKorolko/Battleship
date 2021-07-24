package com.battleship.ui;

import com.battleship.model.field.*;
import com.battleship.model.player.*;
import com.battleship.model.ships.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Game {
    private final Scanner scanner;
    private final Player player1;
    private final Player player2;
    private boolean curPlayerIs1;

    public Game(Field f1, Field f2, ShipName[] shipNames) {
        scanner = new Scanner(System.in);
        player1 = new Player(1, f1, shipNames);
        player2 = new Player(2, f2, shipNames);
        curPlayerIs1 = true;
    }

    public void startGame() {
        fillField();
        switchPlayers();
        fillField();
        while (notAllShipsSunk()) {
            switchPlayers();
            printFields();
            takeAShot();
        }
    }

    private void fillField() {
        System.out.printf("Player %d, place your ships on the game field\n\n", getPlayer(true).getNumber());
        getPlayer(true).getField().printField(true);
        for (Ship ship : getPlayer(true).getShips()) {
            printMessage(ship.getShipName());
            boolean b = false;
            while (!b) {
                String input = scanner.nextLine();
                if (checkShipInput(input)) {
                    List<Coordinate> parsedCoordinates = parseIntoCoordinates(input);
                    Message m = ship.tryPutShipOnField(parsedCoordinates, getOtherShips(ship));
                    if (m == Message.SUCCESS) {
                        putShipOnField(ship);
                        getPlayer(true).getField().printField(true);
                        b = true;
                    } else {
                        printError(m, ship.getShipName());
                    }
                } else {
                    printError(Message.INVALID_INPUT, null);
                }
            }
        }
    }

    private void switchPlayers() {
        System.out.println("Press Enter and pass the move to another player");
        System.out.println("...");
        scanner.nextLine();
        curPlayerIs1 = !curPlayerIs1;
    }

    private boolean notAllShipsSunk() {
        return player1.getSunkShips() != player1.getShips().size() && player2.getSunkShips() != player2.getShips().size();
    }

    private void printFields() {
        getPlayer(false).getField().printField(false);
        System.out.println("---------------------");
        getPlayer(true).getField().printField(true);
    }

    private Player getPlayer(boolean curPlayer) {
        return (curPlayer) ? ((curPlayerIs1) ? player1 : player2) : ((curPlayerIs1) ? player2 : player1);
    }

    private void takeAShot() {
        System.out.printf("Player %d, it's your turn:\n", getPlayer(true).getNumber());
        boolean b = false;
        while (!b) {
            String input = scanner.nextLine();
            if (!checkShotInput(input)) {
                printError(Message.INVALID_INPUT, null);
            } else if (!processShot(input)) {
                printError(Message.REPEATED_SHOT, null);
            } else {
                b = true;
            }
        }
    }

    private void printMessage(ShipName ship) {
        switch (ship) {
            case AIRCRAFT_CARRIER -> System.out.println("Enter the coordinates of the Aircraft Carrier (5 cells):");
            case BATTLESHIP -> System.out.println("Enter the coordinates of the Battleship (4 cells):");
            case SUBMARINE -> System.out.println("Enter the coordinates of the Submarine (3 cells):");
            case CRUISER -> System.out.println("Enter the coordinates of the Cruiser (3 cells):");
            case DESTROYER -> System.out.println("Enter the coordinates of the Destroyer (2 cells):");
        }
    }

    private void printError(Message message, ShipName shipName) {
        switch (message) {
            case IMPOSSIBLE_LOCATION -> System.out.println("Error! Wrong ship location! Try again:");
            case WRONG_LENGTH -> {
                if (shipName != null)
                    System.out.printf("Error! Wrong length of the %s! Try again:\n", shipName.getShipName());
            }
            case WRONG_DISTANCE -> System.out.println("Error! You placed it too close to another one. Try again:");
            case INVALID_INPUT -> System.out.println("You entered the wrong coordinates! Try again:\n");
            case REPEATED_SHOT -> System.out.println("Error! You already shot there!");
        }
    }

    private boolean checkShipInput(String input) {
        Pattern pattern = Pattern.compile("^[a-jA-J]([1-9]|10)\\s[a-jA-J]([1-9]|10)$");
        return pattern.matcher(input).matches();
    }

    private List<Coordinate> parseIntoCoordinates(String input) {
        String[] coordinates = input.split("\\s+");
        List<Coordinate> parsedCoordinates = new ArrayList<>();
        parsedCoordinates.add(getCoordinate(coordinates[0]));
        parsedCoordinates.add(getCoordinate(coordinates[1]));
        if (parsedCoordinates.get(0).compareTo(parsedCoordinates.get(1)) < 0) {
            Collections.swap(parsedCoordinates, 0, 1);
        }
        return parsedCoordinates;
    }

    private List<Ship> getOtherShips(Ship curShip) {
        return getPlayer(true).getShips()
                .stream()
                .filter(ship -> ship != curShip && ship.getShipStatus() != ShipStatus.NOT_PLACED)
                .collect(Collectors.toList());
    }

    private void putShipOnField(Ship ship) {
        if (ship.getCoord1().getLetter() == ship.getCoord2().getLetter()) {
            for (int i = ship.getCoord1().getNumber(); i <= ship.getCoord2().getNumber(); i++) {
                getPlayer(true).getField().setCell(new Coordinate(ship.getCoord1().getLetter(), i), Cell.TAKEN);
            }
        } else if (ship.getCoord1().getNumber() == ship.getCoord2().getNumber()) {
            for (char i = ship.getCoord1().getLetter(); i <= ship.getCoord2().getLetter(); i++) {
                getPlayer(true).getField().setCell(new Coordinate(i, ship.getCoord1().getNumber()), Cell.TAKEN);
            }
        }
    }

    private boolean checkShotInput(String input) {
        Pattern pattern = Pattern.compile("^[a-jA-J]([1-9]|10)$");
        return pattern.matcher(input).matches();
    }

    private boolean processShot(String input) {
        Coordinate coord = getCoordinate(input);
        if (getPlayer(false).getField().getCell(coord) == Cell.FOG) {
            processMiss(coord);
            return true;
        } else if (getPlayer(false).getField().getCell(coord) == Cell.TAKEN) {
            processHit(coord);
            return true;
        }
        return false;
    }

    private void processMiss(Coordinate coord) {
        getPlayer(false).getField().setCell(coord, Cell.MISS);
        System.out.println("You missed!");
    }

    private void processHit(Coordinate coord) {
        for (Ship ship : getPlayer(false).getShips()) {
            if (checkIfShipIsHit(ship, coord)) {
                ship.updateShip();
                getPlayer(false).getField().setCell(coord, Cell.HIT);
                checkShipResult(ship);
            }
        }
    }

    private void checkShipResult(Ship shipResult) {
        if (shipResult.getShipStatus() == ShipStatus.DEAD) {
            getPlayer(false).setSunkShips(getPlayer(false).getSunkShips() + 1);
            if (getPlayer(false).getSunkShips() != getPlayer(false).getShips().size()) {
                System.out.println("You sank a ship!");
            } else {
                System.out.println("You sank the last ship. You won. Congratulations!");
            }
        } else {
            System.out.println("You hit a ship!");
        }
    }

    private boolean checkIfShipIsHit(Ship ship, Coordinate coord) {
        return (ship.getCoord1().getLetter() == ship.getCoord2().getLetter() && horizontalShipHit(ship, coord)) ||
                (ship.getCoord1().getNumber() == ship.getCoord2().getNumber() && verticalShipHit(ship, coord));
    }

    private boolean horizontalShipHit(Ship ship, Coordinate coord) {
        return ship.getCoord1().getLetter() == coord.getLetter() &&
                ship.getCoord1().getNumber() <= coord.getNumber() &&
                ship.getCoord2().getNumber() >= coord.getNumber();
    }

    private boolean verticalShipHit(Ship ship, Coordinate coord) {
        return ship.getCoord1().getNumber() == coord.getNumber() &&
                ship.getCoord1().getLetter() <= coord.getLetter() &&
                ship.getCoord2().getLetter() >= coord.getLetter();
    }

    private Coordinate getCoordinate(String input) {
        return new Coordinate(input.toUpperCase().charAt(0), Integer.parseInt(input.substring(1)));
    }
}
