package pl.edu.pg.eti.kask.rpg.chat.repository.api;

import pl.edu.pg.eti.kask.rpg.chat.entity.Message;
import pl.edu.pg.eti.kask.rpg.repository.api.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for message entity.
 */
public interface MessageRepository extends Repository<Message, UUID> {
    
    /**
     * Find recent messages for display in chat.
     * 
     * @param limit maximum number of messages to return
     * @return list of recent messages
     */
    List<Message> findRecent(int limit);
    
    /**
     * Find messages between two users.
     * 
     * @param user1Id first user id
     * @param user2Id second user id
     * @param limit maximum number of messages to return
     * @return list of messages
     */
    List<Message> findBetweenUsers(UUID user1Id, UUID user2Id, int limit);
}
