package pl.edu.pg.eti.kask.rpg.review.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.user.entity.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = {"game", "user"})
@EqualsAndHashCode(exclude = {"game", "user"})
public class Review implements Serializable {
    private UUID id;

    private String description;
    private LocalDate dateOfCreation;
    private double mark;

    private UUID gameId;
    private UUID userId;
}
