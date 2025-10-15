package pl.edu.pg.eti.kask.rpg.game.repository.api;

import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.repository.api.Repository;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;

import java.util.UUID;

public interface GameRepository extends Repository<Game, UUID> {
}
