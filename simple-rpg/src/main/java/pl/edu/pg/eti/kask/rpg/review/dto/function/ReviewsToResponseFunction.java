package pl.edu.pg.eti.kask.rpg.review.dto.function;

import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.review.dto.GetReviewsResponse;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReviewsToResponseFunction implements Function<List<Review>, GetReviewsResponse> {

    private final ReviewToResponseFunction reviewToResponse = new ReviewToResponseFunction();

    @Override
    public GetReviewsResponse apply(List<Review> reviews) {
        return GetReviewsResponse.builder()
                .reviews(reviews.stream()
                        .map(reviewToResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}