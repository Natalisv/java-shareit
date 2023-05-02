package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName() != null ? user.getName() : null,
                user.getEmail() != null ? user.getEmail() : null
        );
    }

    public static User toUser(UserDto user) {
        return new User(
                user.getName() != null ? user.getName() : null,
                user.getEmail() != null ? user.getEmail() : null
        );
    }
}
