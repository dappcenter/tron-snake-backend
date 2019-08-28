package de.lj.tronsnakebackend.service.game;

/**
 * this interface holds the contants for this game. change with care
 */

public interface GameConstants {

    public static final long GAME_UPDATE_TIME = 100;
    public static final short GAME_COUNTDOWN = 5;

    public static final int FIELD_HEIGHT = 45;
    public static final int FIELD_WIDTH = 35;

    public static final int MAX_PLAYER_COUNT = 2;
    public static final int PLAYER_PADDING = 5;

    public static final int AI_FREE_CELL_SCANNING_DEPTH = 3;

    public static final String[] PLAYER_COLORS = new String[]{
        "blue", "red", "yellow", "green"
    };
}
