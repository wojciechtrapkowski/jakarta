package pl.edu.pg.eti.kask.rpg.user.repository.storage;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import pl.edu.pg.eti.kask.rpg.user.repository.api.UserAvatarRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;

@ApplicationScoped
public class UserAvatarInStorageRepository implements UserAvatarRepository {
    private final Path avatarDir;

    @Inject
    public UserAvatarInStorageRepository(@Named("avatarDir") String avatarDir) {
        this.avatarDir = Paths.get(avatarDir);
        if (!Files.exists(this.avatarDir)) {
            try {
                Files.createDirectories(this.avatarDir);
            } catch (IOException e) {
                System.err.println("Cannot create avatar directory: " + e.getMessage());
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
