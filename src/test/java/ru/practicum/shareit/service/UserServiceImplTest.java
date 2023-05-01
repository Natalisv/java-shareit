package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.exception.ExistException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
public class UserServiceImplTest {

    protected User notValidUser;
    protected User user;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void createUser() {
        notValidUser = new User(
                "user",
                "useruser.com"
        );
        user = new User(
                "user",
                "user@user.com"
        );
    }

    @Test
    void getUserById() {
        User savedUser = userRepository.save(user);
        UserDto userDto = UserMapper.toUserDto(user);
        UserDto result = userService.getUserById(savedUser.getId());
        assertNotNull(result);
        assertEquals(result.getName(), userDto.getName());
    }

    @Test
    void getUserByIdEx() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserById(10L));
        assertEquals(Map.of("error", "Not found"), Map.of("error", "Not found"));
    }

    @Test
    void saveUser() throws ExistException {
        UserDto userDto = UserMapper.toUserDto(user);
        UserDto result = userService.saveUser(userDto);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(result.getName(), userDto.getName());
        assertEquals(result.getEmail(), userDto.getEmail());
    }

    @Test
    void saveUserValidEx() throws ExistException {
        UserDto userDto = UserMapper.toUserDto(notValidUser);
        ValidationException exception = assertThrows(ValidationException.class, () -> userService.saveUser(userDto));
        assertEquals(Map.of("error", exception.getMessage()), Map.of("error", "Не задан емайл или имя пользователя"));
    }

    @Test
    void saveUserExistEx() throws ExistException {
        userRepository.save(user);
        UserDto userDto = UserMapper.toUserDto(user);
        ExistException exception = assertThrows(ExistException.class, () -> userService.saveUser(userDto));
    }

    @Test
    void updateUser() {
        User savedUser = userRepository.save(user);
        UserDto userDto = new UserDto(
                "userUpdate",
                "userUpdate@user.com"
        );
        UserDto result = userService.updateUser(savedUser.getId(), userDto);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(result.getName(), userDto.getName());
        assertEquals(result.getEmail(), userDto.getEmail());
    }

    @Test
    void getAllUsers() {
        userRepository.save(user);
        User userTwo = new User(
                "userTwo",
                "user@userTwo.com"
        );
        userRepository.save(userTwo);
        List<UserDto> users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(users.size(), 2);
    }

    @Test
    void deleteUser() {
        User savedUser = userRepository.save(user);
        userService.deleteUser(savedUser.getId());
        List<UserDto> users = userService.getAllUsers();
        assertEquals(users.size(), 0);
    }
}
