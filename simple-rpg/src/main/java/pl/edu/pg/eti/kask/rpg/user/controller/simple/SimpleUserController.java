package pl.edu.pg.eti.kask.rpg.user.controller.simple;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import pl.edu.pg.eti.kask.rpg.component.DtoFunctionFactory;
import pl.edu.pg.eti.kask.rpg.controller.servlet.exception.NotFoundException;
import pl.edu.pg.eti.kask.rpg.user.controller.api.UserController;
import pl.edu.pg.eti.kask.rpg.user.dto.*;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.user.service.UserService;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@RequestScoped
public class SimpleUserController implements UserController {

    private final UserService userService;

    /**
     * Factory producing functions for conversion between DTO and entities.
     */
    private final DtoFunctionFactory factory;

    @Inject
    public SimpleUserController(UserService userService, DtoFunctionFactory factory) {
        this.userService = userService;
        this.factory = factory;
    }

    @Override
    public GetUsersResponse getUsers() {
        return factory.usersToResponse().apply(userService.findAll());
    }

    @Override
    public GetUserResponse getUser(UUID uuid) {
        return userService.find(uuid)
                .map(factory.userToResponse())
                .orElseThrow(NotFoundException::new);

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