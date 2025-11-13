package pl.edu.pg.eti.kask.rpg.game.service;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.game.repository.api.GameRepository;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@LocalBean
@Stateless
@NoArgsConstructor(force = true)
@Log
public class GameService {
    private final GameRepository gameRepository;

    @Inject
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public void create(Game game) {
        gameRepository.create(game);
    }

    public Optional<Game> find(UUID id) {
        return gameRepository.find(id);
    }

    public void update(Game game) {
        gameRepository.update(game);
    }

    public void delete(Game game) {
        gameRepository.delete(game);
    }
}
