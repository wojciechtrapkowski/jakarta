package pl.edu.pg.eti.kask.rpg.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class UpdateUserAvatarRequest {
    private byte[] avatar;
}
