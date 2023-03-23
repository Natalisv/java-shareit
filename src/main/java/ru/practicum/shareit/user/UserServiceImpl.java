package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.ValidationException;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    private static Long startId = 1L;

    @Override
    public UserDto getUserById(Long id) {
        if (id != null) {
            return UserMapper.toUserDto(userRepository.getUserById(id));
        }
        return null;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user.setId(startId);
        if (user.getEmail() != null && user.getEmail().contains("@")) {
            if (!isContains(user)) {
                User savedUser = userRepository.saveUser(user);
                startId++;
                return UserMapper.toUserDto(savedUser);
            } else {
                log.error("Такой пользователь уже существует");
                throw new RuntimeException();
            }
        } else {
            log.error("Невалидный емайл");
            throw new ValidationException();
        }
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = UserMapper.toUser(userDto);

        User userUpdate = userRepository.getUserById(id);
        if (userUpdate != null) {
            if (user.getName() != null) {
                userUpdate.setName(user.getName());
            }
            if (user.getEmail() != null) {
                if(user.getEmail().equals(userUpdate.getEmail()) || userRepository.getUsers().stream()
                        .noneMatch(u -> u.getEmail().equals(user.getEmail()))) {
                    userUpdate.setEmail(user.getEmail());
                } else {
                    log.error("Пользователь с емайлом = " + user.getEmail() + " уже существует");
                    throw new RuntimeException();
                }
            }
            User savedUser = userRepository.saveUser(userUpdate);
            return UserMapper.toUserDto(savedUser);
        }
        return null;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> list = new ArrayList<>();
        userRepository.getUsers().forEach(u ->
                list.add(UserMapper.toUserDto(u)));
        return list;
    }

    @Override
    public void deleteUser(Long id) {
        if (id != null) {
           userRepository.deleteUser(id);
        }
    }

    private boolean isContains(User user) {
        return userRepository.isContains(user);
    }

}
