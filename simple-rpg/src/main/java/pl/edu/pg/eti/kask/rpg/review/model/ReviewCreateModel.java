package pl.edu.pg.eti.kask.rpg.review.model;


import lombok.*;
import lombok.experimental.SuperBuilder;

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
    private UUID gameId;
    private UUID userId;
    private LocalDate dateOfCreation;
}
