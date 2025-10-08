package pl.edu.pg.eti.kask.rpg.user.repository.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public interface UserAvatarRepository {
    void saveAvatar(UUID userId, InputStream data) throws IOException;
    byte[] getAvatar(UUID userId) throws IOException;
    void deleteAvatar(UUID userId) throws IOException;
}
