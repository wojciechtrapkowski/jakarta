package pl.edu.pg.eti.kask.rpg.game.view;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.rpg.component.ModelFunctionFactory;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.game.model.GameModel;
import pl.edu.pg.eti.kask.rpg.game.model.GamesModel;
import pl.edu.pg.eti.kask.rpg.game.service.GameService;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;
import pl.edu.pg.eti.kask.rpg.review.model.ReviewModel;
import pl.edu.pg.eti.kask.rpg.review.service.ReviewService;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.user.service.UserService;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * View bean for rendering single character information.
 */
@ViewScoped
@Named
public class GameView implements Serializable {

    private final GameService gameService;
    private final ReviewService reviewService;
    private final UserService userService;

    private final ModelFunctionFactory factory;

    @Setter
    @Getter
    private UUID id;

    @Getter
    private GameModel game;


    @Inject
    public GameView(GameService service, ReviewService reviewService, UserService userService, ModelFunctionFactory factory) {
        this.gameService = service;
        this.reviewService = reviewService;
        this.userService = userService;
        this.factory = factory;
    }

    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached within
     * field and initialized during init of the view.
     */
    public void init() throws IOException {
        Optional<Game> game = gameService.find(id);
        if (game.isPresent()) {
            List<ReviewModel> reviewModels = reviewService.findAllForGame(game.get().getId())
                    .stream()
                    .map(review -> {
                        ReviewModel model = factory.reviewToModel().apply(review);
                        // fetch username
                        String username = userService.find(review.getUserId())
                                .map(User::getName) // or getUsername()
                                .orElse("Unknown user");
                        model.setUserName(username);
                        return model;
                    }).toList();

            this.game = factory.gameToModel().apply(game.get());
            this.game.setReviews(reviewModels);
        } else {
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "Game not found");
        }
    }

    public String deleteReview(UUID reviewId) {
        reviewService.delete(reviewId);

        return "game_view.xhtml?id=" + id + "&faces-redirect=true";
    }
}
