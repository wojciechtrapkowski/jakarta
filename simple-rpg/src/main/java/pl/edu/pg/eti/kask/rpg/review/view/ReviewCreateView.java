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

        review.setGameId(games.get(0).getId());
    }

    public String saveAction() {
        review.setDateOfCreation(java.time.LocalDate.now());

        // Use the first user when we don't have authentication yet.
        review.setUserId(userService.findAll().stream().findFirst().get().getId());

        reviewService.create(factory.createReview().apply(review));
        return "/game/game_view.xhtml?faces-redirect=true&id=" + review.getGameId();
    }
}