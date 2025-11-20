package pl.edu.pg.eti.kask.rpg.chat.view;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import pl.edu.pg.eti.kask.rpg.chat.service.MessageService;
import pl.edu.pg.eti.kask.rpg.user.service.UserService;

/**
 * View bean for chat page.
 */
@RequestScoped
@Named
public class ChatView {

    private final MessageService messageService;
    private final UserService userService;
    private final SecurityContext securityContext;

    private String currentUserId;

    @Inject
    public ChatView(MessageService messageService, 
                   UserService userService,
                   SecurityContext securityContext) {
        this.messageService = messageService;
        this.userService = userService;
        this.securityContext = securityContext;
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

    public java.util.List<pl.edu.pg.eti.kask.rpg.user.entity.User> getAllUsers() {
        return userService.findAll();
    }
}
