package pl.edu.pg.eti.kask.rpg.chat.controller.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.rpg.chat.dto.MessageResponse;
import pl.edu.pg.eti.kask.rpg.chat.dto.SendMessageRequest;
import pl.edu.pg.eti.kask.rpg.chat.service.MessageService;
import pl.edu.pg.eti.kask.rpg.user.entity.UserRoles;
import pl.edu.pg.eti.kask.rpg.user.service.UserService;

import java.util.UUID;

/**
 * REST controller for chat messages.
 */
@Path("/api/chat")
@RolesAllowed({UserRoles.USER, UserRoles.ADMIN})
@Log
public class ChatRestController {

    private final MessageService messageService;
    private final UserService userService;
    private final SecurityContext securityContext;

    @Inject
    public ChatRestController(MessageService messageService, 
                             UserService userService,
                             SecurityContext securityContext) {
        this.messageService = messageService;
        this.userService = userService;
        this.securityContext = securityContext;
    }

    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendMessage(SendMessageRequest request) {
        try {
            String login = securityContext.getCallerPrincipal().getName();
            var sender = userService.find(login).orElseThrow();
            
            if (request.getRecipientId() == null || request.getRecipientId().isEmpty()) {
                // Broadcast message
                messageService.sendBroadcastMessage(sender.getId(), request.getContent());
            } else {
                // Private message
                UUID recipientId = UUID.fromString(request.getRecipientId());
                messageService.sendPrivateMessage(sender.getId(), recipientId, request.getContent());
            }
            
            return Response.ok(new MessageResponse("success", "Message sent")).build();
        } catch (Exception e) {
            log.severe("Error sending message: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new MessageResponse("error", e.getMessage()))
                    .build();
        }
    }
}
