package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.model.User;

@Service
public class UserService {
    /**
     * Маппер для преобразования entity-объекта User в DTO-объект UserDTO.
     */
    public UserDTO userToUserDTO(User user) {
        if (user.getId() > 0 && !user.getUsername().isEmpty() && !user.getFirstName().isEmpty()
                && !user.getLastName().isEmpty() && user.getRole() != null &&
                !user.getPhone().isEmpty()) {
            if (user.getImage() != null) {
                return new UserDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(),
                        user.getRole(), user.getPhone(),
                        "/users/avatar/" + user.getId());
            } else {
                return new UserDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(),
                        user.getRole(), user.getPhone(),
                        null);
            }
        }
        throw new IllegalArgumentException();
    }
}
