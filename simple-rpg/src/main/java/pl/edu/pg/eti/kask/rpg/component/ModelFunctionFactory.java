package pl.edu.pg.eti.kask.rpg.component;

import jakarta.enterprise.context.ApplicationScoped;
import pl.edu.pg.eti.kask.rpg.game.model.function.GameToModelFunction;
import pl.edu.pg.eti.kask.rpg.game.model.function.GamesToModelFunction;
import pl.edu.pg.eti.kask.rpg.review.model.function.CreateReviewWithModelFunction;
import pl.edu.pg.eti.kask.rpg.review.model.function.ReviewToEditModelFunction;
import pl.edu.pg.eti.kask.rpg.review.model.function.ReviewToModelFunction;
import pl.edu.pg.eti.kask.rpg.review.model.function.UpdateReviewWithModelFunction;

import java.util.function.Function;

@ApplicationScoped
public class ModelFunctionFactory {

    /**
     * Returns a function to convert a single {@link Character} to {@link pl.edu.pg.eti.kask.rpg.game.model.GamesModel}.
     *
     * @return new instance
     */
    public GamesToModelFunction gamesToModel() {
        return new GamesToModelFunction();
    }

    public GameToModelFunction  gameToModel() {
        return new GameToModelFunction();
    }

    public ReviewToModelFunction reviewToModel() {
        return new ReviewToModelFunction();
    }

    public ReviewToEditModelFunction reviewToEditModel() {
        return new ReviewToEditModelFunction();
    }

    public UpdateReviewWithModelFunction updateReview() {
        return new UpdateReviewWithModelFunction();
    }

    public CreateReviewWithModelFunction createReview() {
        return new CreateReviewWithModelFunction();
    }
}
