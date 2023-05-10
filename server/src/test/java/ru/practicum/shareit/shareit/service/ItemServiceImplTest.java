package ru.practicum.shareit.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.ExistException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
class ItemServiceImplTest {

    protected ItemDto itemDto;
    protected ItemDto itemDtoNotValid;

    protected User user;
    protected Comment comment;
    protected Booking booking;
    protected User userTwo;

    LocalDateTime start = LocalDateTime.of(2024, 04, 27, 10, 00);
    LocalDateTime end = LocalDateTime.of(2024, 04, 28, 10, 00);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    ItemServiceImpl itemService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookingServiceImpl bookingService;


    @BeforeEach
    void createItem() {
        itemDto = new ItemDto(
                "Дрель",
                "Простая дрель",
                true
        );

        itemDtoNotValid = new ItemDto(
                "Простая дрель",
                true
        );

        user = new User(
                "user",
                "user@user.com"
        );

        comment = new Comment(
                "comment"
        );
        booking = new Booking(
                start,
                end,
                3L,
                5L,
                Status.APPROVED
        );
        userTwo = new User(
                10L,
                "userTwo",
                "user@.com"
        );
        userTwo = userRepository.save(userTwo);
    }

    @Test
    void addItem() {
        User savedUser = userRepository.save(user);
        ItemDto result = itemService.addItem(savedUser.getId(), itemDto);
        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getOwner());
        assertEquals("Дрель", result.getName());
    }

    @Test
    void addItemValidEx() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> itemService.addItem(1L, itemDtoNotValid));
    }

    @Test
    void addItemIlArEx() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> itemService.addItem(10L, itemDto));
    }

    @Test
    void getById() {
        User savedUser = userRepository.save(user);
        ItemDto savedItem = itemService.addItem(savedUser.getId(), itemDto);
        ItemDto result = itemService.getById(savedUser.getId(), savedItem.getId());
        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getOwner());
        assertEquals(Boolean.TRUE, result.getAvailable());
    }

    @Test
    void getByIdIlArEx() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> itemService.getById(10L, 1L));
        User savedUser = userRepository.save(user);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> itemService.getById(savedUser.getId(), 10L));
    }

    @Test
    void updateItem() throws ExistException {
        User savedUser = userRepository.save(user);
        ItemDto savedItem = itemService.addItem(savedUser.getId(), itemDto);
        ItemDto newItem = new ItemDto(
                "updatedName",
                "updatedDesc",
                false
        );
        ItemDto result = itemService.updateItem(savedUser.getId(), savedItem.getId(), newItem);
        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getOwner());
        assertEquals(Boolean.FALSE, result.getAvailable());
        assertEquals(newItem.getName(), result.getName());
    }

    @Test
    void updateItemEx() throws ExistException {
        ExistException exception = assertThrows(ExistException.class,
                () -> itemService.updateItem(10L, 1L, itemDto));
    }

    @Test
    void getUserItems() {
        User savedUser = userRepository.save(user);
        itemService.addItem(savedUser.getId(), itemDto);
        List<ItemDto> result = itemService.getUserItems(savedUser.getId());
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findItem() {
        User savedUser = userRepository.save(user);
        itemService.addItem(savedUser.getId(), itemDto);
        String text = "дрель";
        List<ItemDto> result = itemService.findItem(1L, text);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(itemDto.getName(), result.get(0).getName());
    }

    @Test
    void findItemEmpt() {
        List<ItemDto> result = itemService.findItem(1L, "");
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void addComment() {
        User savedUser = userRepository.save(user);
        ItemDto savedItem = itemService.addItem(userTwo.getId(), itemDto);
        booking.setItemId(savedItem.getId());
        booking.setBookerId(userTwo.getId());
        bookingService.createBooking(savedUser.getId(), booking);
        booking.setStatus(Status.APPROVED);
        booking.setStart(LocalDateTime.of(2020, 04, 27, 10, 00));
        booking.setEnd(LocalDateTime.of(2021, 04, 27, 10, 00));
        Comment result = itemService.addComment(savedUser.getId(), savedItem.getId(), comment);
        assertNotNull(result);
        assertEquals("comment", result.getText());
    }

    @Test
    void addCommentEx() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> itemService.addComment(10L, 2L, new Comment()));
        comment.setText("");
        User savedUser = userRepository.save(user);
        ItemDto savedItem = itemService.addItem(userTwo.getId(), itemDto);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> itemService.addComment(savedUser.getId(), savedItem.getId(), comment));

        comment.setText("text");
        ValidationException exception2 = assertThrows(ValidationException.class,
                () -> itemService.addComment(savedUser.getId(), savedItem.getId(), comment));
    }

}
