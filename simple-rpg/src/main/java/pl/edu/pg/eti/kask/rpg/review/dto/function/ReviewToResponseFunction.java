package pl.edu.pg.eti.kask.rpg.review.dto.function;

import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.review.dto.GetReviewResponse;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;

import java.util.function.Function;

import java.util.function.Function;

public class ReviewToResponseFunction implements Function<Review, GetReviewResponse> {

    @Override
    public GetReviewResponse apply(Review review) {
        return GetReviewResponse.builder()
                .id(review.getId())
                .description(review.getDescription())
                .mark(review.getMark())
                .gameId(review.getGame().getId())
                .userId(review.getUser().getId())
                .dateOfCreation(review.getDateOfCreation())
                .build();
    }
}