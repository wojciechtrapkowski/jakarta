package pl.edu.pg.eti.kask.rpg.game.dto;

import lombok.*;
import pl.edu.pg.eti.kask.rpg.game.entity.GameType;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PutGameRequest {
    String name;
    LocalDate dateOfRelease;
    GameType type;
}
