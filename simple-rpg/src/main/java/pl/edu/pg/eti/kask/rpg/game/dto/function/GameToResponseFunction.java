package pl.edu.pg.eti.kask.rpg.game.dto.function;

import pl.edu.pg.eti.kask.rpg.game.dto.GetGameResponse;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import java.util.function.Function;

public class GameToResponseFunction implements  Function<Game, GetGameResponse> {
    @Override
    public GetGameResponse apply(Game game) {
        return GetGameResponse.builder()
                .id(game.getId())
                .name(game.getName())
                .dateOfRelease(game.getDateOfRelease())
                .type(game.getType())
                .reviews(game.getReviews())
                .build();
    }
}
