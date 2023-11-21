package ru.skypro.homework.dto;

import lombok.Data;

/*** Data Transfer Object / Объект передачи данных ***/
@Data
public class UserDto {
    public Long id;
    public String email;
    public String firstName;
    public String lastName;
    public String phone;
    private String image;

    public UserDto() {
    }
}
