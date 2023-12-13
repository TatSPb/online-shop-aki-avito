package ru.skypro.homework.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.User;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.impl.UserService;


@SpringBootTest
public class AuthServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;
    @Mock
    User user;
    @Mock
    RegisterReq registerReq;
    @BeforeEach
    public void init() {
        user = new User("Username", "password", "firstname", "lastName", "+79215003020");
        registerReq = new RegisterReq(user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getRole());
    }

    @Test
    public void whenRegisterReqAndRoleAreNotNullAndUserDoesntExistsRegisterReturnsTrue() {
        Assertions.assertFalse(userService.userExists(user.getUsername()));
        Assertions.assertTrue(authService.register(registerReq, Role.USER));
    }
    @Test
    public void whenRegisterReqAndRoleAreNotNullAndUserExistsRegisterReturnsFalse() {
        authService.register(registerReq, Role.USER);
        Assertions.assertTrue(userService.userExists(user.getUsername()));
        Assertions.assertFalse(authService.register(registerReq, Role.USER));
    }

    @Test
    public void whenRegisterReqIsNotNullRegisterThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, ()->authService.register(null, Role.USER));
    }

    @Test
    public void whenUserExistsLoginReturnsTrueGivenCorrectLoginAndPassword(){
        authService.register(registerReq, Role.USER);
        Assertions.assertTrue(authService.login(user.getUsername(), user.getPassword()));
    }
}
