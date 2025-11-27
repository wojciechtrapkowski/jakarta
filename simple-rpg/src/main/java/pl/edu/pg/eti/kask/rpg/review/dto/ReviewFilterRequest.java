package pl.edu.pg.eti.kask.rpg.review.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for filtering reviews. All fields are optional.
 * When a field is null, it is not included in the filter.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class ReviewFilterRequest {
    private String description;
    private Double minMark;
    private Double maxMark;
    private UUID userId;
    private Long version;
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;
    private LocalDateTime modifiedAfter;
    private LocalDateTime modifiedBefore;
}
