package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ru.skypro.homework.dto.*;

/**
 * КОНТРОЛЛЕР ПО РАБОТЕ С ПОЛУЧЕНИЕМ И ОБНОВЛЕНИЕМ ИНФОРМАЦИИ ОБ АВТОРИЗИРОВАННОМ ПОЛЬЗОВАТЕЛЕ.
 */

@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
public class UserController {

    /**
     * ОБНОВЛЕНИЕ ЛИЧНОЙ ИНФОРМАЦИИ ПОЛЬЗОВАТЕЛЯ В ПРОФИЛЕ (ИМЯ, ФАМИЛИЯ, ТЕЛЕФОН).
     */
    @Tag(name = "Пользователи")
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @PatchMapping("/me")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateReq userUpdateReq) {
        //some code
        return ResponseEntity.ok().body(null);
    }

    /**
     * ОБНОВЛЯЕТ ПАРОЛЬ АВТОРИЗИРОВАННОГО ПОЛЬЗОВАТЕЛЯ.
     **/
    @Tag(name = "Пользователи")
    @Operation(summary = "Обновление пароля")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody PasswordDTO newPassword) {
        //some code
        return ResponseEntity.ok().build();
    }

    /**
     * ПОЛУЧАЕТ ИНФОРМАЦИЮ ОБ АВТОРИЗИРОВАННОМ ПОЛЬЗОВАТЕЛЕ.
     **/
    @Tag(name = "Пользователи")
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @GetMapping("/me")
    public ResponseEntity<?> getUser() {
        //some code
        return ResponseEntity.ok().body(null);
    }

    /**
     * ОБНОВЛЯЕТ АВАТАР АВТОРИЗИРОВАННОГО ПОЛЬЗОВАТЕЛЯ.
     **/
    @Tag(name = "Пользователи")
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @PatchMapping(value = "me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam("image") MultipartFile file) {
        //some code
        return ResponseEntity.ok().build();
    }
}
