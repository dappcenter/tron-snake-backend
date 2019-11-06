package de.lj.tronsnakebackend.controller;

import de.lj.tronsnakebackend.model.Client;
import de.lj.tronsnakebackend.service.GameService;
import de.lj.tronsnakebackend.service.game.GameConstants;
import de.lj.tronsnakebackend.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;


@Controller
public class GameController implements GameConstants {

    private GameService gameService;

    @Autowired
    public GameController(
            GameService gameService
    ) {
        this.gameService = gameService;
    }

    @MessageMapping("/disconnect")
    public void leaveGame(@Header(value = "simpSessionId") String sessionId) {
        Client client = gameService.getClientForSessionId(sessionId);
        gameService.removePlayerForClient(client);
    }

    @MessageMapping("/direction")
    public void updatePlayerDirection(@Payload DirectionDto directionDto,
                             @Header(value = "simpSessionId") String sessionId) {
        Client client = gameService.getClientForSessionId(sessionId);
        gameService.updateDirectionForClient(directionDto.getDirection(), client);
    }

    @MessageMapping("/join")
    public void joinGame(@Payload JoinDto joinDto, MessageHeaders messageHeaders, @Header(value = "simpSessionId") String sessionId) {
        Client client = new Client(joinDto.getPlayerName(), messageHeaders, sessionId);
        gameService.addPlayerForClient(client);
    }
}
