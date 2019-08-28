package de.lj.tronsnakebackend.service.game;

import de.lj.tronsnakebackend.service.game.assets.MyRandom;
import de.lj.tronsnakebackend.service.game.assets.MyVector;
import de.lj.tronsnakebackend.service.game.player.DirectionType;

public class Snake implements GameConstants {

    private DirectionType direction;
    private MyVector position;

    public Snake(int x, int y) {
        this.position = new MyVector(x, y);
        this.direction = DirectionType.values()[MyRandom.generateInt(0,3)];
    }

    public void move() {
        this.position = position.plus(direction.getDirection());
    }

    public boolean validDirection(DirectionType inputDirection) {
        switch(direction) {
            case UP:
                return (inputDirection != DirectionType.DOWN);
            case DOWN:
                return (inputDirection != DirectionType.UP);
            case LEFT:
                return (inputDirection != DirectionType.RIGHT);
            case RIGHT:
                return  (inputDirection != DirectionType.LEFT);
            default:
                return false;
        }
    }

    public MyVector getPosition() {
        return position;
    }

    public void setDirection(DirectionType direction) {
        this.direction = direction;
    }

    public DirectionType getDirection() {
        return direction;
    }
}
