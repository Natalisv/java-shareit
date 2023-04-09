package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.util.Date;


@Data
public class ItemRequestDto {

    Long id;

    String description;

    Long requestor;

    Date created;
}
