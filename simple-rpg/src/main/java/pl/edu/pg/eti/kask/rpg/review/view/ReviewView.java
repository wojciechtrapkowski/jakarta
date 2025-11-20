package pl.edu.pg.eti.kask.rpg.review.view;

import jakarta.ejb.EJB;
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
public class ReviewView implements Serializable {
    private ReviewService reviewService;
    private UserService userService;

    @EJB
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @EJB
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private final ModelFunctionFactory factory;

    @Setter
    @Getter
    private UUID id;

    @Getter
    private ReviewModel review;


    @Inject
    public ReviewView(ModelFunctionFactory factory) {
        this.factory = factory;
    }

    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached within
     * field and initialized during init of the view.
     */
    public void init() throws IOException {
        Optional<Review> review = reviewService.find(id);
        if (review.isPresent()) {
            String currentUsername = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
            boolean isAdmin = FacesContext.getCurrentInstance().getExternalContext().isUserInRole("admin");
            String reviewOwnerLogin = review.get().getUser().getLogin();
            
            // Check if user is owner or admin
            if (!isAdmin && !reviewOwnerLogin.equals(currentUsername)) {
                FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return;
            }
            
            this.review = factory.reviewToModel().apply(review.get());
            String username = userService.find(review.get().getUser().getId())
                    .map(User::getName)
                    .orElse("Unknown user");
            this.review.setUserName(username);
        } else {
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "Review not found");
        }
    }
}
