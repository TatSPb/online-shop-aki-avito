package ru.skypro.homework.dto;

import lombok.Data;

/*** Data Transfer Object / Объект передачи данных ***/
@Data
public class CommentDto {
    private Integer author;
    private String authorImage;
    private String authorFirstName;
    private Long createdAt;
    private Integer pk;
    private String text;

    public CommentDto() {
    }
}
