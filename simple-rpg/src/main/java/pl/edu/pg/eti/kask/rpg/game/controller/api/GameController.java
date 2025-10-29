package pl.edu.pg.eti.kask.rpg.game.controller.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pl.edu.pg.eti.kask.rpg.game.dto.GetGameResponse;
import pl.edu.pg.eti.kask.rpg.game.dto.GetGamesResponse;
import pl.edu.pg.eti.kask.rpg.game.dto.PatchGameRequest;
import pl.edu.pg.eti.kask.rpg.game.dto.PutGameRequest;

import java.util.UUID;

@Path("")
public interface GameController {
    @GET
    @Path("/games")
    @Produces(MediaType.APPLICATION_JSON)
    GetGamesResponse getGames();

    @GET
    @Path("/games/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    GetGameResponse getGame(@PathParam("id") UUID id);

    @PUT
    @Path("/games/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void createGame(@PathParam("id") UUID id, PutGameRequest request);

    @PATCH
    @Path("/games/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateGame(@PathParam("id") UUID id, PatchGameRequest request);

    @DELETE
    @Path("/games/{id}")
    void deleteGame(@PathParam("id") UUID id);
}
