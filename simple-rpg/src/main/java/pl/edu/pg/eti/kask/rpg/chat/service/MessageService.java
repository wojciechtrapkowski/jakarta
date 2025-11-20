package pl.edu.pg.eti.kask.rpg.chat.service;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.rpg.chat.entity.Message;
import pl.edu.pg.eti.kask.rpg.chat.event.MessageEvent;
import pl.edu.pg.eti.kask.rpg.chat.repository.api.MessageRepository;
import pl.edu.pg.eti.kask.rpg.controller.interceptor.LogOperation;
import pl.edu.pg.eti.kask.rpg.controller.interceptor.OperationLoggingInterceptor;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.user.entity.UserRoles;
import pl.edu.pg.eti.kask.rpg.user.repository.api.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for chat messages.
 */
@LocalBean
@Stateless
@Interceptors(OperationLoggingInterceptor.class)
@NoArgsConstructor(force = true)
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final Event<MessageEvent> messageEvent;

    @Inject
    public MessageService(MessageRepository messageRepository, 
                         UserRepository userRepository,
                         Event<MessageEvent> messageEvent) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messageEvent = messageEvent;
    }

    /**
     * Send a message to all users.
     */
    @RolesAllowed({UserRoles.USER, UserRoles.ADMIN})
    @LogOperation("CREATE")
    public void sendBroadcastMessage(UUID senderId, String content) {
        User sender = userRepository.find(senderId).orElseThrow();
        
        Message message = Message.builder()
                .sender(sender)
                .content(content)
                .timestamp(LocalDateTime.now())
                .broadcast(true)
                .build();
        
        messageRepository.create(message);
        
        // Fire CDI event
        fireMessageEvent(message);
    }

    /**
     * Send a message to a specific user.
     */
    @RolesAllowed({UserRoles.USER, UserRoles.ADMIN})
    @LogOperation("CREATE")
    public void sendPrivateMessage(UUID senderId, UUID recipientId, String content) {
        User sender = userRepository.find(senderId).orElseThrow();
        User recipient = userRepository.find(recipientId).orElseThrow();
        
        Message message = Message.builder()
                .sender(sender)
                .recipient(recipient)
                .content(content)
                .timestamp(LocalDateTime.now())
                .broadcast(false)
                .build();
        
        messageRepository.create(message);
        
        // Fire CDI event
        fireMessageEvent(message);
    }

    private void fireMessageEvent(Message message) {
        MessageEvent event = new MessageEvent(
                message.getId(),
                message.getSender().getId(),
                message.getSender().getLogin(),
                message.getRecipient() != null ? message.getRecipient().getId() : null,
                message.getContent(),
                message.isBroadcast(),
                message.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
        
        messageEvent.fire(event);
    }

    @RolesAllowed({UserRoles.USER, UserRoles.ADMIN})
    public List<Message> getRecentMessages(int limit) {
        return messageRepository.findRecent(limit);
    }

    @RolesAllowed({UserRoles.USER, UserRoles.ADMIN})
    public Optional<Message> find(UUID id) {
        return messageRepository.find(id);
    }

    @RolesAllowed({UserRoles.USER, UserRoles.ADMIN})
    public List<Message> findAll() {
        return messageRepository.findAll();
    }
}
