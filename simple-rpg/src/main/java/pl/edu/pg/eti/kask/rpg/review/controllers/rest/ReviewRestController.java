package pl.edu.pg.eti.kask.rpg.review.controllers.rest;

import jakarta.ejb.EJBException;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.rpg.component.DtoFunctionFactory;
import pl.edu.pg.eti.kask.rpg.game.service.GameService;
import pl.edu.pg.eti.kask.rpg.review.controllers.api.ReviewController;
import pl.edu.pg.eti.kask.rpg.review.dto.GetReviewResponse;
import pl.edu.pg.eti.kask.rpg.review.dto.GetReviewsResponse;
import pl.edu.pg.eti.kask.rpg.review.dto.PatchReviewRequest;
import pl.edu.pg.eti.kask.rpg.review.dto.PutReviewRequest;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;
import pl.edu.pg.eti.kask.rpg.review.service.ReviewService;
import pl.edu.pg.eti.kask.rpg.user.service.UserService;

import java.util.UUID;
import java.util.logging.Level;

@Path("")
@Log
public class ReviewRestController implements ReviewController {

    private final GameService gameService;
    private final ReviewService reviewService;
    private final DtoFunctionFactory factory;

    private final UriInfo uriInfo;
    private final UserService userService;

    @Context
    private HttpServletResponse response;

    @Inject
    public ReviewRestController(
            GameService gameService,
            ReviewService reviewService,
            DtoFunctionFactory factory,
            @SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo,
            UserService userService) {
        this.gameService = gameService;
        this.reviewService = reviewService;
        this.factory = factory;
        this.uriInfo = uriInfo;
        this.userService = userService;
    }


    @Override
    public GetReviewsResponse getReviews(UUID gameId) {
        try {
            return gameService.find(gameId)
                    .map(game -> factory.reviewsToResponse()
                            .apply(reviewService.findAllForGame(gameId)))
                    .orElseThrow(NotFoundException::new);
        }  catch (EJBException ex) {
            log.log(Level.WARNING, ex.getMessage(), ex);
            throw new ForbiddenException(ex.getMessage());
        }
    }

    @Override
    public GetReviewResponse getReview(UUID gameId, UUID reviewId) {
        try {
            return reviewService.findForGame(gameId, reviewId)
                    .map(factory.reviewToResponse())
                    .orElseThrow(NotFoundException::new);
        }  catch (EJBException ex) {
            log.log(Level.WARNING, ex.getMessage(), ex);
            throw new ForbiddenException(ex.getMessage());
        }
    }

    @Override
    public void createReview(UUID gameId, UUID reviewId, PutReviewRequest request) {
        try {
            if (reviewService.findForGame(gameId, reviewId).isPresent()) {
                throw new WebApplicationException(Response.Status.CONFLICT);
            }
            
            Review review = factory.putReviewRequest().apply(reviewId, request);
            review.setGame(gameService.find(gameId).orElseThrow(NotFoundException::new));
            review.setUser(userService.find(request.getUserId()).orElseThrow(NotFoundException::new));
            reviewService.create(review);

            String location = uriInfo.getBaseUriBuilder()
                    .path(ReviewController.class, "getReview")
                    .build(gameId, reviewId)
                    .toString();

            response.setHeader("Location", location);

            throw new WebApplicationException(Response.Status.CREATED);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex);
        }  catch (EJBException ex) {
            log.log(Level.WARNING, ex.getMessage(), ex);
            throw new ForbiddenException(ex.getMessage());
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
        } catch (EJBException ex) {
            log.log(Level.WARNING, ex.getMessage(), ex);
            throw new ForbiddenException(ex.getMessage());
        }
    }

    @Override
    public void deleteReview(UUID gameId, UUID reviewId) {
        try {
            reviewService.findForGame(gameId, reviewId).ifPresentOrElse(
                    reviewService::delete,
                    () -> {
                        throw new NotFoundException();
                    });
        }
         catch (EJBException ex) {
            log.log(Level.WARNING, ex.getMessage(), ex);
            throw new ForbiddenException(ex.getMessage());
        }
    }
}
