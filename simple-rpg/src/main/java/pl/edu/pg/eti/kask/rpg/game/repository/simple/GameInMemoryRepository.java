package pl.edu.pg.eti.kask.rpg.game.repository.simple;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import pl.edu.pg.eti.kask.rpg.datastore.component.DataStore;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.game.repository.api.GameRepository;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class GameInMemoryRepository implements GameRepository {
    // Everything is synchronized in DataStore, so we can use ApplicationScoped here.
    private final DataStore store;

    @Inject
    public GameInMemoryRepository(DataStore store) {
        this.store = store;
    }

    @Override
    public Optional<Game> find(UUID id) {
        return store.findAllGames().stream()
                .filter(game -> game.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Game> findAll() {
        return store.findAllGames();
    }

    @Override
    public void create(Game game) {
        store.createGame(game);
    }

    @Override
    public void delete(UUID gameId) {
        store.deleteGame(gameId);
    }

    @Override
    public void update(Game game) {
        store.updateGame(game);
    }
}
