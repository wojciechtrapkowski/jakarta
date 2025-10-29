package pl.edu.pg.eti.kask.rpg.review.dto;
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
public class PatchReviewRequest {
    private String description;
    private Double mark; // Use Double to allow null for partial updates
}