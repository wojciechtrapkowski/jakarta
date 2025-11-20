package pl.edu.pg.eti.kask.rpg.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request DTO for sending a message.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {
    private String content;
    private String recipientId;  // null or empty for broadcast
}
