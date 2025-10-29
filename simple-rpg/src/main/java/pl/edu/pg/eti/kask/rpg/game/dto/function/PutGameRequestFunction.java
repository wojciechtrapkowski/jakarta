package pl.edu.pg.eti.kask.rpg.game.dto.function;


import java.util.ArrayList;
import java.util.UUID;
import java.util.function.BiFunction;

import pl.edu.pg.eti.kask.rpg.game.dto.PutGameRequest;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;

public class PutGameRequestFunction implements BiFunction<UUID, PutGameRequest, Game> {

    @Override
    public Game apply(UUID id, PutGameRequest request) {
        return Game.builder()
                .id(id)
                .name(request.getName())
                .dateOfRelease(request.getDateOfRelease())
                .type(request.getType())
                .reviews(new ArrayList<>())
                .build();
    }
}