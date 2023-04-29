package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.transaction.Transactional;
import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Transactional
    List<ItemRequest> findByRequestorId(Long requestorId);
}
