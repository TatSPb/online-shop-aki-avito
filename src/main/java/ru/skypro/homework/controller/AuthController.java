package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.LoginReq;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.service.AuthService;

/**
 * КОНТРОЛЛЕР ДЛЯ РАБОТЫ С РЕГИСТРАЦИЕЙ И АВТОРИЗАЦИЕЙ ПОЛЬЗОВАТЕЛЯ
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * АВТОРИЗИРУЕТ НОВОГО ПОЛЬЗОВАТЕЛЯ НА ОСНОВЕ СУЩЕСТВУЮЩЕЙ УЧЕТНОЙ ЗАПИСИ.
     *
     * @param req - DTO-объект с учетными данными пользователя (LoginReq req)
     * @return - ответ сервера со статусом 200 / 401
     **/
    @Tag(name = "Авторизация")
    @Operation(summary = "Авторизация пользователя")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq req) {
        if (authService.login(req.getUsername(), req.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * РЕГИСТРИРУЕТ НОВОГО ПОЛЬЗОВАТЕЛЯ.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterReq req) {
        Role role = req.getRole();
        if (authService.register(req, role)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
