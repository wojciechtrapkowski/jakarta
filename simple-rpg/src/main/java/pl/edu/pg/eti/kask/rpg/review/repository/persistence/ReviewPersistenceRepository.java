package pl.edu.pg.eti.kask.rpg.review.repository.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;
import pl.edu.pg.eti.kask.rpg.review.repository.api.ReviewRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Dependent
public class ReviewPersistenceRepository implements ReviewRepository {
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.entityManager = em;
    }

    public Optional<Review> find(UUID id) {
        return Optional.ofNullable(entityManager.find(Review.class, id));
    }

    public Optional<Review> findForGame(UUID reviewId, UUID gameId) {
        try {
            Review review = entityManager.createQuery(
                            "SELECT r FROM Review r WHERE r.id = :reviewId AND r.game.id = :gameId", Review.class)
                    .setParameter("reviewId", reviewId)
                    .setParameter("gameId", gameId)
                    .getSingleResult();

            return Optional.of(review);
        }
        catch (NoResultException e) {
            return Optional.empty();
        }
    }
    public Optional<Review> findForUserAndGame(UUID reviewId, UUID userId, UUID gameId) {
        try {
            Review review = entityManager.createQuery(
                            "SELECT r FROM Review r WHERE r.id = :reviewId AND r.user.id = :userId AND r.game.id = :gameId", Review.class)
                    .setParameter("reviewId", reviewId)
                    .setParameter("userId", userId)
                    .setParameter("gameId", gameId)
                    .getSingleResult();

            return Optional.of(review);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Review> findAll() {
        return entityManager.createQuery("select r from Review r", Review.class).getResultList();
    }

    public List<Review> findAllForGame(UUID gameId) {
        return entityManager.createQuery(
                        "SELECT r FROM Review r WHERE r.game.id = :gameId", Review.class)
                .setParameter("gameId", gameId)
                .getResultList();
    }

    public List<Review> findAllForUserAndGame(UUID userId, UUID gameId) {
        return entityManager.createQuery(
                        "SELECT r FROM Review r WHERE r.user.id = :userId AND r.game.id = :gameId", Review.class)
                .setParameter("userId", userId).setParameter("gameId", gameId)
                .getResultList();
    }

    @Override
    public void create(Review entity) {
        entityManager.persist(entity);
    }

    @Override
    public void delete(Review entity) {
        entityManager.remove(entityManager.find(Review.class, entity.getId()));
    }

    @Override
    public void update(Review entity) {
        entityManager.merge(entity);
    }
}
