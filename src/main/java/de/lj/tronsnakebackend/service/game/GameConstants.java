package de.lj.tronsnakebackend.service.game;

import de.lj.tronsnakebackend.service.game.assets.MyVector;
import de.lj.tronsnakebackend.service.game.player.DirectionType;

/**
 * this interface holds the contants for this game. change with care
 */

public interface GameConstants {

    public static final long GAME_UPDATE_TIME = 100;
    public static final short GAME_COUNTDOWN = 5;

    public static final int FIELD_HEIGHT = 45;
    public static final int FIELD_WIDTH = 35;

    public static final String[] SNAKE_COLORS = new String[]{
        "blue", "red", "orange", "green"
    };

    public static final int STARTING_POSITION_PADDING = 3;

    public static final MyVector[] STARTING_POSITIONS = new MyVector[]{
            new MyVector(FIELD_WIDTH/2, STARTING_POSITION_PADDING),
            new MyVector(FIELD_WIDTH/2, FIELD_HEIGHT-STARTING_POSITION_PADDING-1),
            new MyVector(STARTING_POSITION_PADDING, FIELD_HEIGHT/2),
            new MyVector(FIELD_WIDTH-STARTING_POSITION_PADDING-1, FIELD_HEIGHT/2),
    };

    public static final DirectionType[] STARTING_DIRECTIONS = new DirectionType[]{
            DirectionType.DOWN, DirectionType.UP, DirectionType.RIGHT, DirectionType.LEFT
    };
}
