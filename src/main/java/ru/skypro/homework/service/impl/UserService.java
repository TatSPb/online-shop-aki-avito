package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.PasswordDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.dto.UserDetailsDTO;
import ru.skypro.homework.dto.UserUpdateReq;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.model.UserPrincipal;
import ru.skypro.homework.repository.UserRepository;

import javax.transaction.Transactional;

import java.util.NoSuchElementException;

import static ru.skypro.homework.utils.ValidationUtils.isNotEmptyAndNotNull;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ImageService imageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
    }

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

    /**
     * Метод для поиска авторизированного пользователя по его имени
     *
     * @return объект авторизированного пользователя
     */
    public User getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepository.findUserByUsername(currentPrincipalName).orElseThrow();
    }

    /**
     * Метод для обновления данных пользователя в профиле (имя, фамилия, телефон)
     *
     * @param req - объект с новыми данными пользователя (UserUpdateReq req)
     */
    public void updateUser(UserUpdateReq req) throws UnauthorizedException {
        LOG.info("Was invoked method UPDATE_USER");
        User user = getAuthUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setPhone(req.getPhone());
        userRepository.save(user);
    }

    /**
     * Метод для обновления пароля пользователя.
     *
     * @param passwordDTO новый пароль (PasswordDTO passwordDTO)
     * @return true / false
     */
    @Transactional
    public boolean updateUserPassword(PasswordDTO passwordDTO) {
        LOG.info("Was invoked method UPDATE_USER_PASSWORD");
        if (isNotEmptyAndNotNull(passwordDTO.getNewPassword()) && isNotEmptyAndNotNull(passwordDTO.getCurrentPassword())) {
            User user = getAuthUser();
            user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /**
     * Метод для получения авторизированного пользователя
     *
     * @return объект UserDTO
     */
    public UserDTO getUser() throws UnauthorizedException {
        LOG.info("Was invoked method GET_USER");
        User user = getAuthUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        return userToUserDTO(user);
    }

    /**
     * Метод для обновления аватара пользователя.
     * Принимает файл с изображением и сохраняет его, ссылку на аватар добавляет в БД.
     *
     * @param file новый аватар (MultipartFile file)
     * @return true / false
     */
    public boolean updateUserImage(MultipartFile file) throws UnauthorizedException {
        LOG.info("Was invoked method UPDATE_USER_IMAGE");
        User user = getAuthUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        imageService.updateUserImage(user.getUsername(), file);
        return true;
    }

    /**
     * Метод для загрузки данных пользователя по его имени.
     *
     * @param username имя пользователя (String username)
     * @return UserDetail - данные пользователя
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow();
        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(user.getUsername(), user.getPassword(), user.getId(), user.getRole());
        return new UserPrincipal(userDetailsDTO);
    }

    /**
     * Метод для подтверждения существования пользователя в репозитории.
     *
     * @param username имя пользователя (String username)
     * @return true / false
     */
    public boolean userExists(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }

}
