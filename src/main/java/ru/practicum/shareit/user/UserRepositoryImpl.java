package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class UserRepositoryImpl implements UserRepository {

    Map<Long, User> users = new HashMap<>();

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public User saveUser(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    @Override
    public boolean isContains(User user) {
        return users.containsKey(user.id) || users.containsValue(user) ||
                users.values().stream()
                        .anyMatch(u -> u.getName().equals(user.getName()) || u.getEmail().equals(user.getEmail()));
    }
}
