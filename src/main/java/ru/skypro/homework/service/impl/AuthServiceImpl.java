package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.service.AuthService;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserService userService;

    public AuthServiceImpl(UserDetailsManager manager, PasswordEncoder passwordEncoder, UserService userService) {
        this.manager = manager;
        this.encoder = passwordEncoder;
        this.userService = userService;
    }

    /**
     * checks existing user by login and password
     * @param userName - string
     * @param password - string
     * @return boolean - result if exists
     */
    @Override
    public boolean login(String userName, String password) {

        if (!manager.userExists(userName))
            return false;

        UserDetails userDetails = manager.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    /**
     * creates user at db if does not exist
     * @param register - dto RegisterDto
     * @param role - Role
     * @return boolean - result if was created
     */
    @Override
    public boolean register(RegisterDto register, Role role) {

        if (manager.userExists(register.getUsername()))
            return false;

        manager.createUser(
                User.builder()
                        .passwordEncoder(this.encoder::encode)
                        .password(register.getPassword())
                        .username(register.getUsername())
                        .roles(role.name())
                        .build());

        userService.registerUser(register, this.encoder.encode(register.getPassword()), role);
        return true;
    }

    /**
     * changes password of user
     * @param login - string
     * @param newPassword - dto NewPasswordDto
     * @return boolean - result if password changed
     */
    @Override
    public boolean changeUserPassword(String login, NewPasswordDto newPassword) {

        if (!encoder.matches(newPassword.getCurrentPassword(), manager.loadUserByUsername(login).getPassword()))
            return false;

        Optional<ru.skypro.homework.model.User> userOptional = userService
                .updatePassword(login, this.encoder.encode(newPassword.getNewPassword()));
        if (userOptional.isEmpty())
            return false;

        Role role = userOptional.get().getRole();
        manager.updateUser(User.builder()
                .passwordEncoder(this.encoder::encode)
                .password(newPassword.getNewPassword())
                .username(login)
                .roles(role.name())
                .build());
        return true;
    }
}
