package pl.edu.pg.eti.kask.rpg.game.dto.function;

import pl.edu.pg.eti.kask.rpg.game.dto.PatchGameRequest;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;

import java.util.function.BiFunction;

public class PatchGameRequestFunction implements BiFunction<Game, PatchGameRequest, Game> {

    @Override
    public Game apply(Game entity, PatchGameRequest request) {
        return Game.builder()
                .id(entity.getId())
                .name(request.getName() != null ? request.getName() : entity.getName())
                .dateOfRelease(request.getDateOfRelease() != null ? request.getDateOfRelease() : entity.getDateOfRelease())
                .type(request.getType() != null ? request.getType() : entity.getType())
                .reviews(entity.getReviews())
                .build();
    }
}