package pl.edu.pg.eti.kask.rpg.review.model;


import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.user.entity.User;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class ReviewCreateModel {
    private String description;
    private double mark;
    private Game game;
    private User user;
    private LocalDate dateOfCreation;
}
