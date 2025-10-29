package pl.edu.pg.eti.kask.rpg.review.controllers.rest;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import pl.edu.pg.eti.kask.rpg.component.DtoFunctionFactory;
import pl.edu.pg.eti.kask.rpg.game.service.GameService;
import pl.edu.pg.eti.kask.rpg.review.controllers.api.ReviewController;
import pl.edu.pg.eti.kask.rpg.review.dto.GetReviewResponse;
import pl.edu.pg.eti.kask.rpg.review.dto.GetReviewsResponse;
import pl.edu.pg.eti.kask.rpg.review.dto.PatchReviewRequest;
import pl.edu.pg.eti.kask.rpg.review.dto.PutReviewRequest;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;
import pl.edu.pg.eti.kask.rpg.review.service.ReviewService;

import java.util.UUID;

@Path("")
public class ReviewRestController implements ReviewController {

    private final GameService gameService;
    private final ReviewService reviewService;
    private final DtoFunctionFactory factory;

    private final UriInfo uriInfo;

    @Context
    private HttpServletResponse response;

    @Inject
    public ReviewRestController(
            GameService gameService,
            ReviewService reviewService,
            DtoFunctionFactory factory,
            @SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo
    ) {
        this.gameService = gameService;
        this.reviewService = reviewService;
        this.factory = factory;
        this.uriInfo = uriInfo;
    }


    @Override
    public GetReviewsResponse getReviews(UUID gameId) {
        return gameService.find(gameId)
                .map(game -> factory.reviewsToResponse()
                        .apply(reviewService.findAllForGame(gameId)))
                .orElseThrow(NotFoundException::new);

    }

    @Override
    public GetReviewResponse getReview(UUID gameId, UUID reviewId) {
        return reviewService.findForGame(gameId, reviewId)
                .map(factory.reviewToResponse())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void createReview(UUID gameId, UUID reviewId, PutReviewRequest request) {
        try {
            Review review = factory.putReviewRequest().apply(reviewId, request);
            review.setGameId(gameId);
            reviewService.create(review);

            String location = uriInfo.getBaseUriBuilder()
                    .path(ReviewController.class, "getReview")
                    .build(gameId, reviewId)
                    .toString();

            response.setHeader("Location", location);

            throw new WebApplicationException(Response.Status.CREATED);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex);
        }
    }

    @Override
    public void updateReview(UUID gameId, UUID reviewId, PatchReviewRequest request) {
        try {
            Review existing = reviewService.findForGame(gameId, reviewId)
                    .orElseThrow(() -> new NotFoundException("Review not found: " + reviewId));

            Review updated = factory.patchReviewRequest().apply(existing, request);

            reviewService.update(updated);

            // Return 204 No Content for successful update
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex);
        }
    }

    @Override
    public void deleteReview(UUID gameId, UUID reviewId) {

        reviewService.findForGame(gameId, reviewId).ifPresentOrElse(
                review -> reviewService.delete(review.getId()),
                () -> {
                    throw new NotFoundException();
                });
    }
}
