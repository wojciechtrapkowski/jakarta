package pl.edu.pg.eti.kask.rpg.review.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetReviewsResponse {
    @Singular
    private List<GetReviewResponse> reviews;
}