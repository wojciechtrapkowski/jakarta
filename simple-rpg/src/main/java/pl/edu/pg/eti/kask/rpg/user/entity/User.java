package pl.edu.pg.eti.kask.rpg.user.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity for system user. Represents information about particular user as well as credentials for authorization and
 * authentication needs.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class User implements Serializable {
    private UUID id;

    private String login;

    private Gender gender;

    private String name;

    private String surname;

    private LocalDate accountCreatedAt;

    @ToString.Exclude
    private String password;

    private String email;

    @Builder.Default
    private List<UUID> reviews = new ArrayList<>();
}
