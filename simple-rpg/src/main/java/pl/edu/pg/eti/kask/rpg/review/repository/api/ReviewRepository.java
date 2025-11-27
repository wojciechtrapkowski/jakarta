package pl.edu.pg.eti.kask.rpg.review.repository.api;

import pl.edu.pg.eti.kask.rpg.repository.api.Repository;
import pl.edu.pg.eti.kask.rpg.review.dto.ReviewFilterRequest;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends Repository<Review, UUID> {
    public List<Review> findAllForGame(UUID gameId);

    public List<Review> findAllForUserAndGame(UUID userId, UUID gameId);

    public Optional<Review> findForGame(UUID reviewId, UUID gameId);
    public Optional<Review>
    findForUserAndGame(UUID reviewId, UUID userId, UUID gameId);

    /**
     * Find all reviews for a game matching the given filter criteria.
     * All filter fields are optional and combined with AND operator.
     *
     * @param gameId the game ID
     * @param filter the filter criteria
     * @return list of reviews matching the criteria
     */
    List<Review> findWithFilterForGame(UUID gameId, ReviewFilterRequest filter);

    /**
     * Find all reviews for a user and game matching the given filter criteria.
     * All filter fields are optional and combined with AND operator.
     *
     * @param userId the user ID
     * @param gameId the game ID
     * @param filter the filter criteria
     * @return list of reviews matching the criteria
     */
    List<Review> findWithFilterForUserAndGame(UUID userId, UUID gameId, ReviewFilterRequest filter);
}
