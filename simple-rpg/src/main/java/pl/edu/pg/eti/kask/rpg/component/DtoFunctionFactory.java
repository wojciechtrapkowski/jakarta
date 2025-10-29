package pl.edu.pg.eti.kask.rpg.component;

import jakarta.enterprise.context.ApplicationScoped;
import pl.edu.pg.eti.kask.rpg.game.dto.function.GameToResponseFunction;
import pl.edu.pg.eti.kask.rpg.game.dto.function.GamesToResponseFunction;
import pl.edu.pg.eti.kask.rpg.game.dto.function.PatchGameRequestFunction;
import pl.edu.pg.eti.kask.rpg.game.dto.function.PutGameRequestFunction;
import pl.edu.pg.eti.kask.rpg.review.dto.function.*;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;
import pl.edu.pg.eti.kask.rpg.review.dto.*;
import pl.edu.pg.eti.kask.rpg.user.dto.GetUserResponse;
import pl.edu.pg.eti.kask.rpg.user.dto.GetUsersResponse;
import pl.edu.pg.eti.kask.rpg.user.dto.PutUserRequest;
import pl.edu.pg.eti.kask.rpg.user.dto.function.*;
import pl.edu.pg.eti.kask.rpg.user.entity.User;

import java.util.function.Function;

/**
 * Factory for creating {@link Function} implementations for converting between various objects used in different layers.
 * Instead of injecting multiple function objects, a single factory is injected.
 */
@ApplicationScoped
public class DtoFunctionFactory {

    // ---------------------- User ----------------------
    public RequestToUserFunction requestToUser() {
        return new RequestToUserFunction();
    }

    public UpdateUserWithRequestFunction updateUser() {
        return new UpdateUserWithRequestFunction();
    }

    public UpdateUserPasswordWithRequestFunction updateUserPassword() {
        return new UpdateUserPasswordWithRequestFunction();
    }

    public UsersToResponseFunction usersToResponse() {
        return new UsersToResponseFunction();
    }

    public UpdateUserAvatarFunction updateUserAvatar() {
        return new UpdateUserAvatarFunction();
    }

    public DeleteUserAvatarFunction deleteUserAvatar() {
        return new DeleteUserAvatarFunction();
    }

    public UserToResponseFunction userToResponse() {
        return new UserToResponseFunction();
    }

    // ---------------------- Game ----------------------
    public GameToResponseFunction gameToResponse() {
        return new GameToResponseFunction();
    }

    public GamesToResponseFunction gamesToResponse() {
        return new GamesToResponseFunction();
    }

    public PutGameRequestFunction putGameRequest() {
        return new PutGameRequestFunction();
    }

    public PatchGameRequestFunction patchGameRequest() {
        return new PatchGameRequestFunction();
    }

    // ---------------------- Review ----------------------
    public PutReviewRequestFunction putReviewRequest() {
        return new PutReviewRequestFunction();
    }

    public PatchReviewRequestFunction patchReviewRequest() {
        return new PatchReviewRequestFunction();
    }

    public ReviewToResponseFunction reviewToResponse() {
        return new ReviewToResponseFunction();
    }

    public ReviewsToResponseFunction reviewsToResponse() {
        return new ReviewsToResponseFunction();
    }
}