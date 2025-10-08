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
import java.util.UUID;

/**
 * PUT user request. Contains only fields that can be set during user creation. User is defined in
 * {@link pl.edu.pg.eti.kask.rpg.user.entity.User}.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PutUserRequest {

    private String login;

    private Gender gender;

    private String name;

    private String surname;

    private String password;

    private String email;
}
