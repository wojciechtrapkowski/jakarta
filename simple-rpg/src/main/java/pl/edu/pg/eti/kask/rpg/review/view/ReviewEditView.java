package pl.edu.pg.eti.kask.rpg.review.view;

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
import pl.edu.pg.eti.kask.rpg.review.model.ReviewEditModel;
import pl.edu.pg.eti.kask.rpg.review.model.ReviewModel;
import pl.edu.pg.eti.kask.rpg.review.service.ReviewService;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.user.service.UserService;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ViewScoped
@Named
public class ReviewEditView implements Serializable {


    @Inject
    private ReviewService reviewService;

    @Inject
    private UserService userService;

    @Inject
    private GameService gameService;

    @Inject
    private ModelFunctionFactory factory;

    @Getter
    private List<GameModel> games; // for dropdown

    @Getter
    @Setter
    private UUID reviewId;

    @Getter
    @Setter
    private ReviewEditModel review;

    @Inject
    public ReviewEditView(ReviewService reviewService, UserService userService, GameService gameService, ModelFunctionFactory factory) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.gameService = gameService;
        this.factory = factory;
    }

    public void init() throws IOException {
        games = gameService.findAll().stream()
                .map(game -> new GameModel(game.getId(), game.getName(), null, null, null))
                .collect(Collectors.toList());

        Optional<Review> review = reviewService.find(reviewId);
        if (review.isPresent()) {
            this.review = factory.reviewToEditModel().apply(review.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "Character not found");
        }
    }

    public String saveAction() {
        reviewService.update(factory.updateReview().apply(reviewService.find(reviewId).orElseThrow(), review));
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return "/game/game_view.xhtml?faces-redirect=true&id=" + review.getGameId();
    }

}