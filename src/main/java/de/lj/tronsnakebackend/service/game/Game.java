package de.lj.tronsnakebackend.service.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.lj.tronsnakebackend.service.game.assets.MyVector;
import de.lj.tronsnakebackend.service.game.player.Player;

public class Game implements GameConstants {

    private Field field = new Field();
    private List<Player> players = new ArrayList<>();
    private List<Square> updatedSquares = new Vector<>();

    public void addPlayer(Player player) {
        int index = players.size();
        if (index < MAX_PLAYER_COUNT) {
            MyVector position = STARTING_POSITIONS[index];
            MyVector direction = STARTING_DIRECTIONS[index].getValue();
            String color = SNAKE_COLORS[index];

            Snake snake = new Snake(position, direction, color);
            player.setSnake(snake);
            players.add(player);

            blockSquare(position);
            colorSquare(position, color);

            snake.move();
            blockSquare(position.plus(direction));
            colorSquare(position.plus(direction), "black");
        }
    }

    public synchronized void nextTurn() {
        final List<MyVector> nextPositions =
                getActivePlayersStream()
                        .map(Player::getSnake)
                        .map(Snake::peekPosition)
                        .collect(Collectors.toList());

        updatedSquares.clear();

        getActivePlayersStream()
                .forEach(player -> {
                    MyVector nextPosition = player.getSnake().peekPosition();
                    player.setActive(!(wallCollision(nextPosition) || blockedCellCollision(nextPosition)));
                });

        getActivePlayersStream()
                .forEach(player -> {
                    Snake snake = player.getSnake();
                    MyVector nextPosition = snake.peekPosition();
                    if (frontalCollision(nextPosition, nextPositions)) {
                        moveSnake(snake);
                        player.setActive(false);
                    }
                });

        getActivePlayersStream()
            .map(Player::getSnake)
                .forEach(this::moveSnake);
    }

    private void moveSnake(Snake snake) {
        MyVector currentPosition = snake.getCurrentPosition();
        MyVector nextPosition = snake.peekPosition();

        colorSquare(currentPosition, snake.getColor());
        snake.move();
        blockSquare(nextPosition);
        colorSquare(nextPosition, "black");
    }

    private void colorSquare(MyVector position, String color) {
        Square square = field.getSquareAt(position);
        square.setColor(color);
        updatedSquares.add(square);
    }

    private void blockSquare(MyVector position) {
        Square square = field.getSquareAt(position);
        square.setBlocked(true);
        updatedSquares.add(square);
    }

    private boolean frontalCollision(MyVector nextPosition, List<MyVector> nextPositions) {
        return nextPositions.parallelStream().filter(position -> position.equals(nextPosition)).count() > 1;
    }

    private boolean wallCollision(MyVector position) {
        return !field.withinBounds(position);
    }

    private boolean blockedCellCollision(MyVector position) {
        return field.getSquareAt(position).isBlocked();
    }

    private Stream<Player> getActivePlayersStream() {
        return players.parallelStream().filter(Player::isActive);
    }

    public boolean isFull() {
        return players.size() == MAX_PLAYER_COUNT;
    }

    public boolean isOver() {
        return getActivePlayersStream().count() < 2;
    }

    public Player getWinner() {
        Player winner = null;
        if (isOver() && getActivePlayersStream().count() > 0) {
            winner = getActivePlayersStream().findFirst().orElse(null);
        }
        return winner;
    }

    public List<Square> getUpdatedSquares() {
        return updatedSquares;
    }
}