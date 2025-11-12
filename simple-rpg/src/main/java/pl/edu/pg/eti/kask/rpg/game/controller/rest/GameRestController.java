package pl.edu.pg.eti.kask.rpg.game.controller.rest;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import pl.edu.pg.eti.kask.rpg.component.DtoFunctionFactory;
import pl.edu.pg.eti.kask.rpg.game.controller.api.GameController;
import pl.edu.pg.eti.kask.rpg.game.dto.GetGameResponse;
import pl.edu.pg.eti.kask.rpg.game.dto.GetGamesResponse;
import pl.edu.pg.eti.kask.rpg.game.dto.PatchGameRequest;
import pl.edu.pg.eti.kask.rpg.game.dto.PutGameRequest;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.game.service.GameService;

import java.util.UUID;

@Path("")
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
        }
    }


    @Override
    public GetGameResponse getGame(UUID id) {
        return service.find(id).map(factory.gameToResponse()).orElseThrow(NotFoundException::new);
    }


    @Override
    public GetGamesResponse getGames() {
        return factory.gamesToResponse().apply(service.findAll());
    }

    @Override
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
        }
    }

    @Override
    public void deleteGame(UUID id) {
        service.find(id).ifPresentOrElse(
                entity -> service.delete(entity),
                () -> {
                    throw new NotFoundException();
                }
        );
    }
}
