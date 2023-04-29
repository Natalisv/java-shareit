package ru.practicum.shareit.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.exception.ExistException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
public class UserRepositoryTest {

    protected User user;
    @Autowired
    private TestEntityManager em;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void createUser() {
        user = new User(
                "user",
                "user@user.com"
        );
    }

    @Test
    void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    void updateUser() throws ExistException {
        UserDto userDto = new UserDto(
                "user",
                "user@user.com"
        );

        User savedUser = userRepository.save(user);
        userRepository.updateUserName("newName", savedUser.getId());
        userRepository.updateUserEmail("new@user.com", savedUser.getId());
        User result = userRepository.findById(savedUser.getId()).get();
        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo("newName"));
        assertThat(result.getEmail(), equalTo("new@user.com"));
    }

}
