package pl.edu.pg.eti.kask.rpg.user.controller.simple;

import jakarta.ejb.EJBException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.java.Log;
import pl.edu.pg.eti.kask.rpg.component.DtoFunctionFactory;
import pl.edu.pg.eti.kask.rpg.controller.interceptor.LogOperation;
import pl.edu.pg.eti.kask.rpg.controller.interceptor.OperationLoggingInterceptor;
import pl.edu.pg.eti.kask.rpg.controller.servlet.exception.NotFoundException;
import pl.edu.pg.eti.kask.rpg.review.controllers.api.ReviewController;
import pl.edu.pg.eti.kask.rpg.review.service.ReviewService;
import pl.edu.pg.eti.kask.rpg.user.controller.api.UserController;
import pl.edu.pg.eti.kask.rpg.user.dto.*;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.user.service.UserService;

import java.io.InputStream;
import java.util.UUID;
import java.util.logging.Level;

@Path("")
@Interceptors(OperationLoggingInterceptor.class)
@Log
public class UserRestController implements UserController {

    private final UserService userService;

    private final DtoFunctionFactory factory;

    private final UriInfo uriInfo;

    @Context
    private HttpServletResponse response;


    @Inject
    public UserRestController(UserService userService, DtoFunctionFactory factory, @SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo) {
        this.userService = userService;
        this.factory = factory;
        this.uriInfo = uriInfo;
    }

    @Override
    public GetUsersResponse getUsers() {
        try {
            return factory.usersToResponse().apply(userService.findAll());
        } catch (EJBException ex) {
            log.log(Level.WARNING, ex.getMessage(), ex);
            throw new ForbiddenException(ex.getMessage());
        }
    }

    @Override
    public GetUserResponse getUser(UUID uuid) {
        try {
            return userService.find(uuid)
                    .map(factory.userToResponse())
                    .orElseThrow(NotFoundException::new);
        } catch (NotFoundException e) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }


    @Override
    @LogOperation("CREATE")
    public void createUser(UUID uuid, PutUserRequest request) {
        try {
            if (userService.find(uuid).isPresent()) {
                throw new WebApplicationException(Response.Status.CONFLICT);
            }

            userService.create(factory.putUserRequest().apply(uuid, request));

            String location = uriInfo.getBaseUriBuilder()
                    .path(UserController.class, "getUser")
                    .build(uuid)
                    .toString();

            response.setHeader("Location", location);

            throw new WebApplicationException(Response.Status.CREATED);
        }  catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex);
        }
    }

    @Override
    @LogOperation("UPDATE")
    public void updateUser(UUID uuid, PatchUserRequest request) {
        try {
            if (userService.find(uuid).isEmpty()) {
                throw new WebApplicationException(Response.Status.CONFLICT);
            }

            User existing = userService.find(uuid).orElseThrow(NotFoundException::new);

            User updated = factory.patchUserRequest().apply(existing, request);

            userService.update(updated);

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }catch (IllegalArgumentException ex) {
            throw new BadRequestException(ex);
        }
    }
    @Override
    @LogOperation("DELETE")
    public void deleteUser(UUID uuid) {
        userService.find(uuid).ifPresentOrElse(userService::delete, () -> { throw new NotFoundException(); });
    }

    @Override
    public UpdateUserAvatarResponse putUserAvatar(UUID id, InputStream avatar) {
        boolean updated = userService.find(id)
                .map(user -> {
                    userService.updateAvatar(id, avatar);
                    return true;
                })
                .orElse(false);

        return factory.updateUserAvatar().apply(updated);
    }

    @Override
    public byte[] getUserAvatar(UUID id) {
        return userService.getAvatar(id);
    }


    @Override
    public DeleteUserAvatarResponse deleteUserAvatar(UUID id) {
        boolean deleted = userService.find(id)
                .map(user -> {
                    userService.deleteAvatar(id);
                    return true;
                })
                .orElse(false);

        return factory.deleteUserAvatar().apply(deleted);
    }

}