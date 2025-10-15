package pl.edu.pg.eti.kask.rpg.game.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.game.repository.api.GameRepository;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class GameService {
    private final GameRepository gameRepository;

    @Inject
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Optional<Game> find(UUID id) {
        return gameRepository.find(id);
    }

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public void create(Game game) {
        gameRepository.create(game);
    }
}
