package pl.edu.pg.eti.kask.rpg.game.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.edu.pg.eti.kask.rpg.review.model.ReviewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class GameModel {
    private UUID id;
    private String name;
    private LocalDate dateOfRelease;
    private String type; // We'll convert GameType enum to String for UI

    @Builder.Default
    private List<ReviewModel> reviews = new ArrayList<>();
}