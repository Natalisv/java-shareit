package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    User getUserById(Long id);

    User saveUser(User user);

    List<User> getUsers();

    void deleteUser(Long id);

    boolean isContains(User user);
}
