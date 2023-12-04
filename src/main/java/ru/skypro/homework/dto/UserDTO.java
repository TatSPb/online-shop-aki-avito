package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skypro.homework.dto.Role;

@Data
@AllArgsConstructor
public class UserDTO {
    Integer id;
    String email;
    String firstName;
    String lastName;
    Role role;
    String phone;
    String image;
}
