package pl.edu.pg.eti.kask.rpg.game.model.function;

import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.game.model.GamesModel;

import java.util.List;
import java.util.function.Function;

public class GamesToModelFunction implements Function<List<Game>, GamesModel> {
    @Override
    public GamesModel apply(List<Game> entity) {
        return GamesModel.builder()
                .games(entity.stream()
                        .map(game -> GamesModel.Game.builder()
                                .id(game.getId())
                                .name(game.getName())
                                .build())
                        .toList())
                .build();
    }

}
