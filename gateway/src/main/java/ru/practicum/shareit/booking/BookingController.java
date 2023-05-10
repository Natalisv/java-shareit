package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody Booking booking) {
        return bookingClient.createBooking(userId, booking);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approvedBooking(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long bookingId,
                                                  @RequestParam Boolean approved) {
        return bookingClient.setApproved(userId, bookingId, approved);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new ValidationException("Unknown state: " + stateParam));
        return bookingClient.getAllBooking(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllForOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new ValidationException("Unknown state: " + stateParam));
        return bookingClient.getAllForOwner(userId, state, from, size);
    }
}
