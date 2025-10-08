package pl.edu.pg.eti.kask.rpg.user.service;

import pl.edu.pg.eti.kask.rpg.crypto.component.Pbkdf2PasswordHash;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.user.repository.api.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for all business actions regarding user entity.
 */
public class UserService {

    /**
     * Repository for user entity.
     */
    private final UserRepository repository;

    /**
     * Hash mechanism used for storing users' passwords.
     */
    private final Pbkdf2PasswordHash passwordHash;

    /**
     * @param repository   repository for character entity
     * @param passwordHash hash mechanism used for storing users' passwords
     */
    public UserService(UserRepository repository, Pbkdf2PasswordHash passwordHash) {
        this.repository = repository;
        this.passwordHash = passwordHash;
    }

    /**
     * @param id user's id
     * @return container (can be empty) with user
     */
    public Optional<User> find(UUID id) {
        return repository.find(id);
    }

    /**
     * Seeks for single user using login and password. Can be used in authentication module.
     *
     * @param login user's login
     * @return container (can be empty) with user
     */
    public Optional<User> find(String login) {
        return repository.findByLogin(login);
    }

    public List<User> findAll() {
        return repository.findAll();
    }


    /**
     * Saves new user. Password is hashed using configured hash algorithm.
     *
     * @param user new user to be saved
     */
    public void create(User user) {
        user.setPassword(passwordHash.generate(user.getPassword().toCharArray()));
        repository.create(user);
    }

    /**
     * @param login    user's login
     * @param password user's password
     * @return true if provided login and password are correct
     */
    public boolean verify(String login, String password) {
        return find(login)
                .map(user -> passwordHash.verify(password.toCharArray(), user.getPassword()))
                .orElse(false);
    }

}
