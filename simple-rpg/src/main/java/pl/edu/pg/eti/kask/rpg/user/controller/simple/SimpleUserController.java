package pl.edu.pg.eti.kask.rpg.user.controller.simple;

import pl.edu.pg.eti.kask.rpg.component.DtoFunctionFactory;
import pl.edu.pg.eti.kask.rpg.controller.servlet.exception.NotFoundException;
import pl.edu.pg.eti.kask.rpg.user.controller.api.UserController;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.user.service.UserService;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

public class SimpleUserController implements UserController {

    private final UserService userService;

    /**
     * Factory producing functions for conversion between DTO and entities.
     */
    private final DtoFunctionFactory factory;

    public SimpleUserController(UserService userService, DtoFunctionFactory factory) {
        this.userService = userService;
        this.factory = factory;
    }

    @Override
    public List<User> getUsers() {
        return userService.findAll();
    }

    @Override
    public Optional<User> getUser(UUID uuid) {
        return userService.find(uuid);
    }

    @Override
    public void putUserAvatar(UUID id, InputStream portrait) {
        userService.find(id).ifPresentOrElse(
                entity -> userService.updateAvatar(id, portrait),
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public byte[] getUserAvatar(UUID id) {
        return userService.find(id)
                .map(User::getAvatar)
                .orElseThrow(NotFoundException::new);
    }


    @Override
    public void deleteUserAvatar(UUID id) {
        userService.find(id).ifPresentOrElse(
                        entity -> userService.deleteAvatar(id),
                        () -> {
                            throw new NotFoundException();
        });
    }

}