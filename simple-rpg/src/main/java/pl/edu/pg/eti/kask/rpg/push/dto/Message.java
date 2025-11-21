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
    
    private String messageId;
    private String senderId;
    private String senderLogin;
    private String content;
    private String timestamp;
    private boolean broadcast;
    private String recipientId;
}
