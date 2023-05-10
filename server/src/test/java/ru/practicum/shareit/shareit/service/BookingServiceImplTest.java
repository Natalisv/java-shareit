package ru.practicum.shareit.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookerDtoShort;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
class BookingServiceImplTest {

    protected Booking booking;
    protected BookingDto bookingDto;
    protected Item item;
    protected User user;
    protected User userTwo;
    protected ItemDtoShort itemDtoShort;
    LocalDateTime start = LocalDateTime.of(2024, 04, 27, 10, 00);
    LocalDateTime end = LocalDateTime.of(2024, 04, 28, 10, 00);


    @BeforeEach
    void create() {
        booking = new Booking(
                3L,
                start,
                end
        );

        bookingDto = new BookingDto(
                1L,
                start,
                end,
                itemDtoShort,
                new BookerDtoShort(5L),
                Status.WAITING
        );

        item = new Item(
                "name",
                "description",
                3L,
                Boolean.TRUE
        );
        itemDtoShort = new ItemDtoShort(
                3L,
                "name"
        );

        user = new User(
                2L,
                "user",
                "user@user.com"
        );
        userTwo = new User(
                10L,
                "userTwo",
                "user@.com"
        );
        user = userRepository.save(user);
        userTwo = userRepository.save(userTwo);
        item.setOwner(userTwo.getId());
        item = itemRepository.save(item);
    }

    @Autowired
    BookingServiceImpl bookingService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Test
    void createBooking() {
        booking.setItemId(item.getId());
        BookingDto savedBooking = bookingService.createBooking(user.getId(), booking);
        assertNotNull(savedBooking);
        assertEquals(item.getId(), savedBooking.getItem().getId());
        assertEquals(start, savedBooking.getStart());
    }

    @Test
    void createBookingEx() {
        booking.setItemId(item.getId());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookingService.createBooking(10L, booking));
        booking.setEnd(LocalDateTime.now());
        ValidationException ex = assertThrows(ValidationException.class,
                () -> bookingService.createBooking(user.getId(), booking));
    }

    @Test
    void createBookingEx2() {
        booking.setItemId(item.getId());
        BookingDto savedBooking = bookingService.createBooking(user.getId(), booking);
        booking.setStatus(Status.APPROVED);
        Booking booking2 = new Booking(
                item.getId(),
                LocalDateTime.of(2024, 04, 27, 11, 00),
                LocalDateTime.of(2024, 04, 28, 9, 00)
        );

        ValidationException ex2 = assertThrows(ValidationException.class,
                () -> bookingService.createBooking(user.getId(), booking2));
    }


    @Test
    void getBooking() {
        booking.setItemId(item.getId());
        BookingDto savedBooking = bookingService.createBooking(user.getId(), booking);
        BookingDto result = bookingService.getBooking(user.getId(), savedBooking.getId());
        assertNotNull(savedBooking);
        assertEquals(savedBooking.getId(), result.getId());
        assertEquals(savedBooking.getEnd(), result.getEnd());
    }

    @Test
    void getBookingEx() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookingService.getBooking(3L, 5L));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> bookingService.getBooking(user.getId(), 5L));

        User user3 = new User(
                "user3",
                "user@user3.com"
        );
        User savedUser3 = userRepository.save(user3);
        booking.setItemId(item.getId());
        booking.setBookerId(user.getId());
        BookingDto savedBooking = bookingService.createBooking(user.getId(), booking);
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> bookingService.getBooking(savedUser3.getId(), savedBooking.getId()));

    }

    @Test
    void setApproved() {
        item.setOwner(user.getId());
        booking.setItemId(item.getId());
        BookingDto savedBooking = bookingService.createBooking(userTwo.getId(), booking);
        BookingDto result = bookingService.setApproved(user.getId(), savedBooking.getId(), Boolean.TRUE);
        assertNotNull(result);
        assertEquals(Status.APPROVED, result.getStatus());
    }

    @Test
    void setApprovedEx() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookingService.setApproved(15L, 1L, Boolean.TRUE));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> bookingService.setApproved(user.getId(), 15L, Boolean.TRUE));

        item.setOwner(user.getId());
        booking.setItemId(item.getId());
        BookingDto savedBooking = bookingService.createBooking(userTwo.getId(), booking);
        bookingService.setApproved(user.getId(), savedBooking.getId(), Boolean.TRUE);
        ValidationException ex2 = assertThrows(ValidationException.class,
                () -> bookingService.setApproved(user.getId(), savedBooking.getId(), Boolean.TRUE));
    }

    @Test
    void getAllBooking() {
        item.setOwner(user.getId());
        booking.setItemId(item.getId());
        BookingDto savedBooking = bookingService.createBooking(userTwo.getId(), booking);
        bookingService.setApproved(user.getId(), savedBooking.getId(), Boolean.TRUE);
        List<BookingDto> result = bookingService.getAllBooking(userTwo.getId(), "ALL", 0, 20);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getAllBookingEx() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookingService.getAllBooking(5L, "ALL", 0, 20));
        ValidationException ex = assertThrows(ValidationException.class,
                () -> bookingService.getAllBooking(userTwo.getId(), "ALL", 0, 0));

    }

    @Test
    void getAllForOwner() {
        item.setOwner(userTwo.getId());
        booking.setItemId(item.getId());
        BookingDto savedBooking = bookingService.createBooking(user.getId(), booking);
        List<BookingDto> result = bookingService.getAllForOwner(userTwo.getId(), "ALL", 0, 20);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(start, result.get(0).getStart());
        assertEquals(end, result.get(0).getEnd());
    }

    @Test
    void getAllForOwnerEx() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> bookingService.getAllForOwner(userTwo.getId(), "ALL", 0, 0));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookingService.getAllForOwner(10L, "ALL", 0, 20));
    }

    @Test
    void getBookingsByStateCurrent() {
        item.setOwner(user.getId());
        Booking bookingCurrent = new Booking(
                4L,
                start,
                end
        );
        bookingCurrent.setItemId(item.getId());
        bookingService.createBooking(userTwo.getId(), bookingCurrent);
        bookingCurrent.setStart(LocalDateTime.of(2022, 04, 27, 10, 00));
        List<BookingDto> result = bookingService.getAllBooking(userTwo.getId(), "CURRENT", 0, 20);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(LocalDateTime.of(2022, 04, 27, 10, 00), result.get(0).getStart());
    }

    @Test
    void getBookingsByStateFuture() {
        item.setOwner(user.getId());
        booking.setItemId(item.getId());
        bookingService.createBooking(userTwo.getId(), booking);
        List<BookingDto> result = bookingService.getAllBooking(userTwo.getId(), "FUTURE", 0, 20);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(LocalDateTime.of(2024, 04, 27, 10, 00), result.get(0).getStart());
    }

    @Test
    void getBookingsByStateWaiting() {
        item.setOwner(user.getId());
        booking.setItemId(item.getId());
        booking.setStatus(Status.WAITING);
        bookingService.createBooking(userTwo.getId(), booking);
        List<BookingDto> result = bookingService.getAllBooking(userTwo.getId(), "WAITING", 0, 20);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Status.WAITING, result.get(0).getStatus());
    }

    @Test
    void getBookingsByStateRejected() {
        item.setOwner(user.getId());
        booking.setItemId(item.getId());
        BookingDto savedBooking = bookingService.createBooking(userTwo.getId(), booking);
        Booking booking1 = bookingRepository.findById(savedBooking.getId()).get();
        booking1.setStatus(Status.REJECTED);
        List<BookingDto> result = bookingService.getAllBooking(userTwo.getId(), "REJECTED", 0, 20);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Status.REJECTED, result.get(0).getStatus());
    }

    @Test
    void getBookingsByStatePast() {
        item.setOwner(user.getId());
        booking.setItemId(item.getId());
        bookingService.createBooking(userTwo.getId(), booking);
        booking.setStart(LocalDateTime.of(2020, 04, 27, 10, 00));
        booking.setEnd(LocalDateTime.of(2022, 04, 27, 10, 00));
        List<BookingDto> result = bookingService.getAllBooking(userTwo.getId(), "PAST", 0, 20);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(LocalDateTime.of(2020, 04, 27, 10, 00), result.get(0).getStart());
    }

}
