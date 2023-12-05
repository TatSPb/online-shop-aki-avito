package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skypro.homework.dto.Role;

@Data
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private  Role role;
    private String phone;
    private  String image;
}
