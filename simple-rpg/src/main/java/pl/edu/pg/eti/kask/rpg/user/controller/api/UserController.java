package pl.edu.pg.eti.kask.rpg.user.controller.api;

import pl.edu.pg.eti.kask.rpg.user.dto.*;
import pl.edu.pg.eti.kask.rpg.user.entity.User;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserController {
    GetUsersResponse getUsers();
    GetUserResponse getUser(UUID uuid);

    UpdateUserAvatarResponse putUserAvatar(UUID id, InputStream avatar);

    byte[] getUserAvatar(UUID id);

    DeleteUserAvatarResponse deleteUserAvatar(UUID uuid);
}