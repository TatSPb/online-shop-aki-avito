package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.NoSuchElementException;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest {

    @Value("${name.of.test.data.file}")
    private String testFileName;


    @Autowired
    AuthServiceImpl authService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @BeforeEach
    public void init(){
        authService.register(new RegisterReq("user@gmail.com", "password", "Max",
                "Maximov", "+7912223344", Role.USER), Role.USER);
    }

    /**
     * Тестирование метода userToUserDTO() с позитивным сценарием трансформации entity в DTO.
     **/
    @Test
    @WithMockUser("user@gmail.com")
    public void userToUserDTO_ReturnsCorrectDTO_withCorrectUser(){
        User user = userService.getAuthUser();
        UserDTO userDTO = userService.userToUserDTO(userService.getAuthUser());
        Assertions.assertEquals(user.getUsername(), userDTO.getEmail());
        Assertions.assertEquals( user.getFirstName(), userDTO.getFirstName());
        Assertions.assertEquals(user.getLastName(), userDTO.getLastName());
        Assertions.assertEquals(user.getPhone(),userDTO.getPhone());
        Assertions.assertEquals(user.getRole(),userDTO.getRole());
    }

    /**
     * Тестирование метода getAuthUser() с позитивным сценарием для получения имени пользователя и его e-mail.
     **/
    @Test
    @WithMockUser("user@gmail.com")
    public void getAuthUserIsNotNull(){
        User user = userService.getAuthUser();
        Assertions.assertEquals("user@gmail.com",user.getUsername());
    }

    /**
     * Тестирование метода userToUserDTO() с негативным сценарием трансформации entity в DTO, если поле username пустое.
     **/
    @Test
    public void userToUserDTO_ThrowsExceptionLackOfUsername(){
        User user = new User(1,"", "Max", "Maximov", "+79215554466", Role.USER, null);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> userService.userToUserDTO(user));
    }

    /**
     * Тестирование метода userToUserDTO() с негативным сценарием трансформации entity в DTO, если поле firstname пустое.
     **/
    @Test
    public void userToUserDTO_ThrowsExceptionLackOfFirstName(){
        User user = new User(1,"user", "", "Maximov", "+79215554466", Role.USER, null);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> userService.userToUserDTO(user));
    }

    /**
     * Тестирование метода userToUserDTO() с негативным сценарием трансформации entity в DTO, если поле lastname пустое.
     **/
    @Test
    public void userToUserDTO_ThrowsExceptionLackOfLastName(){
        User user = new User(1,"user", "Max", "", "+79215554466", Role.USER, null);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> userService.userToUserDTO(user));
    }

    /**
     * Тестирование метода userToUserDTO() с негативным сценарием трансформации entity в DTO, если поле phone пустое.
     **/
    @Test
    public void userToUserDTO_ThrowsException_LackOfPhone(){
        User user = new User(1,"user", "Max", "Maximov", "", Role.USER, null);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> userService.userToUserDTO(user));
    }
    /**
     * Тестирование метода userToUserDTO() с негативным сценарием трансформации entity в DTO, если не выбрана роль.
     **/
    @Test
    public void userToUserDTO_ThrowsException_LackOfRole(){
        User user = new User(1,"user", "Max", "Maximov", "+79215554466", null, null);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> userService.userToUserDTO(user));
    }

    /**
     * Тестирование метода updateUserPassword() для обновления пароля пользователя.
     * Позитивный сценарий.
     **/
    @Test
    @WithMockUser("user@gmail.com")
    public void updateUserPasswordWithCorrectData(){
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setCurrentPassword("oldPassword");
        passwordDTO.setNewPassword("newPassword");
        Assertions.assertTrue(userService.updateUserPassword(passwordDTO));
    }

    /**
     * Тестирование метода updateUserPassword() для обновления пароля пользователя.
     * Позитивный сценарий.
     **/
    @Test
    @WithMockUser(value = "user@gmail.com")
    public void doesNotUpdatePasswordWithNullNewPassword(){
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setCurrentPassword("password");
        passwordDTO.setNewPassword(null);
        Assertions.assertFalse(userService.updateUserPassword(passwordDTO));
    }

    /**
     * Тестирование метода updateUserPassword() для обновления пароля пользователя.
     * Негативный сценарий, если задан пустой новый пароль.
     **/
    @Test
    @WithMockUser(value = "user@gmail.com")
    public void doesNotUpdatePasswordWithEmptyNewPassword(){
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setCurrentPassword("password");
        passwordDTO.setNewPassword(null);
        Assertions.assertFalse(userService.updateUserPassword(passwordDTO));
    }

    /**
     * Тестирование метода updateUser() для обновления данных пользователя в профиле (имя, фамилия, телефон)
     * Позитивный сценарий.
     **/
    @Test
    @WithMockUser(value = "user@gmail.com")
    public void updatesUserWithCorrectData() throws UnauthorizedException {
        UserUpdateReq userUpdateReq = new UserUpdateReq("Max", "Maximov", "+79112223344");
        userService.updateUser(userUpdateReq);
        User updatedUser = userRepository.findUserByUsername("user@gmail.com").orElseThrow();
        Assertions.assertEquals(userUpdateReq.getFirstName(), updatedUser.getFirstName());
        Assertions.assertEquals(userUpdateReq.getLastName(), updatedUser.getLastName());
        Assertions.assertEquals(userUpdateReq.getPhone(), updatedUser.getPhone());
    }

    /**
     * Тестирование метода getUser() для получения авторизированного пользователя.
     * Позитивный сценарий.
     **/
    @Test
    @WithMockUser(value = "user@gmail.com")
    public void getUserReturnsCorrectUserDTO() throws UnauthorizedException {
        User  user = userService.getAuthUser();
        UserDTO userDTO = userService.getUser();
        Assertions.assertEquals(user.getFirstName(), userDTO.getFirstName());
        Assertions.assertEquals(user.getLastName(), userDTO.getLastName());
        Assertions.assertEquals(user.getUsername(), userDTO.getEmail());
        Assertions.assertEquals(user.getPhone(), userDTO.getPhone());
        Assertions.assertEquals(Role.USER, userDTO.getRole());
    }

    /**
     * Тестирование метода updateUserImage() для обновления аватара пользователя.
     * Позитивный сценарий.
     **/
    @Test
    @WithMockUser(value = "user@gmail.com")
    public void updateUserImageWorksWithCorrectImage() throws UnauthorizedException, IOException {
        MultipartFile multipartFile = new MockMultipartFile("test.jpg", new FileInputStream(testFileName));
        userService.updateUserImage(multipartFile);
        Assertions.assertFalse(userService.getAuthUser().getImage()==null);
    }

    /**
     * Тестирование метода loadUserByUsername() для загрузки данных пользователя по его имени.
     * Позитивный сценарий.
     **/
    @Test
    public void loadUserByUsernameReturnsCorrectUserPrincipal(){
        UserDetails userPrincipal = userService.loadUserByUsername("user@gmail.com");
        Assertions.assertEquals("user@gmail.com", userPrincipal.getUsername());
        Assertions.assertEquals(Collections.singleton("ROLE_USER").toString(), userPrincipal.getAuthorities().toString());
    }

    /**
     * Тестирование метода loadUserByUsername() для загрузки данных пользователя по его имени.
     * Негативный сценарий, если пользователь не найден по имени пользователя.
     **/
    @Test
    public void loadUserByUsernameReturnsWithNotExistingUsernameThrowsException(){
        Assertions.assertThrows(NoSuchElementException.class, ()->userService.loadUserByUsername("user1@gmail.com"));

    }

    /**
     * Тестирование метода userExists() для подтверждения существования пользователя в репозитории.
     * Позитивный сценарий, если пользователь найден по имени пользователя.
     **/
    @Test
    public void userExistsReturnsTrueIfUserExists(){
        Assertions.assertTrue(userService.userExists("user@gmail.com"));
    }

    /**
     * Тестирование метода userExists() для подтверждения существования пользователя в репозитории.
     * Негативный сценарий, если пользователь не найден по имени пользователя.
     **/
    @Test
    public void userExistsReturnsFalseIfUserNotExists(){
        Assertions.assertFalse(userService.userExists("user1@gmail.com"));
    }
}
