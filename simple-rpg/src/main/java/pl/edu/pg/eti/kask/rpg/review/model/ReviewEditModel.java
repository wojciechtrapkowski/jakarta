package pl.edu.pg.eti.kask.rpg.review.model;

import jakarta.servlet.http.Part;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.user.entity.User;

import java.time.LocalDate;
import java.util.UUID;

/**
 * JSF view model class in order to not use entity classes. Represents single character to be edited. Includes
 * only fields which can be edited after character creation.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class ReviewEditModel {
    private String description;
    private double mark;
    private Game game;
    private User user;
}
