package pl.edu.pg.eti.kask.rpg.review.view;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.eti.kask.rpg.component.ModelFunctionFactory;
import pl.edu.pg.eti.kask.rpg.game.model.GameModel;
import pl.edu.pg.eti.kask.rpg.game.service.GameService;
import pl.edu.pg.eti.kask.rpg.review.model.ReviewCreateModel;
import pl.edu.pg.eti.kask.rpg.review.model.ReviewEditModel;
import pl.edu.pg.eti.kask.rpg.review.service.ReviewService;
import pl.edu.pg.eti.kask.rpg.user.service.UserService;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@ViewScoped
@Named
public class ReviewCreateView implements Serializable {
    @Inject
    private UserService userService;

    @Inject
    private ReviewService reviewService;

    @Inject
    private GameService gameService;

    @Inject
    private ModelFunctionFactory factory;

    @Getter
    private List<GameModel> games; // for dropdown

    @Getter
    @Setter
    private ReviewCreateModel review = new ReviewCreateModel();


    public void init() throws IOException  {
        games = gameService.findAll().stream()
                .map(game -> new GameModel(game.getId(), game.getName(), null, null, null))
                .collect(Collectors.toList());

        review.setGame(gameService.find(games.get(0).getId()).orElseThrow());
    }

    public String saveAction() {
        review.setDateOfCreation(java.time.LocalDate.now());

        review.setUser(userService.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No user found")));

        var reviewEntity = factory.createReview().apply(review);

        var managedGame = gameService.find(reviewEntity.getGame().getId())
                .orElseThrow(() -> new IllegalStateException("Game not found"));

        reviewEntity.setGame(managedGame);

        reviewService.create(reviewEntity);

        managedGame.getReviews().add(reviewEntity);

        // Reset form
        review = new ReviewCreateModel();

        return "/game/game_view.xhtml?faces-redirect=true&id=" + managedGame.getId();
    }
}