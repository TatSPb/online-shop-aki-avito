package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder encoder;
    private final UserService userService;
    private final UserRepository userRepository;
    private final Logger LOG = LoggerFactory.getLogger(AuthServiceImpl.class);

    public AuthServiceImpl(PasswordEncoder passwordEncoder, UserService userService, UserRepository userRepository) {

        this.encoder = passwordEncoder;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public boolean login(String userName, String password) {
        LOG.info("Was invoked method LOGIN");
        if (!userService.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = userService.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(RegisterReq registerReq, Role role) {
        LOG.info("Was invoked method REGISTER");
        if (userService.userExists(registerReq.getUsername())) {
            return false;
        }
        ru.skypro.homework.model.User newUser = new User();
        newUser.setUsername(registerReq.getUsername());
        newUser.setPassword(encoder.encode(registerReq.getPassword()));
        newUser.setFirstName(registerReq.getFirstName());
        newUser.setLastName(registerReq.getLastName());
        newUser.setPhone(registerReq.getPhone());
        newUser.setRole(role);
        userRepository.save(newUser);
        return true;
    }
}
