package pl.edu.pg.eti.kask.rpg.review.model.function;

import lombok.SneakyThrows;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;
import pl.edu.pg.eti.kask.rpg.review.model.ReviewEditModel;

import java.io.Serializable;
import java.util.function.BiFunction;

public class UpdateReviewWithModelFunction implements BiFunction<Review, ReviewEditModel, Review>, Serializable {

    @Override
    @SneakyThrows
    public Review apply(Review review, ReviewEditModel request) {
        return Review.builder()
                .id(review.getId())
                .description(request.getDescription())
                .mark(request.getMark())
                .dateOfCreation(review.getDateOfCreation())
                .gameId(request.getGameId())
                .userId(review.getUserId())
                .build();
    }

}

