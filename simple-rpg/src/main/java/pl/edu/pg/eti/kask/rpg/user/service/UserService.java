package pl.edu.pg.eti.kask.rpg.user.service;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.security.enterprise.SecurityContext;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.rpg.controller.interceptor.LogOperation;
import pl.edu.pg.eti.kask.rpg.controller.interceptor.OperationLoggingInterceptor;
import pl.edu.pg.eti.kask.rpg.controller.servlet.exception.NotFoundException;
import pl.edu.pg.eti.kask.rpg.crypto.component.Pbkdf2PasswordHash;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.user.entity.UserRoles;
import pl.edu.pg.eti.kask.rpg.user.repository.api.UserAvatarRepository;
import pl.edu.pg.eti.kask.rpg.user.repository.api.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for all business actions regarding user entity.
 */
@LocalBean
@Stateless
@Interceptors(OperationLoggingInterceptor.class)
@NoArgsConstructor(force = true)
public class UserService {

    /**
     * Repository for user entity.
     */
    private final UserRepository userRepository;

    private final UserAvatarRepository userAvatarRepository;
    /**
     * Hash mechanism used for storing users' passwords.
     */
    private final Pbkdf2PasswordHash passwordHash;


    private final SecurityContext securityContext;


    @Inject
    public UserService(UserRepository userRepository, UserAvatarRepository userAvatarRepository, Pbkdf2PasswordHash passwordHash,  SecurityContext securityContext) {
        this.userRepository = userRepository;
        this.userAvatarRepository = userAvatarRepository;
        this.passwordHash = passwordHash;
        this.securityContext = securityContext;
    }

    /**
     * @param id user's id
     * @return container (can be empty) with user
     */
    public Optional<User> find(UUID id) {
        return userRepository.find(id);
    }

    /**
     * Seeks for single user using login and password. Can be used in authentication module.
     *
     * @param login user's login
     * @return container (can be empty) with user
     */
    public Optional<User> find(String login) {
        return userRepository.findByLogin(login);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Saves new user. Password is hashed using configured hash algorithm.
     *
     * @param user new user to be saved
     */
    @LogOperation("CREATE")
    public void create(User user) {
//        user.setPassword(passwordHash.generate(user.getPassword().toCharArray()));
        userRepository.create(user);
    }

    @LogOperation("UPDATE")
    public void update(User user) {
        userRepository.update(user);
    }

    @LogOperation("DELETE")
    public void delete(User user) {
        userRepository.delete(user);
    }

    public void updateAvatar(UUID id, InputStream is) {
        userRepository.find(id).ifPresent(user -> {
            try {
                userAvatarRepository.saveAvatar(id, is);
            } catch (IOException ex) {
                throw new NotFoundException("User not found");
            }
        });
    }

    public byte[] getAvatar(UUID id) {
        return userRepository.find(id)
                .map(user -> {
                    try {
                        return userAvatarRepository.getAvatar(id);
                    } catch (IOException ex) {
                        throw new NotFoundException("User not found");
                    }
                })
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void deleteAvatar(UUID id) {
        userRepository.find(id).ifPresent(user -> {
            try {
                userAvatarRepository.deleteAvatar(id);
            } catch (IOException ex) {
                throw new NotFoundException("User not found");
            }
        });
    }

    /**
     * @param login    user's login
     * @param password user's password
     * @return true if provided login and password are correct
     */
    @PermitAll
    public boolean verify(String login, String password) {
        return find(login)
                .map(user -> passwordHash.verify(password.toCharArray(), user.getPassword()))
                .orElse(false);
    }

}
