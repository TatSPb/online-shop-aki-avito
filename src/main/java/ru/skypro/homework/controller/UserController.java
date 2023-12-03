package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.PasswordDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.dto.UserUpdateReq;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.service.impl.UserService;

/**
 * КОНТРОЛЛЕР ПО РАБОТЕ С ПОЛУЧЕНИЕМ И ОБНОВЛЕНИЕМ ИНФОРМАЦИИ ОБ АВТОРИЗИРОВАННОМ ПОЛЬЗОВАТЕЛЕ.
 */
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** ОБНОВЛЯЕТ ПАРОЛЬ АВТОРИЗИРОВАННОГО ПОЛЬЗОВАТЕЛЯ.
     * @param  newPassword - DTO-объект с новым паролем (PasswordDTO newPassword)
     * @return: ответ сервера со статусом 200
     * */
    @Tag(name = "Пользователи")
    @Operation(summary = "Обновление пароля")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody PasswordDTO newPassword) throws UnauthorizedException {
        userService.updateUserPassword(newPassword);
        return ResponseEntity.ok().build();
    }

    /** ПОЛУЧАЕТ ИНФОРМАЦИЮ ОБ АВТОРИЗИРОВАННОМ ПОЛЬЗОВАТЕЛЕ.
     * @return: ответ сервера со статусом 200 / 401
     * */
    @Tag(name = "Пользователи")
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @GetMapping("/me")
    public ResponseEntity<?> getUser() throws UnauthorizedException {
        UserDTO userDTO = userService.getUser();
        return ResponseEntity.ok().body(userDTO);
    }


    /** ОБНОВЛЕНИЕ ЛИЧНОЙ ИНФОРМАЦИИ ПОЛЬЗОВАТЕЛЯ В ПРОФИЛЕ (ИМЯ, ФАМИЛИЯ, ТЕЛЕФОН).
     * @param  userUpdateReq - DTO-объект с новыми данными пользователя (UserUpdateReq userUpdateReq)
     * @return: ответ сервера со статусом 200 / 401
     * */
    @Tag(name = "Пользователи")
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @PatchMapping("/me")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateReq userUpdateReq) throws UnauthorizedException {
        userService.updateUser(userUpdateReq);
        return ResponseEntity.ok().body(userUpdateReq);
    }


    /** ОБНОВЛЯЕТ АВАТАР АВТОРИЗИРОВАННОГО ПОЛЬЗОВАТЕЛЯ.
     * @param file - файл с новым изображением пользователя (MultipartFile file)
     * @return: ответ сервера со статусом 200 / 401
     * */
    @Tag(name = "Пользователи")
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @PatchMapping(value ="/me/image",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam("image") MultipartFile file) throws UnauthorizedException {
        userService.updateUserImage(file);
        return ResponseEntity.ok().build();
    }
}
