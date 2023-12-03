package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class AdDTO {
    Integer author;
    String image;
    Integer pk;
    Integer price;
    String title;

}
