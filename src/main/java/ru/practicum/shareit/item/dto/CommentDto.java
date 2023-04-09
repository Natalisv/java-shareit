package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {

    public Long id;

    String text;

    Long itemId;

    Long authorId;

    String authorName;

    LocalDateTime created;

    public CommentDto(Long id, String text, Long itemId, Long authorId, String authorName, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.itemId = itemId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.created = created;
    }
}
