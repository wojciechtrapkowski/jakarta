package pl.edu.pg.eti.kask.rpg.user.controller.api;

import pl.edu.pg.eti.kask.rpg.user.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserController {
    List<User> getUsers();
    Optional<User> getUser(UUID uuid);
}