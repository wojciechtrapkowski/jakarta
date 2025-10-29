package pl.edu.pg.eti.kask.rpg.game.dto.function;

import pl.edu.pg.eti.kask.rpg.game.dto.GetGameResponse;
import pl.edu.pg.eti.kask.rpg.game.dto.GetGamesResponse;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GamesToResponseFunction implements Function<List<Game>, GetGamesResponse> {
    private final GameToResponseFunction gameToResponse = new GameToResponseFunction();
    @Override
    public GetGamesResponse apply(List<Game> games) {
        return GetGamesResponse.builder()
                .games(games.stream()
                        .map(gameToResponse)
                        .collect(Collectors.toList())).build();
    }
}

