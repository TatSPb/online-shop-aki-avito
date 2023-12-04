package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.User;

import java.io.IOException;


/*** ЭТАП #1 - Написание DTO, контроллеров ***/

@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:3000")
public class UserController {

    /*** Method to update an authorized user data / Метод для обновления данных об авторизованном пользователе ***/
    @PatchMapping("/me")
    public User updateUser(@RequestBody User user) {
        System.out.println("Инфо в логах: updateUser() works correctly");
        return new User();
    }

    /*** Method to set a new user password  / Метод для обновления пароля пользователя **/
    @PostMapping("/set_password")
    public User setPassword(@RequestBody NewPassword newPassword, Authentication authentication) {
        System.out.println("Инфо в логах: setPassword() works correctly");
        return new User();
    }

    /*** Method to get an authorized user data  / Метод для получения информации об авторизованном пользователе **/
    @GetMapping("/me")
    public User getUser(Authentication authentication) {
        return new User();
    }

    /*** Method to update an authorized user avatar  / Метод для обновления аватара авторизованного пользователя **/
    @PatchMapping(value = "me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public User updateUserImage(@RequestParam("image") MultipartFile file) throws IOException {
        return new User();
    }


}
