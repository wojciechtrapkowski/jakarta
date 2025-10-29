package pl.edu.pg.eti.kask.rpg.review.controllers.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pl.edu.pg.eti.kask.rpg.review.dto.*;

import java.util.UUID;

@Path("")
public interface ReviewController {

    @GET
    @Path("/games/{gameId}/reviews")
    @Produces(MediaType.APPLICATION_JSON)
    GetReviewsResponse getReviews(@PathParam("gameId") UUID gameId);

    @GET
    @Path("/games/{gameId}/reviews/{reviewId}")
    @Produces(MediaType.APPLICATION_JSON)
    GetReviewResponse getReview(@PathParam("gameId") UUID gameId,
                                @PathParam("reviewId") UUID reviewId);

    @PUT
    @Path("/games/{gameId}/reviews/{reviewId}")
    @Consumes(MediaType.APPLICATION_JSON)
    void createReview(@PathParam("gameId") UUID gameId,
                      @PathParam("reviewId") UUID reviewId,
                      PutReviewRequest request);

    @PATCH
    @Path("/games/{gameId}/reviews/{reviewId}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateReview(@PathParam("gameId") UUID gameId,
                      @PathParam("reviewId") UUID reviewId,
                      PatchReviewRequest request);

    @DELETE
    @Path("/games/{gameId}/reviews/{reviewId}")
    void deleteReview(@PathParam("gameId") UUID gameId,
                      @PathParam("reviewId") UUID reviewId);
}