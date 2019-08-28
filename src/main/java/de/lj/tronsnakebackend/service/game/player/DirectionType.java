package de.lj.tronsnakebackend.service.game.player;

import de.lj.tronsnakebackend.service.game.assets.MyVector;

public enum DirectionType {
    RIGHT(1,0),
    LEFT(-1, 0),
    DOWN(0, 1),
    UP(0,-1);

    MyVector direction;

    DirectionType(int xDir, int yDir) {
        this.direction = new MyVector(xDir, yDir);
    }

    public MyVector getDirection() {
        return direction;
    }
}
