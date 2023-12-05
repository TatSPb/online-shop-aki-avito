
package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class PasswordDTO {
    private String currentPassword;
    private String newPassword;
}
