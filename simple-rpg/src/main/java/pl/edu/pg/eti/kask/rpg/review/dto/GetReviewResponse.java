package pl.edu.pg.eti.kask.rpg.review.dto;

import lombok.*;
import pl.edu.pg.eti.kask.rpg.game.entity.GameType;

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
public class GetReviewResponse {
    private UUID id;
    private String description;
    private double mark;
    private UUID gameId;
    private UUID userId;
    private LocalDate dateOfCreation;
}