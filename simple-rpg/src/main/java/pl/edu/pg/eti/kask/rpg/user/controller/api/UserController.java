package pl.edu.pg.eti.kask.rpg.user.controller.api;

import pl.edu.pg.eti.kask.rpg.user.entity.User;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserController {
    List<User> getUsers();
    Optional<User> getUser(UUID uuid);

    void putUserAvatar(UUID id, InputStream portrait);

    byte[] getUserAvatar(UUID id);

    void deleteUserAvatar(UUID uuid);
}