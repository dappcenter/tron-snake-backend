package de.lj.tronsnakebackend.service;

import de.lj.tronsnakebackend.model.Client;
import de.lj.tronsnakebackend.service.game.Game;
import de.lj.tronsnakebackend.service.game.GameConstants;
import de.lj.tronsnakebackend.service.game.Square;
import de.lj.tronsnakebackend.service.game.player.DirectionType;
import de.lj.tronsnakebackend.service.game.player.Human;
import de.lj.tronsnakebackend.service.game.player.Player;
import de.lj.tronsnakebackend.model.dto.ColorDto;
import de.lj.tronsnakebackend.model.dto.PlayerDto;
import de.lj.tronsnakebackend.model.dto.SquareDto;
import de.lj.tronsnakebackend.model.dto.CountdownDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService implements GameConstants {

    private BroadcastScheduler broadcastScheduler;
    private SimpMessagingTemplate messagingTemplate;

    private List<Game> pendingGames = new ArrayList<>();
    private List<Client> clients = new ArrayList<>();
    private Map<Client, Player> clientPlayerMap = new IdentityHashMap<>();
    private Map<Client, Game> clientGameMap = new IdentityHashMap<>();

    @Autowired
    public GameService(BroadcastScheduler broadcastScheduler,
                       SimpMessagingTemplate messagingTemplate) {
        this.broadcastScheduler = broadcastScheduler;
        this.messagingTemplate = messagingTemplate;
    }

    @SendToUser
    private synchronized void sendGameOverMessage(List<Client> clients, Player winner) {
        PlayerDto message = new PlayerDto();
        message.setName((winner == null) ? "Nobody" : winner.getName());
        message.setColor((winner == null) ? "white" : winner.getSnake().getColor());

        for (Client client : clients) {
            messagingTemplate.convertAndSendToUser(client.getSessionId(), "/client/game_over", message, client.getMessageHeaders());
        }
    }

    @SendToUser
    private synchronized void sendSquareUpdateMessage(List<Client> clients, List<Square> squares) {
        List<SquareDto> message = new Vector<>();
        squares.parallelStream().forEach(square -> message.add(new SquareDto(square.getIndex(), square.getColor())));

        for (Client client : clients) {
            messagingTemplate.convertAndSendToUser(client.getSessionId(), "/client/update_squares", message, client.getMessageHeaders());
        }
    }

    @SendToUser
    private synchronized void sendCountdownMessage(List<Client> clients, short count) {
        CountdownDto message = new CountdownDto(count);

        for (Client client : clients) {
            messagingTemplate.convertAndSendToUser(client.getSessionId(), "/client/countdown", message, client.getMessageHeaders());
        }
    }

    @SendToUser
    private synchronized void sendColorMessage(Client client, String color) {
        ColorDto colorDto = new ColorDto(color);

        System.out.println("sending color to client: " + color);

        messagingTemplate.convertAndSendToUser(client.getSessionId(), "/client/get_color", colorDto, client.getMessageHeaders());
    }

    private Runnable broadcastGame(Game game) {
        return new Runnable() {
            final List<Client> clients = getClientsForGame(game);
            final Vector<Square> blockedSquares = new Vector<>();

            @Override
            public void run() {
                if (game.isOver()) {
                    Player winner = game.getWinner();
                    sendGameOverMessage(clients, winner);
                    broadcastScheduler.cancelBroadcast(this);
                    removeClients(clients);
                } else {
                    game.nextTurn();
                    sendSquareUpdateMessage(clients, game.getUpdatedSquares());
                }
            }
        };
    }

    private Runnable broadcastCountdown(Game game) {
        return new Runnable() {
            final List<Client> clients = getClientsForGame(game);
            short count = GAME_COUNTDOWN;

            @Override
            public void run() {
                if (count-- > 0) {
                    sendCountdownMessage(clients, count);
                } else {
                    broadcastScheduler.cancelBroadcast(this);
                    scheduleBroadcastGame(game);
                }
            }
        };
    }

    private void scheduleBroadcastCountdown(Game game) {
        broadcastScheduler.scheduleWithFixedDelay(broadcastCountdown(game), 1000);
    }

    private void scheduleBroadcastGame(Game game) {
        broadcastScheduler.scheduleWithFixedDelay(broadcastGame(game), GAME_UPDATE_TIME);
    }

    private void removeClients(List<Client> clients) {
        clients.parallelStream().forEach(client -> {
            clientGameMap.remove(client);
            clientPlayerMap.remove(client);
            this.clients.remove(client);
        });
    }

    private List<Client> getClientsForGame(Game game) {
        return clientGameMap.entrySet().parallelStream()
                .filter((entry) -> entry.getValue().equals(game))
                .map((Map.Entry::getKey))
                .collect(Collectors.toList());
    }

    private Player getPlayerForClient(Client client) {
        return clientPlayerMap.getOrDefault(client, null);
    }

    public synchronized Client getClientForSessionId(String sessionId) {
        return clients.parallelStream()
                .filter(client -> client.getSessionId().equals(sessionId))
                .findAny().orElse(null);
    }

    public void updateDirectionForClient(DirectionType direction, Client client) {
        Player player = getPlayerForClient(client);
        if (player != null && player.isActive()) {
            player.getSnake().updateDirection(direction.getValue());
        }
    }

    public void removePlayerForClient(Client client) {
        clientPlayerMap.get(client).setActive(false);
    }

    public void addPlayerForClient(Client client, Integer playerCount) {
        clients.add(client);

        Player player = new Human(client.getName());
        clientPlayerMap.put(client, player);

        Game game = getPendingGame(playerCount);
        game.addPlayer(player);
        clientGameMap.put(client, game);

        sendColorMessage(client, player.getSnake().getColor());
        sendSquareUpdateMessage(getClientsForGame(game), game.getUpdatedSquares());

        if (game.isFull()) {
            pendingGames.remove(game);
            scheduleBroadcastCountdown(game);
        }
    }

    private Game getPendingGame(Integer playerCount) {
        List<Game> filtered_games = pendingGames.parallelStream()
                .filter(game -> game.getPlayerCount() == playerCount)
                .collect(Collectors.toList());
        Game game = filtered_games.size() > 0 ? filtered_games.get(0) : new Game(playerCount);
        if (!pendingGames.contains(game)) {
            pendingGames.add(game);
        }
        return game;
    }
}
