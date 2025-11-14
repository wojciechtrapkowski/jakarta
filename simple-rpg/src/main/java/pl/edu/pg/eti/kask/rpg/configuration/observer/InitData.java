package pl.edu.pg.eti.kask.rpg.configuration.observer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RunAs;
import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.game.entity.GameType;
import pl.edu.pg.eti.kask.rpg.game.repository.api.GameRepository;

import pl.edu.pg.eti.kask.rpg.review.entity.Review;
import pl.edu.pg.eti.kask.rpg.review.repository.api.ReviewRepository;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.user.entity.UserRoles;
import pl.edu.pg.eti.kask.rpg.user.repository.api.UserRepository;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Listener started automatically on CDI application context initialized. Injects proxy to the services and fills
 * database with default content. When using persistence storage application instance should be initialized only during
 * first run in order to init database with starting data. Good place to create first default admin user.
 */
@Singleton
@Startup
//@TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
@NoArgsConstructor
//@DependsOn("InitializeAdminService")
@DeclareRoles({UserRoles.ADMIN, UserRoles.USER})
@RunAs(UserRoles.ADMIN)
@Log
public class InitData {

    /**
     * User service.
     */
    @Inject
    private UserRepository userRepository;
    @Inject

    private ReviewRepository reviewRepository;
    @Inject
    private GameRepository gameRepository;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @Inject
    private SecurityContext securityContext;

    public InitData(UserRepository userRepository, ReviewRepository reviewRepository, GameRepository gameRepository, Pbkdf2PasswordHash passwordHash, SecurityContext securityContext) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.gameRepository = gameRepository;
        this.passwordHash = passwordHash;
        this.securityContext = securityContext;
    }
    /**
     * Initializes database with some example values. Should be called after creating this object. This object should be
     * created only once.
     */

    @PostConstruct
    @SneakyThrows
    public void init() {
        log.info("[DEBUG] Starting initialization...");


        if (userRepository.findAll().isEmpty()) {
            User admin = User.builder()
                    .id(UUID.fromString("c4804e0f-769e-4ab9-9ebe-0578fb4f00a6"))
                    .login("admin")
                    .name("System")
                    .surname("Admin")
                    .email("admin@simplerpg.example.com")
                    .password(passwordHash.generate("adminadmin".toCharArray()))
                    .roles(List.of(UserRoles.ADMIN, UserRoles.USER))
                    .build();

            User kevin = User.builder()
                    .id(UUID.fromString("81e1c2a9-7f57-439b-b53d-6db88b071e4e"))
                    .login("kevin")
                    .name("Kevin")
                    .surname("Pear")
                    .email("kevin@example.com")
                    .roles(List.of(UserRoles.USER))
                    .password(passwordHash.generate("useruser".toCharArray()))
                    .build();

            User alice = User.builder()
                    .id(UUID.fromString("ed6cfb2a-cad7-47dd-9b56-9d1e3c7a4197"))
                    .login("alice")
                    .name("Alice")
                    .surname("Grape")
                    .email("alice@example.com")
                    .roles(List.of(UserRoles.USER))
                    .password(passwordHash.generate("useruser".toCharArray()))
                    .build();

            User bob = User.builder()
                    .id(UUID.fromString("ed6cfb2a-cad7-47dd-9b56-9e1b3c7a4197"))
                    .login("bob")
                    .name("Bob")
                    .surname("Tent")
                    .email("bob@example.com")
                    .roles(List.of(UserRoles.USER))
                    .password(passwordHash.generate("useruser".toCharArray()))
                    .build();

            userRepository.create(admin);
            userRepository.create(kevin);
            userRepository.create(alice);
            userRepository.create(bob);

            Game dragonQuest = Game.builder()
                    .id(UUID.fromString("ed6cfb2a-cad7-47dd-9b56-9d1e3c7b4197"))
                    .name("Dragon Quest XI")
                    .type(GameType.RPG)
                    .dateOfRelease(LocalDate.of(2017, 7, 29))
                    .build();

            Game witcher3 = Game.builder()
                    .id(UUID.randomUUID())
                    .name("The Witcher 3: Wild Hunt")
                    .type(GameType.RPG)
                    .dateOfRelease(LocalDate.of(2015, 5, 19))
                    .build();


            Game cyberpunk = Game.builder()
                    .id(UUID.randomUUID())
                    .name("Cyberpunk 2077")
                    .type(GameType.ACTION_RPG)
                    .dateOfRelease(LocalDate.of(2020, 12, 10))
                    .build();

            gameRepository.create(dragonQuest);
            gameRepository.create(witcher3);
            gameRepository.create(cyberpunk);

            var review1 = Review.builder()
                    .id(UUID.fromString("ed6cfb2a-cad7-47dd-9b56-9d1e3c7b4117"))
                    .description("Great gameplay and stunning graphics!")
                    .dateOfCreation(LocalDate.now().minusDays(5))
                    .mark(9.5)
                    .user(kevin)
                    .game(dragonQuest)
                    .build();

            var review2 = Review.builder()
                    .id(UUID.randomUUID())
                    .description("Very immersive story, loved the characters.")
                    .dateOfCreation(LocalDate.now().minusDays(3))
                    .mark(8.7)
                    .user(alice)
                    .game(witcher3)
                    .build();

            var review3 = Review.builder()
                    .id(UUID.randomUUID())
                    .description("Not bad, but a bit repetitive after some time.")
                    .dateOfCreation(LocalDate.now().minusDays(1))
                    .mark(6.8)
                    .user(bob)
                    .game(cyberpunk)
                    .build();

            reviewRepository.create(review1);
            reviewRepository.create(review2);
            reviewRepository.create(review3);

            // Print
            System.out.println("[DEBUG] Initialized database with some example values.");
            for (User user : userRepository.findAll()) {
                System.out.println(user);
            }
            for (Review review : reviewRepository.findAll()) {
                System.out.println(review);
            }
            for (Game game : gameRepository.findAll()) {
                System.out.println(game);
            }

            // Remove game
//            Game managedWitcher3 = gameRepository.find(witcher3.getId()).orElseThrow();
//            gameRepository.delete(managedWitcher3);
//            System.out.println("[DEBUG] Removed game.");
//            for (User user : userRepository.findAll()) {
//                System.out.println(user);
//            }
//            for (Review review : reviewRepository.findAll()) {
//                System.out.println(review);
//            }
//            for (Game game : gameRepository.findAll()) {
//                System.out.println(game);
//            }
//
//        // Remove review
//        reviewRepository.delete(review1.getId());
//        System.out.println("[DEBUG] Removed review.");
//        for (User user : userRepository.findAll()) {
//            System.out.println(user);
//        }
//        for (Review review : reviewRepository.findAll()) {
//            System.out.println(review);
//        }
//        for (Game game : gameRepository.findAll()) {
//            System.out.println(game);
//        }
        }
    }

    /**
     * @param name name of the desired resource
     * @return array of bytes read from the resource
     */
    @SneakyThrows
    private byte[] getResourceAsByteArray(String name) {
        try (InputStream is = this.getClass().getResourceAsStream(name)) {
            if (is != null) {
                return is.readAllBytes();
            } else {
                throw new IllegalStateException("Unable to get resource %s".formatted(name));
            }
        }
    }
}