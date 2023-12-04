package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class RegisterReq {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
}