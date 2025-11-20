package pl.edu.pg.eti.kask.rpg.chat.websocket;

import jakarta.websocket.Endpoint;
import jakarta.websocket.server.ServerApplicationConfig;
import jakarta.websocket.server.ServerEndpointConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * WebSocket application configuration.
 * Programmatically registers WebSocket endpoints.
 */
public class WebSocketConfig implements ServerApplicationConfig {

    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses) {
        return new HashSet<>();
    }

    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
        Set<Class<?>> endpoints = new HashSet<>();
        // Register the chat endpoint
        endpoints.add(ChatWebSocketEndpoint.class);
        return endpoints;
    }
}
