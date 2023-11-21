package ru.skypro.homework.service;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.*;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.repository.*;

import java.util.Optional;


/*** Service to deal with user data / Сервис для работы с данными пользователя  ***/

@Service
@Primary
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    /*** Method to create a new UserModel based on user registration data/
     * Метод для создания нового пользователя на базе его данных, полученных при регистрации.
     * Methods in use / Используемые методы:
     * {@link UserRepository#findByUsername} * To find the user by username / Поиск пользователя по имени пользователя
     * {@link PasswordEncoder#encode} * To produce an object representation to be transmitted as a WebSocket message
     * {@link UserRepository#save} * To save user into user repository / Сохранение пользователя в репозиторий
     *  ***/
    public void createUser(RegisterReqDto registerReqDto) throws UsernameAlreadyExistsException {
        if (userRepository.findByUsername(registerReqDto.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }
        UserModel userModel = new UserModel();
        userModel.setUsername(registerReqDto.getUsername());
        userModel.setPassword(passwordEncoder.encode(registerReqDto.getPassword()));
        userModel.setRole(Role.USER);
        userModel.setFirstName(registerReqDto.getFirstName());
        userModel.setLastName(registerReqDto.getLastName());
        userModel.setPhone(registerReqDto.getPhone());
        userRepository.save(userModel);
    }

    /*** Method to update username / Метод для изменения имени пользователя
     * Methods in use / Используемые методы:
     * {@link UserRepository#findByUsername} * To find the user by username / Поиск пользователя по имени пользователя
     * {@link UserMapper#toUserModel} * To convert UserDto  -> UserModel / Преобразование UserDto  -> UserModel
     * ***/
    @Transactional
    public boolean updateUser(UserDto userDto) {
        Optional<UserModel> currentUser = userRepository.findByUsername(getCurrentUsername());
        currentUser.ifPresent((userModel) -> userMapper.toUserModel(userModel, userDto));
        return currentUser.isPresent();
    }



    /*** Method to update user password / Метод для изменения пароля пользователя.
     * Methods in use / Используемые методы:
     * {@link UserRepository#findByUsername} * To find the user by username / Поиск пользователя по имени пользователя
     * {@link PasswordEncoder#matches}
     * {@link PasswordEncoder#encode} * To produce an object representation to be transmitted as a WebSocket message
     * ***/
    public boolean setPassword(NewPassword newPassword, Authentication authentication) {
        UserModel currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        if (passwordEncoder.matches(newPassword.getCurrentPassword(), currentUser.getPassword())) {
            currentUser.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
            return true;
        }
        return false;
    }

    /*** Method to get user by username / Метод для поиска пользователя по имени пользователя.
     * Methods in use / Используемые методы:
     * {@link UserRepository#findByUsername} * To find the user by username / Поиск пользователя по имени пользователя
     * {@link UserMapper#toUserDto} * To convert UserModel -> UserDto
     * ***/
    public UserDto getUser(Authentication authentication) {
        UserModel currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Sorry, we couldn't find this Username "));
        UserDto userDto = new UserDto();
        userMapper.toUserDto(userDto, currentUser);
        return userDto;
    }


    /*** Method to get a current username / Метод для получения текущего имени пользователя. ***/
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
