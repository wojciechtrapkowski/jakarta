package pl.edu.pg.eti.kask.rpg.datastore.component;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;
import pl.edu.pg.eti.kask.rpg.serialization.component.CloningUtility;
import pl.edu.pg.eti.kask.rpg.user.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * For the sake of simplification instead of using real database this example is using a data source object which should
 * be put in servlet context in a single instance. In order to avoid {@link java.util.ConcurrentModificationException}
 * all methods are synchronized. Normally synchronization would be carried on by the database server. Caution, this is
 * very inefficient implementation but can be used to present other mechanisms without obscuration example with ORM
 * usage.
 */
@Log
@ApplicationScoped
@NoArgsConstructor(force = true)
public class DataStore {
    /**
     * Set of all users.
     */
    private final Set<User> users = new HashSet<>();

    private final Set<Review> reviews = new HashSet<>();
    private final Set<Game> games = new HashSet<>();

    /**
     * Component used for creating deep copies.
     */
    private final CloningUtility cloningUtility;

    /**
     * @param cloningUtility component used for creating deep copies
     */
    @Inject
    public DataStore(CloningUtility cloningUtility) {
        this.cloningUtility = cloningUtility;
    }

    /**
     * Seeks for all users.
     *
     * @return list (can be empty) of all users
     */
    public synchronized List<User> findAllUsers() {
        return users.stream()
                .map(cloningUtility::clone)
                .collect(Collectors.toList());
    }

    /**
     * Stores new user.
     *
     * @param value new user to be stored
     * @throws IllegalArgumentException if user with provided id already exists
     */
    public synchronized void createUser(User value) throws IllegalArgumentException {
        if (users.stream().anyMatch(user -> user.getId().equals(value.getId()))) {
            throw new IllegalArgumentException("The user id \"%s\" is not unique".formatted(value.getId()));
        }
        users.add(cloningUtility.clone(value));
    }

    /**
     * Updates existing user.
     *
     * @param value user to be updated
     * @throws IllegalArgumentException if user with the same id does not exist
     */
    public synchronized void updateUser(User value) throws IllegalArgumentException {
        if (users.removeIf(user -> user.getId().equals(value.getId()))) {
            users.add(cloningUtility.clone(value));
        } else {
            throw new IllegalArgumentException("The user with id \"%s\" does not exist".formatted(value.getId()));
        }
    }

    public synchronized List<Game> findAllGames() {
        return games.stream()
                .map(cloningUtility::clone)
                .collect(Collectors.toList());
    }

    public synchronized void createGame(Game value) throws IllegalArgumentException {
        if (games.stream().anyMatch(game -> game.getId().equals(value.getId()))) {
            throw new IllegalArgumentException("The game id \"%s\" is not unique".formatted(value.getId()));
        }
        games.add(cloningUtility.clone(value));
    }

    public synchronized void updateGame(Game value) throws IllegalArgumentException {
        if (games.removeIf(game -> game.getId().equals(value.getId()))) {
            games.add(cloningUtility.clone(value));
        } else {
            throw new IllegalArgumentException("The game with id \"%s\" does not exist".formatted(value.getId()));
        }
    }

    public synchronized List<Review> findAllReviews() {
        return reviews.stream()
                .map(cloningUtility::clone)
                .collect(Collectors.toList());
    }

    public synchronized void createReview(Review value) throws IllegalArgumentException {
        if (reviews.stream().anyMatch(review -> review.getId().equals(value.getId()))) {
            throw new IllegalArgumentException("The review id \"%s\" is not unique".formatted(value.getId()));
        }

        User user = users.stream()
                .filter(u -> u.getId().equals(value.getUser().getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Game game = games.stream()
                .filter(g -> g.getId().equals(value.getGame().getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));

        Review clonedReview = cloningUtility.clone(value);

        user.getReviews().add(clonedReview);
        game.getReviews().add(clonedReview);

        clonedReview.setUser(user);
        clonedReview.setGame(game);

        reviews.add(clonedReview);
    }

    public synchronized void updateReview(Review value) throws IllegalArgumentException {
        if (reviews.removeIf(review -> review.getId().equals(value.getId()))) {
            reviews.add(cloningUtility.clone(value));
        } else {
            throw new IllegalArgumentException("The user with id \"%s\" does not exist".formatted(value.getId()));
        }
    }
}
