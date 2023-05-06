package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(Long userId, Booking booking);

    BookingDto getBooking(Long userId, Long bookingId);

    BookingDto setApproved(Long userId, Long bookingId, Boolean approved);

    List<BookingDto> getAllBooking(Long userId, String state, Integer from, Integer size);

    List<BookingDto> getAllForOwner(Long userId, String state, Integer from, Integer size);

    List<BookingDto> getBookingsByState(List<Booking> bookings, String state);
}
