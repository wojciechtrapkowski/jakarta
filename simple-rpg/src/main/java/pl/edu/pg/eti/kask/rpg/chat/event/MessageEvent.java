package pl.edu.pg.eti.kask.rpg.chat.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * CDI event for chat messages.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageEvent {
    private UUID messageId;
    private UUID senderId;
    private String senderLogin;
    private UUID recipientId;
    private String content;
    private boolean broadcast;
    private String timestamp;
}
