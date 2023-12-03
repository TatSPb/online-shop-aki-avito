package ru.skypro.homework.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDTO {
    Integer author;
    String authorImage;
    String authorFirstName;
    Long createdAt;
    Integer pk;
    String text;
}