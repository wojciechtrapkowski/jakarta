package pl.edu.pg.eti.kask.rpg.review.repository.api;

import pl.edu.pg.eti.kask.rpg.repository.api.Repository;
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
}
