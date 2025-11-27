package pl.edu.pg.eti.kask.rpg.review.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.edu.pg.eti.kask.rpg.game.entity.Game;
import pl.edu.pg.eti.kask.rpg.user.entity.User;
import pl.edu.pg.eti.kask.rpg.validation.ValidMark;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class ReviewCreateModel {
    @NotBlank(message = "{validation.description.notblank}")
    @Size(min = 0, max = 50, message = "{validation.description.size}")
    private String description;

    @ValidMark
    private Double mark;

    @NotNull(message = "{validation.game.notnull}")
    private Game game;

    private User user;
    private LocalDateTime dateOfCreation;
}
