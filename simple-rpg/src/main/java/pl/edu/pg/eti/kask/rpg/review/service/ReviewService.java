package pl.edu.pg.eti.kask.rpg.review.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;
import pl.edu.pg.eti.kask.rpg.review.repository.api.ReviewRepository;
import pl.edu.pg.eti.kask.rpg.serialization.component.CloningUtility;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.user.repository.api.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Inject
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Optional<Review> find(UUID id) {
        return reviewRepository.find(id);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public void create(Review review) {
        reviewRepository.create(review);
    }
}
