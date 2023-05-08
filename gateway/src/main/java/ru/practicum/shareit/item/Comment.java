package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Comment {

    private Long id;

    private String text;

    private Long itemId;

    private Long authorId;

    private String authorName;

    private LocalDateTime created;

    public Comment(Long id, String text, Long itemId, Long authorId) {
        this.id = id;
        this.text = text;
        this.itemId = itemId;
        this.authorId = authorId;
    }

    public Comment() {
    }

    public Comment(String text) {
        this.text = text;
    }
}
