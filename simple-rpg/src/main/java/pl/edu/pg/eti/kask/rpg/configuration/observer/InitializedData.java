package pl.edu.pg.eti.kask.rpg.configuration.observer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RunAs;
import jakarta.ejb.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.control.RequestContextController;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.game.entity.GameType;
import pl.edu.pg.eti.kask.rpg.game.service.GameService;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;
import pl.edu.pg.eti.kask.rpg.review.service.ReviewService;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.user.entity.UserRoles;
import pl.edu.pg.eti.kask.rpg.user.service.UserService;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.h2.tools.Server;

/**
 * Listener started automatically on CDI application context initialized. Injects proxy to the services and fills
 * database with default content. When using persistence storage application instance should be initialized only during
 * first run in order to init database with starting data. Good place to create first default admin user.
 */
@Singleton
@Startup
@NoArgsConstructor(force=true)
@DeclareRoles({UserRoles.ADMIN, UserRoles.USER})
@TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
@RunAs(UserRoles.ADMIN)
@Log
public class InitializedData {

    /**
     * User service.
     */
    private UserService userService;
    private ReviewService reviewService;
    private GameService   gameService;

    private Pbkdf2PasswordHash passwordHash;

    @Inject
    private SecurityContext securityContext;

    @EJB
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @EJB
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @EJB
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @Inject
    public InitializedData(@SuppressWarnings("CdiInjectionPointsInspection")
                           Pbkdf2PasswordHash passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * CDI observer method that is automatically called when ApplicationScoped context is initialized.
     *
     * @param init initialization event
     */
    public void contextInitialized(@Observes @Initialized(ApplicationScoped.class) Object init) {
        init();
    }

    /**
     * Initializes database with some example values. Should be called after creating this object. This object should be
     * created only once.
     */
    @SneakyThrows
    @PostConstruct
    private void init() {

        if (userService.findAllForInit().isEmpty()) {
            Server webServer = Server.createWebServer(
                    "-web",
                    "-webAllowOthers",
                    "-webPort", "8082"
            ).start();

            // Start the TCP server for JDBC connections (port 9092)
            Server tcpServer = Server.createTcpServer(
                    "-tcp",
                    "-tcpAllowOthers",
                    "-tcpPort", "9092"
            ).start();


            System.out.println("H2 Web Console running at: " + webServer.getURL());
            System.out.println("Use JDBC URL: jdbc:h2:mem:simple-rpg-games");

            User admin = User.builder()
                    .id(UUID.fromString("c4804e0f-769e-4ab9-9ebe-0578fb4f00a6"))
                    .login("admin")
                    .name("System")
                    .surname("Admin")
                    .email("admin@simplerpg.example.com")
                    .password("adminadmin")
                    .roles(List.of(UserRoles.ADMIN, UserRoles.USER))
                    .build();

            User kevin = User.builder()
                    .id(UUID.fromString("81e1c2a9-7f57-439b-b53d-6db88b071e4e"))
                    .login("kevin")
                    .name("Kevin")
                    .surname("Pear")
                    .email("kevin@example.com")
                    .password("useruser")
                    .roles(List.of(UserRoles.USER))
                    .build();

            User alice = User.builder()
                    .id(UUID.fromString("ed6cfb2a-cad7-47dd-9b56-9d1e3c7a4197"))
                    .login("alice")
                    .name("Alice")
                    .surname("Grape")
                    .email("alice@example.com")
                    .password("useruser")
                    .roles(List.of(UserRoles.USER))
                    .build();

            User bob = User.builder()
                    .id(UUID.fromString("ed6cfb2a-cad7-47dd-9b56-9e1b3c7a4197"))
                    .login("bob")
                    .name("Bob")
                    .surname("Tent")
                    .email("bob@example.com")
                    .password("useruser")
                    .roles(List.of(UserRoles.USER))
                    .build();

            userService.create(admin);
            userService.create(kevin);
            userService.create(alice);
            userService.create(bob);

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

            gameService.create(dragonQuest);
            gameService.create(witcher3);
            gameService.create(cyberpunk);

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

            reviewService.create(review1);
            reviewService.create(review2);
            reviewService.create(review3);

            // Print
            System.out.println("[DEBUG] Initialized database with some example values.");
            for (User user : userService.findAllForInit()) {
                System.out.println(user);
            }
            for (Review review : reviewService.findAll()) {
                System.out.println(review);
            }
            for (Game game : gameService.findAll()) {
                System.out.println(game);
            }

            // Remove game
//            Game managedWitcher3 = gameService.find(witcher3.getId()).orElseThrow();
//            gameService.delete(managedWitcher3);
//            System.out.println("[DEBUG] Removed game.");
//            for (User user : userService.findAll()) {
//                System.out.println(user);
//            }
//            for (Review review : reviewService.findAll()) {
//                System.out.println(review);
//            }
//            for (Game game : gameService.findAll()) {
//                System.out.println(game);
//            }
//
//        // Remove review
//        reviewService.delete(review1.getId());
//        System.out.println("[DEBUG] Removed review.");
//        for (User user : userService.findAll()) {
//            System.out.println(user);
//        }
//        for (Review review : reviewService.findAll()) {
//            System.out.println(review);
//        }
//        for (Game game : gameService.findAll()) {
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