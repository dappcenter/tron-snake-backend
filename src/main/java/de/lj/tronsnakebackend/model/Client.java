package de.lj.tronsnakebackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class Client {

    @NotNull
    @NotEmpty
    private String sessionId;

    @NotNull
    @NotEmpty
    private MessageHeaders messageHeaders;

    @NotNull
    @NotEmpty
    private String name;

    public Client(String name, MessageHeaders messageHeaders, String SessionId) {
        this.name = name;
        this.messageHeaders = messageHeaders;
        this.sessionId = (String) messageHeaders.get(SimpMessageHeaderAccessor.SESSION_ID_HEADER);
    }
}
