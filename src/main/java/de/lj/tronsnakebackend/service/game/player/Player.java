package de.lj.tronsnakebackend.service.game.player;

import de.lj.tronsnakebackend.service.game.Snake;
import de.lj.tronsnakebackend.service.game.assets.MyVector;

public abstract class Player {

    private String name;
    private boolean active;

    private Snake snake;

    public Player(String name) {
        this.name = name;
        this.active = true;
    }

    public MyVector getSnakePosition() {
        return this.snake.getCurrentPosition();
    }

    public String getName() {
        return name;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
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

}
