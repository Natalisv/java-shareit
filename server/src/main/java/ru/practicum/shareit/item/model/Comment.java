package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "author_name")
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
