package pl.edu.pg.eti.kask.rpg.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pg.eti.kask.rpg.user.entity.Gender;

import java.time.LocalDate;

/**
 * PATCH user request. Contains only fields which can be changed byt the user while updating its profile. User is
 * defined in {@link pl.edu.pg.eti.kask.rpg.user.entity.User}.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PatchUserRequest {
    private String login;

    private Gender gender;

    private String name;

    private String surname;

    private String email;

    private String password;
}
