package pl.edu.pg.eti.kask.rpg.user.controller.simple;

import pl.edu.pg.eti.kask.rpg.component.DtoFunctionFactory;
import pl.edu.pg.eti.kask.rpg.user.controller.api.UserController;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.user.service.UserService;

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
}