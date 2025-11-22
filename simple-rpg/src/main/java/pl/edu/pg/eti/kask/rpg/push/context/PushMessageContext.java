package pl.edu.pg.eti.kask.rpg.push.context;

import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.rpg.push.dto.Message;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.inject.Inject;

/**
 * CDI bean realizing push messages. Two channels are available, broadcast (sending to all sessions) and user (sending
 * to sessions started by specified user). At this moment those to channels are using the same handling JS function
 * in main.xhtml JSF template but those can be associated with different ones.
 */
@ApplicationScoped
@Log
@NoArgsConstructor(force = true)
public class PushMessageContext {

    /**
     * Channel for sending message to all active sessions.
     */
    private PushContext broadcastChannel;

    /**
     * Channel for sending message to sessions opened by specified user.
     */
    private PushContext userChannel;

    @Inject
    public PushMessageContext(
            @Push(channel = "broadcastChannel") PushContext broadcastChannel,
            @Push(channel = "userChannel") PushContext userChannel
    ) {
        this.broadcastChannel = broadcastChannel;
        this.userChannel = userChannel;
    }

    /**
     * Send push message to all users.
     *
     * @param message message to be sent
     */
    public void notifyAll(Message message) {
        broadcastChannel.send(message);
    }

    /**
     * Send push message to specified user.
     *
     * @param message  message to be sent
     * @param username message receiver
     */
    public void notifyUser(Message message, String username) {
        userChannel.send(message, username);
    }

}
