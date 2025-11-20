package pl.edu.pg.eti.kask.rpg.chat.websocket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.rpg.chat.event.MessageEvent;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket endpoint for chat.
 */
@ServerEndpoint("/chat")
@ApplicationScoped
@Log
public class ChatWebSocketEndpoint {

    // Map of userId to WebSocket sessions
    private static final ConcurrentHashMap<UUID, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        log.info("WebSocket opened: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        log.info("WebSocket closed: " + session.getId());
        // Remove session from all users
        sessions.values().removeIf(s -> s.equals(session));
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.severe("WebSocket error: " + throwable.getMessage());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("Received message: " + message);
        // Handle registration messages
        try {
            jakarta.json.JsonReader reader = Json.createReader(new java.io.StringReader(message));
            JsonObject json = reader.readObject();
            
            if (json.containsKey("type") && "register".equals(json.getString("type"))) {
                String userId = json.getString("userId");
                sessions.put(UUID.fromString(userId), session);
                log.info("Registered user: " + userId);
            }
        } catch (Exception e) {
            log.warning("Error processing message: " + e.getMessage());
        }
    }

    /**
     * CDI observer for message events.
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
            broadcastMessage(messageText);
        } else {
            // Send to specific user
            sendToUser(event.getRecipientId(), messageText);
            // Also send to sender for confirmation
            sendToUser(event.getSenderId(), messageText);
        }
    }

    private void broadcastMessage(String message) {
        sessions.values().forEach(session -> {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    log.warning("Error sending message: " + e.getMessage());
                }
            }
        });
    }

    private void sendToUser(UUID userId, String message) {
        Session session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.warning("Error sending message to user: " + e.getMessage());
            }
        }
    }
}
