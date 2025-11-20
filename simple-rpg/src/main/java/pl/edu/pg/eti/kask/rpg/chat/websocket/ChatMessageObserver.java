package pl.edu.pg.eti.kask.rpg.chat.websocket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.rpg.chat.event.MessageEvent;

import java.io.IOException;
import java.io.StringWriter;
import java.util.UUID;

/**
 * CDI bean that observes message events and broadcasts them via WebSocket.
 */
@ApplicationScoped
@Log
public class ChatMessageObserver {

    /**
     * Observes message events and broadcasts them to WebSocket clients.
     */
    public void onMessageEvent(@Observes MessageEvent event) {
        log.info("Message event received: " + event.getContent());
        
        JsonObject jsonMessage = Json.createObjectBuilder()
                .add("messageId", event.getMessageId().toString())
                .add("senderId", event.getSenderId().toString())
                .add("senderLogin", event.getSenderLogin())
                .add("content", event.getContent())
                .add("timestamp", event.getTimestamp())
                .add("broadcast", event.isBroadcast())
                .add("recipientId", event.getRecipientId() != null ? event.getRecipientId().toString() : "")
                .build();

        StringWriter writer = new StringWriter();
        Json.createWriter(writer).write(jsonMessage);
        String messageText = writer.toString();

        if (event.isBroadcast()) {
            // Send to all connected users
            ChatWebSocketEndpoint.broadcastMessage(messageText);
        } else {
            // Send to specific user
            ChatWebSocketEndpoint.sendToUser(event.getRecipientId(), messageText);
            // Also send to sender for confirmation
            ChatWebSocketEndpoint.sendToUser(event.getSenderId(), messageText);
        }
    }
}
