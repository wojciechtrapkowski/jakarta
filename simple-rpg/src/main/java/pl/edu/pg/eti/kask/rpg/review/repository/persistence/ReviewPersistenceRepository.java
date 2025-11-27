package pl.edu.pg.eti.kask.rpg.review.repository.persistence;

import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import pl.edu.pg.eti.kask.rpg.game.entity.Game_;
import pl.edu.pg.eti.kask.rpg.review.dto.ReviewFilterRequest;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;
import pl.edu.pg.eti.kask.rpg.review.entity.Review_;
import pl.edu.pg.eti.kask.rpg.review.repository.api.ReviewRepository;
import pl.edu.pg.eti.kask.rpg.user.entity.User_;

import java.util.ArrayList;
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
            
            Predicate reviewIdPredicate = cb.equal(root.get(Review_.id), reviewId);
            Predicate gameIdPredicate = cb.equal(root.get(Review_.game).get(Game_.id), gameId);
            
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
            
            Predicate reviewIdPredicate = cb.equal(root.get(Review_.id), reviewId);
            Predicate userIdPredicate = cb.equal(root.get(Review_.user).get(User_.id), userId);
            Predicate gameIdPredicate = cb.equal(root.get(Review_.game).get(Game_.id), gameId);
            
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
        
        Predicate gameIdPredicate = cb.equal(root.get(Review_.game).get(Game_.id), gameId);
        
        cq.select(root).where(gameIdPredicate);
        return entityManager.createQuery(cq).getResultList();
    }

    public List<Review> findAllForUserAndGame(UUID userId, UUID gameId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Review> cq = cb.createQuery(Review.class);
        Root<Review> root = cq.from(Review.class);
        
        Predicate userIdPredicate = cb.equal(root.get(Review_.user).get(User_.id), userId);
        Predicate gameIdPredicate = cb.equal(root.get(Review_.game).get(Game_.id), gameId);
        
        cq.select(root).where(cb.and(userIdPredicate, gameIdPredicate));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Review> findWithFilterForGame(UUID gameId, ReviewFilterRequest filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Review> cq = cb.createQuery(Review.class);
        Root<Review> root = cq.from(Review.class);

        List<Predicate> predicates = new ArrayList<>();

        // Always filter by game
        predicates.add(cb.equal(root.get(Review_.game).get(Game_.id), gameId));

        // Add optional filter predicates
        addFilterPredicates(cb, root, predicates, filter);

        cq.select(root);
        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Review> findWithFilterForUserAndGame(UUID userId, UUID gameId, ReviewFilterRequest filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Review> cq = cb.createQuery(Review.class);
        Root<Review> root = cq.from(Review.class);

        List<Predicate> predicates = new ArrayList<>();

        // Always filter by user and game
        predicates.add(cb.equal(root.get(Review_.user).get(User_.id), userId));
        predicates.add(cb.equal(root.get(Review_.game).get(Game_.id), gameId));

        // Add optional filter predicates
        addFilterPredicates(cb, root, predicates, filter);

        cq.select(root);
        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(cq).getResultList();
    }

    private void addFilterPredicates(CriteriaBuilder cb, Root<Review> root, List<Predicate> predicates, ReviewFilterRequest filter) {
        if (filter.getDescription() != null && !filter.getDescription().isEmpty()) {
            // Escape special LIKE characters to prevent SQL injection
            String escapedDescription = filter.getDescription()
                    .replace("\\", "\\\\")
                    .replace("%", "\\%")
                    .replace("_", "\\_");
            predicates.add(cb.like(cb.lower(root.get(Review_.description)), "%" + escapedDescription.toLowerCase() + "%", '\\'));
        }

        if (filter.getMinMark() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(Review_.mark), filter.getMinMark()));
        }

        if (filter.getMaxMark() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get(Review_.mark), filter.getMaxMark()));
        }

        if (filter.getUserId() != null) {
            predicates.add(cb.equal(root.get(Review_.user).get(User_.id), filter.getUserId()));
        }

        if (filter.getVersion() != null) {
            predicates.add(cb.equal(root.get(Review_.version), filter.getVersion()));
        }

        if (filter.getCreatedAfter() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(Review_.dateOfCreation), filter.getCreatedAfter()));
        }

        if (filter.getCreatedBefore() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get(Review_.dateOfCreation), filter.getCreatedBefore()));
        }

        if (filter.getModifiedAfter() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(Review_.modificationDate), filter.getModifiedAfter()));
        }

        if (filter.getModifiedBefore() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get(Review_.modificationDate), filter.getModifiedBefore()));
        }
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
