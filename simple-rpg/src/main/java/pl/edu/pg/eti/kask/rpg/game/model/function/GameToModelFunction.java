package pl.edu.pg.eti.kask.rpg.game.model.function;

import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.game.model.GameModel;

import java.util.function.Function;

public class GameToModelFunction implements Function<Game, GameModel> {

    @Override
    public GameModel apply(Game game) {
        if (game == null) {
            return null;
        }

        return GameModel.builder()
                .id(game.getId())
                .name(game.getName())
                .dateOfRelease(game.getDateOfRelease())
                .type(game.getType() != null ? game.getType().name() : null)
                .build();
    }
}