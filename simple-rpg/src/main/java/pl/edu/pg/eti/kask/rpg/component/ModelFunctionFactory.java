package pl.edu.pg.eti.kask.rpg.component;

import jakarta.enterprise.context.ApplicationScoped;
import pl.edu.pg.eti.kask.rpg.game.model.function.GameToModelFunction;
import pl.edu.pg.eti.kask.rpg.game.model.function.GamesToModelFunction;
import pl.edu.pg.eti.kask.rpg.review.model.function.ReviewToModelFunction;

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
}
