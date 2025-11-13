package pl.edu.pg.eti.kask.rpg.user.controller.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pl.edu.pg.eti.kask.rpg.user.dto.*;
import pl.edu.pg.eti.kask.rpg.user.entity.User;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("")
public interface UserController {
    @GET
    @Path("/users/")
    @Produces(MediaType.APPLICATION_JSON)
    GetUsersResponse getUsers();

    @GET
    @Path("/users/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    GetUserResponse getUser(@PathParam("userId") UUID uuid);

    @PUT
    @Path("/users/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    void createUser(@PathParam("userId") UUID uuid, PutUserRequest request);

    @PATCH
    @Path("/users/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateUser(@PathParam("userId") UUID uuid, PatchUserRequest request);

    @DELETE
    @Path("/users/{userId}")
    void deleteUser(@PathParam("userId") UUID uuid);

    UpdateUserAvatarResponse putUserAvatar(UUID id, InputStream avatar);

    byte[] getUserAvatar(UUID id);

    DeleteUserAvatarResponse deleteUserAvatar(UUID uuid);
}