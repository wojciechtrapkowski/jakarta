package pl.edu.pg.eti.kask.rpg.review.model.function;

import pl.edu.pg.eti.kask.rpg.review.entity.Review;
import pl.edu.pg.eti.kask.rpg.review.model.ReviewCreateModel;

import java.io.Serializable;
import java.util.UUID;
import java.util.function.Function;

public class CreateReviewWithModelFunction implements Function<ReviewCreateModel, Review>, Serializable {

    @Override
    public Review apply(ReviewCreateModel model) {
        return Review.builder()
                .id(UUID.randomUUID())
                .description(model.getDescription())
                .mark(model.getMark())
                .game(model.getGame())
                .user(model.getUser())
                .dateOfCreation(model.getDateOfCreation())
                .build();
    }
}