package pl.edu.pg.eti.kask.rpg.user.dto.function;

import pl.edu.pg.eti.kask.rpg.user.dto.PatchUserRequest;
import pl.edu.pg.eti.kask.rpg.user.entity.User;

import java.util.function.BiFunction;

public class PatchUserRequestFunction implements BiFunction<User, PatchUserRequest, User> {

    @Override
    public User apply(User entity, PatchUserRequest request) {
        return User.builder()
                .id(entity.getId())
                .login(request.getLogin() != null ? request.getLogin() : entity.getLogin())
                .name(request.getName() != null ? request.getName() : entity.getName())
                .surname(request.getSurname() != null ? request.getSurname() : entity.getSurname())
                .email(request.getEmail() != null ? request.getEmail() : entity.getEmail())
                .password(request.getPassword() != null ? request.getPassword() : entity.getPassword())
                .gender(request.getGender() != null ? request.getGender() : entity.getGender())
                .accountCreatedAt(entity.getAccountCreatedAt())
                .reviews(entity.getReviews())
                .build();
    }
}