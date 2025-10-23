package pl.edu.pg.eti.kask.rpg.review.repository.simple;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import pl.edu.pg.eti.kask.rpg.datastore.component.DataStore;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;
import pl.edu.pg.eti.kask.rpg.review.repository.api.ReviewRepository;
import pl.edu.pg.eti.kask.rpg.user.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ReviewInMemoryRepository implements ReviewRepository {
    // Everything is synchronized in DataStore, so we can use ApplicationScoped here.
    private final DataStore store;

    @Inject
    public ReviewInMemoryRepository(DataStore store) {
        this.store = store;
    }

    @Override
    public Optional<Review> find(UUID id) {
        return store.findAllReviews().stream()
                .filter(review -> review.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Review> findAll() {
        return store.findAllReviews();
    }

    @Override
    public void create(Review review) {
        store.createReview(review);
    }

    @Override
    public void delete(UUID reviewId) {
        store.deleteReview(reviewId);
    }

    @Override
    public void update(Review review) {
        store.updateReview(review);
    }

}
