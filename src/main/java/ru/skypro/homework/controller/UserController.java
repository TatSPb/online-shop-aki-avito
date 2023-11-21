package ru.skypro.homework.controller;

import com.sun.istack.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;


/*** ЭТАП #1 - Написание DTO, контроллеров ***/

@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:3000")
public class UserController {

    private  final UserService userService;
    private  final UserRepository userRepository;
    private  final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService,
                          UserRepository userRepository,
                          AuthService authService,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }


    /*** Method to update an authorized user data / Метод для обновления данных об авторизованном пользователе
     * Methods in use / Используемые методы:
     * {@link #updateUser(UserDto, Authentication)} * To update username / Метод для изменения имени пользователя
     ***/
    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, Authentication authentication) {
        if (userService.updateUser(userDto)) {
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /*** Method to set a new user password  / Метод для обновления пароля пользователя
     * Methods in use / Используемые методы:
     * {@link UserRepository#findByUsername} * To find the user by username / Поиск пользователя по имени пользователя
     * {@link PasswordEncoder#matches}
     * {@link PasswordEncoder#encode} * To produce an object representation to be transmitted as a WebSocket message
     ***/
    @PostMapping("/set_password")
    public boolean setPassword(NewPassword newPassword,Authentication authentication) {
        UserModel currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        if (passwordEncoder.matches(newPassword.getCurrentPassword(), currentUser.getPassword())) {
            currentUser.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
            return true;
        }
        return false;
    }

    /*** Method to get an authorized user data  / Метод для получения информации об авторизованном пользователе **/
    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser(@NotNull Authentication authentication) {
        return ResponseEntity.ok(userService.getUser(authentication));
    }

    /*** Method to update an authorized user avatar  / Метод для обновления аватара авторизованного пользователя **/
    @PatchMapping(value = "me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserDto updateUserImage(@RequestParam("image") MultipartFile file) throws IOException {
        return new UserDto();
    }


}