package pl.edu.pg.eti.kask.rpg.user.dto.function;

import pl.edu.pg.eti.kask.rpg.user.dto.UpdateUserAvatarResponse;

import java.util.function.Function;

public class UpdateUserAvatarFunction implements Function<Boolean, UpdateUserAvatarResponse> {
    @Override
    public UpdateUserAvatarResponse apply(Boolean success) {
        return UpdateUserAvatarResponse.builder().success(success).build();
    }
}
