package pl.edu.pg.eti.kask.rpg.user.repository.storage;

import pl.edu.pg.eti.kask.rpg.user.repository.api.UserAvatarRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;

public class UserAvatarInStorageRepository implements UserAvatarRepository {
    private final Path avatarDir;

    public UserAvatarInStorageRepository(String avatarDirPath) {
        System.out.println(avatarDirPath);

        this.avatarDir = Paths.get(avatarDirPath);
        if (!Files.exists(avatarDir)) {
            try {
                Files.createDirectories(avatarDir);
            } catch (IOException e) {

            }

        }
    }

    @Override
    public void saveAvatar(UUID userId, InputStream data) throws IOException {
        String ext = getExtension("image/png");
        deleteAvatar(userId); // delete previous avatar
        Path path = avatarDir.resolve(userId.toString() + ext);
        Files.copy(data, path, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public byte[] getAvatar(UUID userId) throws IOException {
        Path path = findAvatarPath(userId);
        if (path == null) throw new IOException("Avatar not found");
        return Files.readAllBytes(path);
    }

    @Override
    public void deleteAvatar(UUID userId) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(avatarDir, userId.toString() + ".*")) {
            for (Path entry : stream) {
                Files.deleteIfExists(entry);
            }
        }
    }

    private Path findAvatarPath(UUID userId) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(avatarDir, userId.toString() + ".*")) {
            for (Path entry : stream) {
                return entry;
            }
        }
        return null;
    }

    private String getExtension(String contentType) {
        if ("image/png".equals(contentType)) return ".png";
        if ("image/jpeg".equals(contentType) || "image/jpg".equals(contentType)) return ".jpg";
        return ".dat";
    }
}
