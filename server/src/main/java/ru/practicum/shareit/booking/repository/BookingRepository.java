package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import javax.transaction.Transactional;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Transactional
    List<Booking> findByBookerId(Long bookerId);

    @Transactional
    List<Booking> findByItemId(Long itemId);

    @Transactional
    List<Booking> findByBookerIdAndItemId(Long bookerId, Long itemId);
}
