package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ExistException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public UserDto getUserById(Long id) {
        if (id != null) {
            User user = userRepository.findById(id).orElseThrow(() -> {
                throw new IllegalArgumentException();
            });
            return UserMapper.toUserDto(user);
        }
        return null;
    }

    @Transactional
    @Override
    public UserDto saveUser(UserDto userDto) throws ExistException {
        if (userDto.getEmail() != null && userDto.getEmail().contains("@") && userDto.getName() != null) {
            User user = UserMapper.toUser(userDto);
            User savedUser = null;
            try {
                savedUser = userRepository.save(user);
            } catch (Exception e) {
                throw new ExistException("Пользователь с таким email уже существует");
            }
            log.debug("Add user: {}", savedUser);
            return UserMapper.toUserDto(savedUser);
        } else {
            log.error("Не задан емайл или имя пользователя");
            throw new ValidationException("Не задан емайл или имя пользователя");
        }
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id).get();
        if (user != null) {
            if (userDto.getName() != null) {
                userRepository.updateUserName(userDto.getName(), id);
            }
            if (userDto.getEmail() != null && userDto.getEmail().contains("@")) {
                userRepository.updateUserEmail(userDto.getEmail(), id);
            }
            User savedUser = userRepository.findById(id).get();
            log.debug("Update user: {}", savedUser);
            return UserMapper.toUserDto(savedUser);
        }
        return null;
    }

    @Transactional
    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> list = new ArrayList<>();
        userRepository.findAll().forEach(u ->
                list.add(UserMapper.toUserDto(u)));
        return list;
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        if (id != null) {
            userRepository.deleteById(id);
        }
    }

}
