package pl.edu.pg.eti.kask.rpg.user.dto.function;

import pl.edu.pg.eti.kask.rpg.user.dto.DeleteUserAvatarResponse;

import java.util.List;
import java.util.function.Function;

public class DeleteUserAvatarFunction implements Function<Boolean, DeleteUserAvatarResponse> {
    @Override
    public DeleteUserAvatarResponse apply(Boolean success) {
        return DeleteUserAvatarResponse.builder().success(success).build();
    }
}
