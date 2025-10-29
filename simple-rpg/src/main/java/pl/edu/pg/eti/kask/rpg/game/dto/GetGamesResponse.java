package pl.edu.pg.eti.kask.rpg.game.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetGamesResponse {

    @Singular
    List<GetGameResponse> games;
}
