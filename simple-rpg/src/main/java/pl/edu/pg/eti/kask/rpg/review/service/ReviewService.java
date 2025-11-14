package pl.edu.pg.eti.kask.rpg.review.service;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.transaction.Transactional;
import jdk.jshell.spi.ExecutionControl;
import lombok.NoArgsConstructor;
import pl.edu.pg.eti.kask.rpg.controller.servlet.exception.NotFoundException;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;
import pl.edu.pg.eti.kask.rpg.review.repository.api.ReviewRepository;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.user.entity.UserRoles;
import pl.edu.pg.eti.kask.rpg.user.repository.api.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@LocalBean
@Stateless
@NoArgsConstructor(force = true)
public class ReviewService {
    private final UserRepository  userRepository;
    private final ReviewRepository reviewRepository;

    private final SecurityContext securityContext;

    @Inject
    public ReviewService(UserRepository userRepository, ReviewRepository reviewRepository,
                         @SuppressWarnings("CdiInjectionPointsInspection") SecurityContext securityContext
    ) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.securityContext = securityContext;
    }


    @RolesAllowed(UserRoles.USER)
    public List<Review> findAll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    @RolesAllowed(UserRoles.USER)
    public List<Review> findAllForGame(UUID gameId) {
        if (securityContext.isCallerInRole(UserRoles.ADMIN)) {
            return reviewRepository.findAllForGame(gameId);
        }

        User user = userRepository.findByLogin(securityContext.getCallerPrincipal().getName()).orElseThrow();

        System.out.println(user.getLogin());

        return reviewRepository.findAllForUserAndGame(user.getId(), gameId);
    }


    @RolesAllowed({UserRoles.USER})
    public void create(Review review) {
        User user = userRepository.findByLogin(securityContext.getCallerPrincipal().getName())
                .orElseThrow(IllegalStateException::new);
        review.setUser(user);
        reviewRepository.create(review);
    }


    @RolesAllowed(UserRoles.USER)
    public Optional<Review> find(UUID id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Optional<Review> findForGame(UUID gameId, UUID reviewId) {
        try {
            if (securityContext.isCallerInRole(UserRoles.ADMIN)) {
                return reviewRepository.findForGame(reviewId, gameId);
            }

            User user = userRepository.findByLogin(securityContext.getCallerPrincipal().getName()).orElseThrow();
            return reviewRepository.findForUserAndGame(reviewId, user.getId(), gameId);
        } catch (Exception e) {
            throw new NotFoundException(e.getMessage());
        }

    }

    public void update(Review review) {
        checkAdminRoleOrOwner(reviewRepository.find(review.getId()));
        reviewRepository.update(review);
    }

    public void delete(Review review) {
        checkAdminRoleOrOwner(reviewRepository.find(review.getId()));
        reviewRepository.delete(review);
    }

    private void checkAdminRoleOrOwner(Optional<Review> review) throws EJBAccessException {
        if (securityContext.isCallerInRole(UserRoles.ADMIN)) {
            return;
        }
        if (securityContext.isCallerInRole(UserRoles.USER)
                && review.isPresent()
                && review.get().getUser().getLogin().equals(securityContext.getCallerPrincipal().getName())) {
            return;
        }
        throw new EJBAccessException("Caller not authorized.");
    }

}
