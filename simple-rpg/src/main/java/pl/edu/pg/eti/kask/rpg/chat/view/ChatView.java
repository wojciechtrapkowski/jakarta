package pl.edu.pg.eti.kask.rpg.chat.view;

import jakarta.faces.view.ViewScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.rpg.chat.event.MessageEvent;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.user.service.UserService;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * View bean for chat page.
 * Messages are sent directly via JSF actions which fire CDI events.
 * Push notifications are handled via JSF Push API.
 */
@ViewScoped
@Named
@Log
public class ChatView implements Serializable {

    private final UserService userService;
    private final SecurityContext securityContext;
    private final Event<MessageEvent> messageEvent;

    @Getter
    @Setter
    private String messageContent;

    @Getter
    @Setter
    private String recipientId;

    private String currentUserId;

    @Inject
    public ChatView(UserService userService, 
                    SecurityContext securityContext,
                    Event<MessageEvent> messageEvent) {
        this.userService = userService;
        this.securityContext = securityContext;
        this.messageEvent = messageEvent;
    }

    public String getCurrentUserId() {
        if (currentUserId == null && securityContext.getCallerPrincipal() != null) {
            String login = securityContext.getCallerPrincipal().getName();
            currentUserId = userService.find(login)
                    .map(user -> user.getId().toString())
                    .orElse("");
        }
        return currentUserId;
    }

    public List<User> getAllUsers() {
        return userService.findAll();
    }

    /**
     * Sends a chat message by firing a CDI event.
     * The event is observed by PushMessageObserver which sends push notifications.
     */
    public void sendMessage() {
        if (messageContent == null || messageContent.trim().isEmpty()) {
            return;
        }

        String login = securityContext.getCallerPrincipal().getName();
        User sender = userService.find(login).orElse(null);
        if (sender == null) {
            return;
        }

        MessageEvent event;
        if (recipientId == null || recipientId.isEmpty()) {
            // Broadcast message
            event = new MessageEvent(
                    UUID.randomUUID(),
                    sender.getId(),
                    sender.getLogin(),
                    null,
                    null,
                    messageContent,
                    true,
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
        } else {
            // Private message
            try {
                UUID recipientUUID = UUID.fromString(recipientId);
                User recipient = userService.find(recipientUUID).orElse(null);
                if (recipient == null) {
                    log.warning("Recipient not found: " + recipientId);
                    return;
                }
                event = new MessageEvent(
                        UUID.randomUUID(),
                        sender.getId(),
                        sender.getLogin(),
                        recipient.getId(),
                        recipient.getLogin(),
                        messageContent,
                        false,
                        LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                );
            } catch (IllegalArgumentException e) {
                log.warning("Invalid recipient ID format: " + recipientId);
                return;
            }
        }

        // Fire the CDI event - PushMessageObserver will handle pushing to clients
        messageEvent.fire(event);

        // Clear the message content after sending
        messageContent = "";
    }
}
