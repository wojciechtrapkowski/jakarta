package pl.edu.pg.eti.kask.rpg.chat.repository.persistence;

import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.edu.pg.eti.kask.rpg.chat.entity.Message;
import pl.edu.pg.eti.kask.rpg.chat.repository.api.MessageRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA implementation of message repository.
 */
@Dependent
public class MessagePersistenceRepository implements MessageRepository {

    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Message> find(UUID id) {
        return Optional.ofNullable(em.find(Message.class, id));
    }

    @Override
    public List<Message> findAll() {
        return em.createQuery("SELECT m FROM Message m ORDER BY m.timestamp DESC", Message.class)
                .getResultList();
    }

    @Override
    public void create(Message entity) {
        em.persist(entity);
    }

    @Override
    public void delete(Message entity) {
        em.remove(em.merge(entity));
    }

    @Override
    public void update(Message entity) {
        em.merge(entity);
    }

    @Override
    public List<Message> findRecent(int limit) {
        return em.createQuery("SELECT m FROM Message m ORDER BY m.timestamp DESC", Message.class)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Message> findBetweenUsers(UUID user1Id, UUID user2Id, int limit) {
        return em.createQuery(
                "SELECT m FROM Message m WHERE " +
                "(m.sender.id = :user1 AND m.recipient.id = :user2) OR " +
                "(m.sender.id = :user2 AND m.recipient.id = :user1) " +
                "ORDER BY m.timestamp DESC", Message.class)
                .setParameter("user1", user1Id)
                .setParameter("user2", user2Id)
                .setMaxResults(limit)
                .getResultList();
    }
}
