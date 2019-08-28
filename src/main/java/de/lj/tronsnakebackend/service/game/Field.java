package de.lj.tronsnakebackend.service.game;

import de.lj.tronsnakebackend.service.game.assets.MyVector;

import java.util.ArrayList;
import java.util.List;

public class Field implements GameConstants {

    private Integer width;
    private Integer height;
    private List<Square> squares;

    public Field(Integer width, Integer height) {
        this.width = width;
        this.height = height;
        this.squares = new ArrayList<>();
        initializeCells();
    }

    private void initializeCells() {
        for (int index = 0; index < height * width; index++) {
            squares.add(new Square(index));
        }
    }

    public Square getSquareAt(MyVector position) {
        return squares.stream().filter(square -> square.getIndex() == position.getX() + position.getY() * width)
                .findFirst().orElse(null);
    }

    public boolean withinBounds(MyVector position) {
        return position.getX() >= 0 & position.getX() < width
                & position.getY() >= 0 & position.getY() < height;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }
}

