package de.lj.tronsnakebackend.websocket.message;

import de.lj.tronsnakebackend.service.game.player.DirectionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectionMessage {
    private DirectionType direction;
}
