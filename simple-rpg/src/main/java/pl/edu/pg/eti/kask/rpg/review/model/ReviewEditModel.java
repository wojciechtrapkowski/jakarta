package pl.edu.pg.eti.kask.rpg.review.model;

import jakarta.servlet.http.Part;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
import pl.edu.pg.eti.kask.rpg.validation.ValidMark;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @NotBlank(message = "{validation.description.notblank}")
    @Size(min = 1, max = 50, message = "{validation.description.size}")
    private String description;

    @ValidMark
    private Double mark;

    private Long version;

    @NotNull(message = "{validation.game.notnull}")
    private Game game;

    private User user;
    private LocalDateTime dateOfCreation;
}
