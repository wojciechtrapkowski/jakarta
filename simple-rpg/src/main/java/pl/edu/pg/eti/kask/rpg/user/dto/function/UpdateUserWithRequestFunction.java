package pl.edu.pg.eti.kask.rpg.user.dto.function;

import lombok.ToString;
import pl.edu.pg.eti.kask.rpg.user.dto.PatchUserRequest;
import pl.edu.pg.eti.kask.rpg.user.entity.Gender;
import pl.edu.pg.eti.kask.rpg.user.entity.User;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * Returns new instance of {@link User} based on provided value and updated with values from {@link PatchUserRequest}.
 */
public class UpdateUserWithRequestFunction implements BiFunction<User, PatchUserRequest, User> {

    @Override
    public User apply(User entity, PatchUserRequest request) {
        return User.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .gender(request.getGender())
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(entity.getPassword())
                .accountCreatedAt(entity.getAccountCreatedAt())
                .build();
    }

}
