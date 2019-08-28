package de.lj.tronsnakebackend.websocket.message;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class JoinMessage {
    private String name;
}
