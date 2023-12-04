package ru.skypro.homework.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class AdDTO {
    Integer author;
    String image;
    Integer pk;
    Integer price;
    String title;
}