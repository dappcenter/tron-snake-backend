package de.lj.tronsnakebackend.service.game;

import de.lj.tronsnakebackend.service.game.assets.MyVector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Field implements GameConstants {

    private List<Square> squares;

    public Field() {
        this.squares = new ArrayList<>();
        initializeCells();
    }

    private void initializeCells() {
        for (int index = 0; index < FIELD_HEIGHT * FIELD_WIDTH; index++) {
            squares.add(new Square(index));
        }
    }

    public Square getSquareAt(MyVector position) {
        int index = position.getX() + position.getY() * FIELD_WIDTH;
        return index > squares.size() ? null : squares.get(index);
    }

    public boolean withinBounds(MyVector position) {
        int x = position.getX();
        int y = position.getY();
        return x >= 0 & x < FIELD_WIDTH & y >= 0 & y < FIELD_HEIGHT;
    }

    public List<Square> getBlockedSquares() {
        return squares.parallelStream().filter(Square::isBlocked).collect(Collectors.toList());
    }
}

