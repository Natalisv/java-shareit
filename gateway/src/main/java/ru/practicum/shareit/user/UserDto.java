package ru.practicum.shareit.user;

import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String name;

    private String email;

    public UserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserDto() {
    }
}
