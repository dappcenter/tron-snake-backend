package de.lj.tronsnakebackend.service.game;

import de.lj.tronsnakebackend.service.game.assets.MyVector;

public class Snake {

    private MyVector previousPosition;
    private MyVector currentPosition;
    private MyVector direction;
    private String color;

    public Snake(MyVector currentPosition, MyVector direction, String color) {
        this.previousPosition = currentPosition;
        this.currentPosition = currentPosition;
        this.direction = direction;
        this.color = color;
    }

    public void move() {
        previousPosition = currentPosition;
        currentPosition = currentPosition.plus(direction);
    }

    public MyVector peekPosition() {
        return currentPosition.plus(direction);
    }

    public void updateDirection(MyVector newDirection) {
        if(!currentPosition.plus(newDirection).equals(previousPosition)) {
            direction = newDirection;
        }
    }

    public MyVector getCurrentPosition() {
        return currentPosition;
    }

    public String getColor() {
        return color;
    }
}
