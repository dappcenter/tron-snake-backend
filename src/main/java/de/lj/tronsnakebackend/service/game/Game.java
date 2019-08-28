package de.lj.tronsnakebackend.service.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.lj.tronsnakebackend.service.game.assets.MyVector;
import de.lj.tronsnakebackend.service.game.player.Player;

/**
 * model.Game handles the data processing and implements the rules for this game.
 * A player is deactivated if he runs into the wall or another snake. if both player collide
 * frontally both players are deactivated. game is over if only one or no player is still actively
 * moving.
 */
public class Game implements GameConstants {

    private Field field;
    private List<Player> players = new ArrayList<>();
    private List<Square> changedSquares = new ArrayList<>();

    public Game() {
        this.field = new Field(FIELD_WIDTH, FIELD_HEIGHT);
    }

    // add a player to the game if game isnt full yet
    public void addPlayer(Player player) {
        if (players.size() < MAX_PLAYER_COUNT) {
            player.setColor(PLAYER_COLORS[playerCount()]);
            players.add(player);
            field.getSquareAt(player.getSnake().getPosition()).setColor(player.getColor());
        }
    }

    // is called by the game controller after every user input to handle collision checking and
    // updating the positions of the snakes
    public void nextTurn() {
        moveSnakes();
        handleCollisions();
        blockCells();
    }

    private void moveSnakes() {
        getActivePlayersStream()
            .map(Player::getSnake)
            .forEach(Snake::move);
    }

    // set a cell to blocked, if a snakes moves upon it
    private void blockCells() {
        changedSquares.clear();
        getActivePlayersStream().forEach(player -> {
            MyVector position = player.getSnake().getPosition();
            Square square = field.getSquareAt(position);
            if (square != null) {
                square.setColor(player.getColor());
                changedSquares.add(square);
            }
        });
    }

    private void handleCollisions() {
        getActivePlayersStream().forEach(player -> {
            MyVector position = player.getSnake().getPosition();
            player.setActive(!(wallCollision(position) || blockedCellCollision(position)));
        });
    }

    private boolean wallCollision(MyVector position) {
        return !field.withinBounds(position);
    }

    private boolean blockedCellCollision(MyVector position) {
        Square square = field.getSquareAt(position);
        return square != null && square.isBlocked();
    }

    public int playerCount() {
        return players.size();
    }

    public Stream<Player> getActivePlayersStream() {
        return players.parallelStream().filter(Player::isActive);
    }

    // check winning conditions
    public boolean isOver() {
        return getActivePlayersStream().count() < 2;
    }

    public List<Square> getChangedSquares() {
        return changedSquares;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getWinner() {
        Player winner = null;
        if (isOver() && getActivePlayersStream().count() > 0) {
            winner = getActivePlayersStream().findFirst().orElse(null);
        }
        return winner;
    }

    public Field getField() {
        return field;
    }
}