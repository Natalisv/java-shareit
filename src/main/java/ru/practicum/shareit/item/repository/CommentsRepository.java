package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import javax.transaction.Transactional;
import java.util.List;

public interface CommentsRepository extends JpaRepository<Comment, Long> {

    @Transactional
    List<Comment> findByItemId(Long itemId);

}
