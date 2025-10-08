package pl.edu.pg.eti.kask.rpg.user.dto.function;

import lombok.ToString;
import pl.edu.pg.eti.kask.rpg.user.dto.GetUserResponse;
import pl.edu.pg.eti.kask.rpg.user.entity.Gender;
import pl.edu.pg.eti.kask.rpg.user.entity.User;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Function;

/**
 * Converts {@link User} to {@link GetUserResponse}.
 */
public class UserToResponseFunction implements Function<User, GetUserResponse> {

    @Override
    public GetUserResponse apply(User user) {
        return GetUserResponse.builder()
                .id(user.getId())
                .login(user.getLogin())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .gender(user.getGender())
                .build();
    }

}
