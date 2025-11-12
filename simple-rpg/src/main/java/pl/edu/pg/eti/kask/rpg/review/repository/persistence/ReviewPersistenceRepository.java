package pl.edu.pg.eti.kask.rpg.review.repository.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;
import pl.edu.pg.eti.kask.rpg.review.repository.api.ReviewRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ReviewPersistenceRepository implements ReviewRepository {
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.entityManager = em;
    }

    public Optional<Review> find(UUID id) {
        return Optional.ofNullable(entityManager.find(Review.class, id));
    }

    @Override
    public List<Review> findAll() {
        return entityManager.createQuery("select r from Review r", Review.class).getResultList();
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
