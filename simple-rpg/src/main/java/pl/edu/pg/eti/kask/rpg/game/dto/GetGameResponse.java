package pl.edu.pg.eti.kask.rpg.game.dto;

import lombok.*;
import pl.edu.pg.eti.kask.rpg.game.entity.GameType;
import pl.edu.pg.eti.kask.rpg.review.entity.Review;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetGameResponse {
    UUID id;
    String name;
    LocalDate dateOfRelease;
    GameType type;
    List<UUID> reviews;
}
