package pl.edu.pg.eti.kask.rpg.game.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class Game implements Serializable {
    private UUID id;
    private String name;
    private LocalDate dateOfRelease;
    private GameType type;

    @Builder.Default
    private List<UUID> reviews = new ArrayList<>();
}
