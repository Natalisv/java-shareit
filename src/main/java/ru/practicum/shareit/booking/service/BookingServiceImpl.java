package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingMapper bookingMapper;


    @Transactional
    public BookingDto createBooking(Long userId, Booking booking) {
        if (isContainsUser(userId)) {
            if (isValid(booking)) {
                Item item = itemRepository.findById(booking.getItemId()).orElseThrow(() -> {
                    throw new IllegalArgumentException();
                });
                if (item.getOwner().equals(userId)) {
                    throw new IllegalArgumentException();
                }
                if (Boolean.TRUE.equals(item.getAvailable())) {
                    List<Booking> existBookings = bookingRepository.findByItemId(item.getId());
                    isAvailable(existBookings, booking);
                    return saveBooking(booking, userId);
                } else {
                    log.error("Вещь не доступна к бронированию");
                    throw new ValidationException("Вещь не доступна к бронированию");
                }
            } else {
                log.error("Невалидная сущность");
                throw new ValidationException("Невалидная сущность");
            }
        } else {
            log.error("Пользователя с userId = " + userId + " не существует");
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public BookingDto getBooking(Long userId, Long bookingId) {
        if (isContainsUser(userId)) {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
                throw new IllegalArgumentException();
            });
            if (booking.getBookerId().equals(userId) || itemRepository.findById(booking.getItemId()).get().getOwner().equals(userId)) {
                return bookingMapper.toBookingDto(booking);
            } else {
                log.error("У пользователя нет бронирований");
                throw new IllegalArgumentException();
            }
        } else {
            log.error("Пользователя с userId = " + userId + " не существует");
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public BookingDto setApproved(Long userId, Long bookingId, Boolean approved) {
        if (isContainsUser(userId)) {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
                throw new IllegalArgumentException();
            });
            if (booking.getStatus().equals(Status.APPROVED)) {
                throw new ValidationException("Бронирование уже утверждено");
            }
            Item item = itemRepository.findById(booking.getItemId()).get();
            if (Boolean.TRUE.equals(item.getAvailable())) {
                return this.setApproved(item, userId, booking, approved);
            } else {
                throw new ValidationException("Вещь не доступа к бронированию");
            }
        } else {
            log.error("Пользователя с userId = " + userId + " не существует");
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public List<BookingDto> getAllBooking(Long userId, String state) {
        if (isContainsUser(userId)) {
            List<Booking> bookings = bookingRepository.findByBookerId(userId);
            bookings = bookings.stream().sorted(Comparator.comparing(Booking::getId).reversed()).collect(Collectors.toList());
            return getBookingsByState(bookings, state);
        } else {
            log.error("Пользователя с userId = " + userId + " не существует");
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public List<BookingDto> getAllForOwner(Long userId, String state) {
        if (isContainsUser(userId)) {
            List<Item> items = itemRepository.findByOwner(userId);
            if (items != null) {
                List<Booking> bookings = bookingRepository.findAll();
                bookings = bookings.stream().sorted(Comparator.comparing(Booking::getId).reversed()).collect(Collectors.toList());
                List<Booking> userBookings = new ArrayList<>();
                for (Item i : items) {
                    userBookings.addAll(bookings.stream().filter(b -> b.getItemId().equals(i.getId())).collect(Collectors.toList()));
                }
                return getBookingsByState(userBookings, state);
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            log.error("Пользователя с userId = " + userId + " не существует");
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public List<BookingDto> getBookingsByState(List<Booking> bookings, String state) {
        switch (state) {
            case ("ALL"):
                return bookings.stream().map(b -> bookingMapper.toBookingDto(b)).collect(Collectors.toList());
            case ("CURRENT"):
                return bookings.stream()
                        .filter(b -> b.getStart().isBefore(LocalDateTime.now()) && b.getEnd().isAfter(LocalDateTime.now()))
                        .sorted(Comparator.comparing(Booking::getId))
                        .map(b -> bookingMapper.toBookingDto(b))
                        .collect(Collectors.toList());
            case ("FUTURE"):
                return bookings.stream()
                        .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                        .map(b -> bookingMapper.toBookingDto(b))
                        .collect(Collectors.toList());
            case ("WAITING"):
                return bookings.stream()
                        .filter(b -> b.getStatus().equals(Status.WAITING))
                        .map(b -> bookingMapper.toBookingDto(b))
                        .collect(Collectors.toList());
            case ("REJECTED"):
                return bookings.stream()
                        .filter(b -> b.getStatus().equals(Status.REJECTED))
                        .map(b -> bookingMapper.toBookingDto(b))
                        .collect(Collectors.toList());
            case ("PAST"):
                return bookings.stream()
                        .filter(b -> b.getStart().isBefore(LocalDateTime.now()) && b.getEnd().isBefore(LocalDateTime.now()))
                        .map(b -> bookingMapper.toBookingDto(b))
                        .collect(Collectors.toList());
            default:
                throw new ValidationException("Unknown state: " + state);
        }
    }

    private boolean isContainsUser(Long id) {
        return userRepository.existsById(id);
    }

    private boolean isValid(Booking bookingDto) {
        return bookingDto.getItemId() != null && bookingDto.getStart() != null && bookingDto.getEnd() != null &&
                bookingDto.getStart().isAfter(LocalDateTime.now()) && bookingDto.getEnd().isAfter(LocalDateTime.now())
                && bookingDto.getStart().isBefore(bookingDto.getEnd())
                && !bookingDto.getStart().equals(bookingDto.getEnd());
    }

    private boolean isAvailable(List<Booking> existBookings, Booking booking) {
        for (Booking existBooking : existBookings) {
            if (existBooking.getStatus().equals(Status.APPROVED) && booking.getStart().isAfter(existBooking.getStart()) && booking.getEnd().isBefore(existBooking.getEnd())) {
                log.error("Вещь не доступна к бронированию");
                throw new ValidationException("Вещь не доступна к бронированию");
            }
        }
        return Boolean.TRUE;
    }

    private BookingDto saveBooking(Booking booking, Long userId) {
        booking.setStatus(Status.WAITING);
        booking.setBookerId(userId);
        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingDto(savedBooking);
    }

    private BookingDto setApproved(Item item, Long userId, Booking booking, Boolean approved) {
        if (item.getOwner().equals(userId)) {
            if (Boolean.TRUE.equals(approved)) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
            bookingRepository.save(booking);
            return bookingMapper.toBookingDto(booking);
        } else {
            log.error("Бронирование bookingId = " + booking.getId() + " не найдено");
            throw new IllegalArgumentException();
        }
    }
}
