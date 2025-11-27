package pl.edu.pg.eti.kask.rpg.review.view;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.OptimisticLockException;
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


    private ReviewService reviewService;

    private UserService userService;

    private GameService gameService;

    @EJB
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @EJB
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @EJB
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

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

    /**
     * Flag indicating whether a version conflict occurred.
     */
    @Getter
    private boolean versionConflict = false;

    /**
     * Current data from the database (shown when conflict occurs).
     */
    @Getter
    private ReviewEditModel currentDatabaseReview;

    /**
     * User's submitted data that failed to save (shown when conflict occurs).
     */
    @Getter
    private ReviewEditModel userSubmittedReview;

    @Inject
    public ReviewEditView(ModelFunctionFactory factory) {
        this.factory = factory;
    }

    public void init() throws IOException {
        games = gameService.findAll().stream()
                .map(game -> new GameModel(game.getId(), game.getName(), null, null, null))
                .collect(Collectors.toList());

        Optional<Review> review = reviewService.find(reviewId);
        if (review.isPresent()) {
            String currentUsername = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
            boolean isAdmin = FacesContext.getCurrentInstance().getExternalContext().isUserInRole("admin");
            String reviewOwnerLogin = review.get().getUser().getLogin();
            
            // Check if user is owner or admin
            if (!isAdmin && !reviewOwnerLogin.equals(currentUsername)) {
                FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return;
            }
            
            this.review = factory.reviewToEditModel().apply(review.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "Character not found");
        }
    }

    public String saveAction() {
        try {
            reviewService.update(factory.updateReview().apply(reviewService.find(reviewId).orElseThrow(), review));
            return "/game/game_view.xhtml?faces-redirect=true&id=" + review.getGame().getId();
        } catch (EJBException e) {
            if (isOptimisticLockException(e)) {
                handleVersionConflict();
                return null; // Stay on the same page
            }
            throw e;
        } catch (OptimisticLockException e) {
            handleVersionConflict();
            return null; // Stay on the same page
        }
    }

    /**
     * Check if the exception chain contains an OptimisticLockException.
     */
    private boolean isOptimisticLockException(Throwable e) {
        Throwable current = e;
        while (current != null) {
            if (current instanceof OptimisticLockException) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    /**
     * Handle version conflict by storing both the user's submitted data and the current database state.
     */
    private void handleVersionConflict() {
        versionConflict = true;
        
        // Store the user's submitted data
        userSubmittedReview = ReviewEditModel.builder()
                .description(review.getDescription())
                .mark(review.getMark())
                .version(review.getVersion())
                .game(review.getGame())
                .user(review.getUser())
                .build();
        
        // Fetch the current state from the database
        Optional<Review> currentReview = reviewService.find(reviewId);
        if (currentReview.isPresent()) {
            currentDatabaseReview = factory.reviewToEditModel().apply(currentReview.get());
            // Update the review model with current database state for retry
            this.review = factory.reviewToEditModel().apply(currentReview.get());
        }
        
        // Add a faces message to inform the user using internationalized messages
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("messages", 
                FacesContext.getCurrentInstance().getViewRoot().getLocale());
        String title = bundle.getString("review.edit.conflict.title");
        String message = bundle.getString("review.edit.conflict.message");
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, title, message));
    }

    /**
     * Action to retry with current database data (user accepts the database version).
     */
    public String retryWithCurrentData() {
        versionConflict = false;
        userSubmittedReview = null;
        currentDatabaseReview = null;
        return null; // Stay on page with current data
    }

    /**
     * Action to cancel and go back to game view.
     */
    public String cancelAction() {
        return "/game/game_view.xhtml?faces-redirect=true&id=" + review.getGame().getId();
    }

}