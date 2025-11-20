package pl.edu.pg.eti.kask.rpg.game.controller.rest;

import jakarta.ejb.EJBException;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.rpg.component.DtoFunctionFactory;
import pl.edu.pg.eti.kask.rpg.controller.interceptor.LogOperation;
import pl.edu.pg.eti.kask.rpg.controller.interceptor.OperationLoggingInterceptor;
import pl.edu.pg.eti.kask.rpg.game.controller.api.GameController;
import pl.edu.pg.eti.kask.rpg.game.dto.GetGameResponse;
import pl.edu.pg.eti.kask.rpg.game.dto.GetGamesResponse;
import pl.edu.pg.eti.kask.rpg.game.dto.PatchGameRequest;
import pl.edu.pg.eti.kask.rpg.game.dto.PutGameRequest;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.game.service.GameService;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

@Path("")
@Interceptors(OperationLoggingInterceptor.class)
@Log
public class GameRestController implements GameController {

    GameService service;
    private final DtoFunctionFactory factory;

    /**
     * Allows to create {@link UriBuilder} based on current request.
     */
    private final UriInfo uriInfo;

    /**
     * Current HTTP Servlet response.
     */
    private HttpServletResponse response;

    @Context
    public void setResponse(HttpServletResponse response) {
        //ATM in this implementation only HttpServletRequest can be injected with CDI so JAX-RS injection is used.
        this.response = response;
    }


    @Inject
    public GameRestController(
            GameService service,
            DtoFunctionFactory factory,
            @SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo
    ) {
        this.service = service;
        this.factory = factory;
        this.uriInfo = uriInfo;
    }

    @Override
    @LogOperation("CREATE")
    public void createGame(UUID id, PutGameRequest request) {
        try {
            if (service.find(id).isPresent()) {
                throw new WebApplicationException(Response.Status.CONFLICT);
            }
            service.create(factory.putGameRequest().apply(id, request));

            //This can be done with Response builder but requires method different return type.
            response.setHeader("Location", uriInfo.getBaseUriBuilder()
                    .path(GameController.class, "getGame")
                    .build(id)
                    .toString());

            //This can be done with Response builder but requires method different return type.
            //Calling HttpServletResponse#setStatus(int) is ignored.
            //Calling HttpServletResponse#sendError(int) causes response headers and body looking like error.
            throw new WebApplicationException(Response.Status.CREATED);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex);
        } catch (EJBException ex) {
            log.log(Level.WARNING, ex.getMessage(), ex);
            throw new ForbiddenException(ex.getMessage());
        }
    }


    @Override
    public GetGameResponse getGame(UUID id) {
        try {
            return service.find(id).map(factory.gameToResponse()).orElseThrow(NotFoundException::new);
        }
        catch (EJBException ex) {
            log.log(Level.WARNING, ex.getMessage(), ex);
            throw new ForbiddenException(ex.getMessage());
        }
    }


    @Override
    public GetGamesResponse getGames() {
        try {
            return factory.gamesToResponse().apply(service.findAll());
        }
        catch (EJBException ex) {
            log.log(Level.WARNING, ex.getMessage(), ex);
            throw new ForbiddenException(ex.getMessage());
        }
    }

    @Override
    @LogOperation("UPDATE")
    public void updateGame(UUID id, PatchGameRequest request) {
        try {
            Game existing = service.find(id)
                    .orElseThrow(() -> new NotFoundException("Game not found: " + id));

            Game updated = factory.patchGameRequest().apply(existing, request);

            service.update(updated);

            // Return 204 No Content for successful update
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex);
        } catch (EJBException ex) {
            log.log(Level.WARNING, ex.getMessage(), ex);
            throw new ForbiddenException(ex.getMessage());
        }
    }

    @Override
    @LogOperation("DELETE")
    public void deleteGame(UUID id) {
        try {
            Game existing = service.find(id)
                    .orElseThrow(() -> new NotFoundException("Game not found: " + id));

            final List<Review> reviews = existing.getReviews();

            service.find(id).ifPresentOrElse(
                    entity -> service.delete(entity),
                    () -> {
                        throw new NotFoundException();
                    }
            );
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex);
         }catch (EJBException ex) {
            log.log(Level.WARNING, ex.getMessage(), ex);
            throw new ForbiddenException(ex.getMessage());
        }

    }
}
