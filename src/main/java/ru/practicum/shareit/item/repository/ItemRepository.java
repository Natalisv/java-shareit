package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Transactional
    Item findByIdAndOwner(Long id, Long owner);

    @Transactional
    List<Item> findByOwner(Long owner);

    @Transactional
    List<Item> findByRequestId(Long requestorId);


    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update Item i set i.name = ?1  where i.id = ?2")
    void updateItemName(String name, Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update Item i set i.description = ?1  where i.id = ?2")
    void updateItemDescription(String description, Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update Item i set i.available = ?1  where i.id = ?2")
    void updateItemAvailable(Boolean available, Long id);

}
