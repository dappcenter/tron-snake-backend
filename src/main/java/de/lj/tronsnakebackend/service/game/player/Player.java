package de.lj.tronsnakebackend.service.game.player;

import de.lj.tronsnakebackend.service.game.GameConstants;
import de.lj.tronsnakebackend.service.game.Snake;

public abstract class Player implements GameConstants {

    private String name;
    private boolean active;
    private String color;

    private Snake snake;

    public Player(String name) {
        this.name = name;
        this.snake = new Snake(
                (int) Math.floor(Math.random()*FIELD_WIDTH),
                (int) Math.floor(Math.random()*FIELD_HEIGHT)
        );
        this.active = true;
        this.color = "black";
    }

    public String getName() {
        return name;
    }

    public Snake getSnake() {
        return snake;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void updateDirection(DirectionType direction) {
        if(this.getSnake().validDirection(direction)) {
            this.getSnake().setDirection(direction);
        }
    };
}
