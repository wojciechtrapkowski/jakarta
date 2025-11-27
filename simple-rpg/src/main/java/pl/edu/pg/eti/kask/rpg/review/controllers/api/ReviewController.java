package pl.edu.pg.eti.kask.rpg.review.controllers.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pl.edu.pg.eti.kask.rpg.review.dto.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Path("")
public interface ReviewController {

    @GET
    @Path("/games/{gameId}/reviews")
    @Produces(MediaType.APPLICATION_JSON)
    GetReviewsResponse getReviews(@PathParam("gameId") UUID gameId,
                                  @QueryParam("description") String description,
                                  @QueryParam("minMark") Double minMark,
                                  @QueryParam("maxMark") Double maxMark,
                                  @QueryParam("userId") UUID userId,
                                  @QueryParam("version") Long version,
                                  @QueryParam("createdAfter") LocalDateTime createdAfter,
                                  @QueryParam("createdBefore") LocalDateTime createdBefore,
                                  @QueryParam("modifiedAfter") LocalDateTime modifiedAfter,
                                  @QueryParam("modifiedBefore") LocalDateTime modifiedBefore);

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