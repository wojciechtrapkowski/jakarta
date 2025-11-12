package pl.edu.pg.eti.kask.rpg.review.model.function;

import pl.edu.pg.eti.kask.rpg.review.entity.Review;
import pl.edu.pg.eti.kask.rpg.review.model.ReviewModel;

import java.util.function.Function;

public class ReviewToModelFunction implements Function<Review, ReviewModel> {

    @Override
    public ReviewModel apply(Review review) {
        if (review == null) {
            return null;
        }

        return ReviewModel.builder()
                .id(review.getId())
                .description(review.getDescription())
                .mark(review.getMark())
                .dateOfCreation(review.getDateOfCreation())
                .game(review.getGame())
                .user(review.getUser())
                .build();
    }
}