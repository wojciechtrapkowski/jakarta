package pl.edu.pg.eti.kask.rpg.push.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for push messages sent through JSF push channels.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message implements Serializable {
    
    /**
     * Unique message identifier (UUID string format).
     */
    private String messageId;
    
    /**
     * Sender's user identifier (UUID string format).
     */
    private String senderId;
    
    /**
     * Sender's login name.
     */
    private String senderLogin;
    
    /**
     * Message content.
     */
    private String content;
    
    /**
     * Message timestamp in ISO_LOCAL_DATE_TIME format.
     */
    private String timestamp;
    
    /**
     * Whether this is a broadcast message (true) or private message (false).
     */
    private boolean broadcast;
    
    /**
     * Recipient's user identifier (UUID string format), null for broadcast messages.
     */
    private String recipientId;
}
