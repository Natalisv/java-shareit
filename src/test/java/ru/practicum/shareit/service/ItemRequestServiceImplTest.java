package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
class ItemRequestServiceImplTest {

    protected ItemRequestDto itemRequestDto;

    protected User user;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRequestServiceImpl itemRequestService;

    @Autowired
    ItemRequestRepository itemRequestRepository;

    @BeforeEach
    void create() {
        itemRequestDto = new ItemRequestDto(
                "Хотел бы воспользоваться щёткой для обуви"
        );

        user = new User(
                "user",
                "user@user.com"
        );
    }

    @Test
    void addRequest() {
        User savedUser = userRepository.save(user);
        ItemRequestDto result = itemRequestService.addRequest(savedUser.getId(), itemRequestDto);
        assertNotNull(result);
        assertEquals(result.getRequestorId(), savedUser.getId());
        assertEquals(result.getDescription(), "Хотел бы воспользоваться щёткой для обуви");
    }

    @Test
    void addRequestEx() {
        ValidationException exception = assertThrows(ValidationException.class, () -> itemRequestService.addRequest(10L, new ItemRequestDto()));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> itemRequestService.addRequest(10L, itemRequestDto));
    }

    @Test
    void getRequests() {
        User savedUser = userRepository.save(user);
        itemRequestService.addRequest(savedUser.getId(), itemRequestDto);
        List<ItemRequestDto> result = itemRequestService.getRequests(savedUser.getId());
        assertNotNull(result);
        assertEquals(result.size(), 1);
    }

    @Test
    void getRequestsEx() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> itemRequestService.getRequests(10L));
    }

    @Test
    void getRequestById() {
        User savedUser = userRepository.save(user);
        ItemRequestDto savedRequest = itemRequestService.addRequest(savedUser.getId(), itemRequestDto);
        ItemRequestDto result = itemRequestService.getRequestById(savedUser.getId(), savedRequest.getId());
        assertNotNull(result);
        assertEquals(result.getRequestorId(), savedRequest.getRequestorId());
    }

    @Test
    void getRequestByIdEx() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> itemRequestService.getRequestById(1L, 2L));
    }

    @Test
    void getOtherRequests() {
        User savedUser = userRepository.save(user);
        User otherUser = new User(
                "name",
                "user@name.com"
        );
        otherUser = userRepository.save(otherUser);
        ItemRequestDto savedRequest = itemRequestService.addRequest(otherUser.getId(), itemRequestDto);
        List<ItemRequestDto> result = itemRequestService.getOtherRequests(savedUser.getId(), 0, 20);
        assertNotNull(result);
        assertEquals(result.size(), 1);
    }

    @Test
    void getOtherRequestsEx() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> itemRequestService.getOtherRequests(1L, 2, 2));
        User savedUser = userRepository.save(user);
        ValidationException ex = assertThrows(ValidationException.class, () -> itemRequestService.getOtherRequests(savedUser.getId(), 0, 0));
        List<ItemRequestDto> result = itemRequestService.getOtherRequests(savedUser.getId(), null, null);

    }
}
