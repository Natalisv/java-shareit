package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    @Autowired
    private BookingServiceImpl bookingServiceImpl;

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody Booking booking) {
        return bookingServiceImpl.createBooking(userId, booking);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long bookingId) {
        return bookingServiceImpl.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approvedBooking(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long bookingId, @RequestParam Boolean approved) {
        return bookingServiceImpl.setApproved(userId, bookingId, approved);
    }

    @GetMapping
    public List<BookingDto> getAllBooking(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(required = false) String state,
                                          @RequestParam(required = false) Integer from, @RequestParam(required = false) Integer size) {
        if (state == null) state = "ALL";
        return bookingServiceImpl.getAllBooking(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllForOwner(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(required = false) String state,
                                           @RequestParam(required = false) Integer from, @RequestParam(required = false) Integer size) {
        if (state == null) state = "ALL";
        return bookingServiceImpl.getAllForOwner(userId, state, from, size);
    }
}
