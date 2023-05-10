package ru.practicum.shareit.user.service;


import ru.practicum.shareit.exception.ExistException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUserById(Long id);

    UserDto saveUser(UserDto user) throws ExistException;

    UserDto updateUser(Long id, UserDto user);

    List<UserDto> getAllUsers();

    void deleteUser(Long id);


}
