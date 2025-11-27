package pl.edu.pg.eti.kask.rpg.review.repository.persistence;

import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Review> cq = cb.createQuery(Review.class);
            Root<Review> root = cq.from(Review.class);
            
            Predicate reviewIdPredicate = cb.equal(root.get("id"), reviewId);
            Predicate gameIdPredicate = cb.equal(root.get("game").get("id"), gameId);
            
            cq.select(root).where(cb.and(reviewIdPredicate, gameIdPredicate));
            
            Review review = entityManager.createQuery(cq).getSingleResult();
            return Optional.of(review);
        }
        catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Review> findForUserAndGame(UUID reviewId, UUID userId, UUID gameId) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Review> cq = cb.createQuery(Review.class);
            Root<Review> root = cq.from(Review.class);
            
            Predicate reviewIdPredicate = cb.equal(root.get("id"), reviewId);
            Predicate userIdPredicate = cb.equal(root.get("user").get("id"), userId);
            Predicate gameIdPredicate = cb.equal(root.get("game").get("id"), gameId);
            
            cq.select(root).where(cb.and(reviewIdPredicate, userIdPredicate, gameIdPredicate));
            
            Review review = entityManager.createQuery(cq).getSingleResult();
            return Optional.of(review);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Review> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Review> cq = cb.createQuery(Review.class);
        Root<Review> root = cq.from(Review.class);
        cq.select(root);
        return entityManager.createQuery(cq).getResultList();
    }

    public List<Review> findAllForGame(UUID gameId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Review> cq = cb.createQuery(Review.class);
        Root<Review> root = cq.from(Review.class);
        
        Predicate gameIdPredicate = cb.equal(root.get("game").get("id"), gameId);
        
        cq.select(root).where(gameIdPredicate);
        return entityManager.createQuery(cq).getResultList();
    }

    public List<Review> findAllForUserAndGame(UUID userId, UUID gameId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Review> cq = cb.createQuery(Review.class);
        Root<Review> root = cq.from(Review.class);
        
        Predicate userIdPredicate = cb.equal(root.get("user").get("id"), userId);
        Predicate gameIdPredicate = cb.equal(root.get("game").get("id"), gameId);
        
        cq.select(root).where(cb.and(userIdPredicate, gameIdPredicate));
        return entityManager.createQuery(cq).getResultList();
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
