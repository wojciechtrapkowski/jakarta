package pl.edu.pg.eti.kask.rpg.chat.websocket;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * WebSocket endpoint for chat.
 */
@ServerEndpoint("/chat")
public class ChatWebSocketEndpoint {

    private static final Logger log = Logger.getLogger(ChatWebSocketEndpoint.class.getName());

    // Map of userId to WebSocket sessions (static to share across all instances)
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
     * Broadcast a message to all connected users.
     */
    public static void broadcastMessage(String message) {
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

    /**
     * Send a message to a specific user.
     */
    public static void sendToUser(UUID userId, String message) {
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
