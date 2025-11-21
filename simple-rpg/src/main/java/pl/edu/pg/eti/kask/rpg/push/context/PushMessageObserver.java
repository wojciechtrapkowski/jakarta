package pl.edu.pg.eti.kask.rpg.push.context;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.rpg.chat.event.MessageEvent;
import pl.edu.pg.eti.kask.rpg.push.dto.Message;

/**
 * Observer that listens to MessageEvent and sends it via JSF Push channels.
 */
@ApplicationScoped
@Log
@NoArgsConstructor(force = true)
public class PushMessageObserver {

    private final PushMessageContext pushMessageContext;

    @Inject
    public PushMessageObserver(PushMessageContext pushMessageContext) {
        this.pushMessageContext = pushMessageContext;
    }

    /**
     * Observes message events and sends them via JSF push.
     */
    public void onMessageEvent(@Observes MessageEvent event) {
        log.info("Push observer received message event: " + event.getContent());
        
        Message message = Message.builder()
                .messageId(event.getMessageId().toString())
                .senderId(event.getSenderId().toString())
                .senderLogin(event.getSenderLogin())
                .content(event.getContent())
                .timestamp(event.getTimestamp())
                .broadcast(event.isBroadcast())
                .recipientId(event.getRecipientId() != null ? event.getRecipientId().toString() : null)
                .build();

        if (event.isBroadcast()) {
            // Send to all connected users via broadcast channel
            pushMessageContext.notifyAll(message);
        } else {
            // Send to specific user via user channel using their login
            // Send to both sender and recipient
            pushMessageContext.notifyUser(message, event.getSenderLogin());
            if (event.getRecipientLogin() != null) {
                pushMessageContext.notifyUser(message, event.getRecipientLogin());
            }
        }
    }
}
