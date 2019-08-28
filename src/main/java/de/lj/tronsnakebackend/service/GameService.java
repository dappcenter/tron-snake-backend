package de.lj.tronsnakebackend.service;

import de.lj.tronsnakebackend.model.Client;
import de.lj.tronsnakebackend.service.game.Game;
import de.lj.tronsnakebackend.service.game.GameConstants;
import de.lj.tronsnakebackend.service.game.player.DirectionType;
import de.lj.tronsnakebackend.service.game.player.Human;
import de.lj.tronsnakebackend.service.game.player.Player;
import de.lj.tronsnakebackend.websocket.message.ColorMessage;
import de.lj.tronsnakebackend.websocket.message.GameOverMessage;
import de.lj.tronsnakebackend.websocket.message.SquareUpdateMessage;
import de.lj.tronsnakebackend.websocket.message.StartGameMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService implements GameConstants {

    private BroadcastScheduler broadcastScheduler;
    private SimpMessagingTemplate messagingTemplate;

    private List<Game> games = new ArrayList<>();
    private Map<Client, Player> clientPlayerMap = new IdentityHashMap<>();

    @Autowired
    public GameService(BroadcastScheduler broadcastScheduler,
                       SimpMessagingTemplate messagingTemplate) {
        this.broadcastScheduler = broadcastScheduler;
        this.messagingTemplate = messagingTemplate;
    }

    @SendToUser
    private Runnable broadcastGame(Game game) {
        final List<Client> clients = getClientsForGame(game);
        return new Runnable() {
            @Override
            public void run() {
                if (game.isOver()) {
                    Player winner = game.getWinner();
                    for (Client client : clients) {
                        GameOverMessage message = new GameOverMessage(winner == null ? "Nobody" : winner.getName());
                        messagingTemplate.convertAndSendToUser(client.getSessionId(), "/client/game_over", message, client.getMessageHeaders());
                    }
                    broadcastScheduler.cancelBroadcast(this);
                    clients.parallelStream().forEach(client -> clientPlayerMap.remove(client));
                    games.remove(game);
                } else {
                    game.nextTurn();
                    for (Client client : clients) {
                        SquareUpdateMessage message = new SquareUpdateMessage(game.getChangedSquares());
                        messagingTemplate.convertAndSendToUser(client.getSessionId(), "/client/update_squares", message, client.getMessageHeaders());
                    }
                }
            }
        };
    }

    @SendToUser
    private Runnable broadcastCountdown(Game game) {
        final List<Client> clients = getClientsForGame(game);

        return new Runnable() {
            short count = GAME_COUNTDOWN;
            @Override
            public void run() {
                if(count-- > 0) {
                    for(Client client : clients) {
                        StartGameMessage message = new StartGameMessage(count);
                        messagingTemplate.convertAndSendToUser(client.getSessionId(), "/client/countdown", message, client.getMessageHeaders());
                    }
                } else {
                    broadcastScheduler.cancelBroadcast(this);
                    scheduleBroadcastGame(game);
                }
            }
        };
    }

    private List<Client> getClientsForGame(Game game) {
        List<Client> clients = new ArrayList<>();

        for (Player player : game.getPlayers()) {
            clientPlayerMap.entrySet().parallelStream()
                    .filter(entrySet -> entrySet.getValue().equals(player))
                    .map(Map.Entry::getKey)
                    .findAny().ifPresent(clients::add);
        }

        return clients;
    }

    private Player getPlayerForClient(Client client) {
        return clientPlayerMap.getOrDefault(client, null);
    }

    public Client getClientForSessionId(String sessionId) {
        return clientPlayerMap.keySet().parallelStream()
                .filter(client -> client.getSessionId().equals(sessionId))
                .findAny().orElse(null);
    }

    public void updatePlayerDirectionForClient(DirectionType direction, Client client) {
        Player player = getPlayerForClient(client);
        if (player != null) {
            player.updateDirection(direction);
        }
    }

    public void removePlayerForClient(Client client) {
        Player player = getPlayerForClient(client);
        player.setActive(false);
    }

    @SendToUser
    public void addPlayerForClient(Client client) {
        Game game = getGameWithCapacity();
        Player player = new Human(client.getName());

        game.addPlayer(player);
        clientPlayerMap.put(client, player);

        ColorMessage message = new ColorMessage(player.getColor());
        messagingTemplate.convertAndSendToUser(client.getSessionId(), "/client/get_color", message, client.getMessageHeaders());

        if (game.playerCount() == MAX_PLAYER_COUNT) {
            scheduleBroadcastCountdown(game);
        }
    }

    private void scheduleBroadcastCountdown(Game game) {
        broadcastScheduler.scheduleAtFixedRate(broadcastCountdown(game), 1000);
    }

    private void scheduleBroadcastGame(Game game) {
        broadcastScheduler.scheduleAtFixedRate(broadcastGame(game), GAME_UPDATE_TIME);
    }

    private Game getGameWithCapacity() {
        Game game = games.parallelStream()
                .filter(g -> g.playerCount() < MAX_PLAYER_COUNT)
                .findAny().orElse(new Game());
        if (game.playerCount() == 0) {
            games.add(game);
        }
        return game;
    }
}