package pl.edu.pg.eti.kask.rpg.chat.service;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.rpg.chat.event.MessageEvent;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.user.entity.UserRoles;
import pl.edu.pg.eti.kask.rpg.user.repository.api.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class MessageService {

    private final UserRepository userRepository;
    private final Event<MessageEvent> messageEvent;

    @Inject
    public MessageService(UserRepository userRepository,
                          Event<MessageEvent> messageEvent) {
        this.userRepository = userRepository;
        this.messageEvent = messageEvent;
    }

    @RolesAllowed({UserRoles.USER, UserRoles.ADMIN})
    @Transactional
    public void sendBroadcastMessage(UUID senderId, String content) {
        User sender = userRepository.find(senderId).orElseThrow();

        MessageEvent event = new MessageEvent(
                UUID.randomUUID(),
                sender.getId(),
                sender.getLogin(),
                null,
                null,
                content,
                true,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );

        messageEvent.fire(event);
    }

    @RolesAllowed({UserRoles.USER, UserRoles.ADMIN})
    @Transactional
    public void sendPrivateMessage(UUID senderId, UUID recipientId, String content) {
        User sender = userRepository.find(senderId).orElseThrow();
        User recipient = userRepository.find(recipientId).orElseThrow();

        MessageEvent event = new MessageEvent(
                UUID.randomUUID(),
                sender.getId(),
                sender.getLogin(),
                recipient.getId(),
                recipient.getLogin(),
                content,
                false,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );

        messageEvent.fire(event);
    }
}