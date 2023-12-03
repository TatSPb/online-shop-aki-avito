package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class PasswordDTO {
    String currentPassword;
    String newPassword;
}
