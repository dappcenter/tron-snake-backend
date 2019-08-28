package de.lj.tronsnakebackend.websocket.message;

import de.lj.tronsnakebackend.service.game.Square;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SquareUpdateMessage {
    private List<Square> squares;
}
