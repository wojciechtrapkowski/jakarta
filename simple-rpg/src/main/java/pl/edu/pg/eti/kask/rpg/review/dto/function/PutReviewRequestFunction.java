package pl.edu.pg.eti.kask.rpg.review.dto.function;

import pl.edu.pg.eti.kask.rpg.review.dto.PutReviewRequest;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.BiFunction;

public class PutReviewRequestFunction implements BiFunction<UUID, PutReviewRequest, Review> {

    @Override
    public Review apply(UUID id, PutReviewRequest request) {
        return Review.builder()
                .id(id)
                .description(request.getDescription())
                .mark(request.getMark())
                .userId(request.getUserId())
                .dateOfCreation(LocalDate.now())
                .build();
    }
}