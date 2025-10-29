package pl.edu.pg.eti.kask.rpg.review.dto.function;

import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.review.dto.PatchReviewRequest;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;

import java.util.function.BiFunction;

import java.util.function.BiFunction;

public class PatchReviewRequestFunction implements BiFunction<Review, PatchReviewRequest, Review> {

    @Override
    public Review apply(Review entity, PatchReviewRequest request) {
        return Review.builder()
                .id(entity.getId())
                .description(request.getDescription() != null ? request.getDescription() : entity.getDescription())
                .mark(request.getMark() != null ? request.getMark() : entity.getMark())
                .gameId(entity.getGameId())
                .userId(entity.getUserId())
                .dateOfCreation(entity.getDateOfCreation())
                .build();
    }
}