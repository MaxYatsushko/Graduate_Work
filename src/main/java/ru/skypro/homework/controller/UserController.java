package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserUpdateDto;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.impl.UserService;

import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("users")
@CrossOrigin(value = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }
    @PostMapping("set_password")
    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPassword) {

        if (authService.changeUserPassword(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName(), newPassword))
            return new ResponseEntity<>(HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @GetMapping("me")
    public ResponseEntity<User> getUser() {
        Optional<User> userOptional = userService
                .getUserByLogin(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName()
                );

        //TODO Complete the method
        return userOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PatchMapping("me")
    public ResponseEntity<User> updateUser(@RequestBody UserUpdateDto userUpdateDto) {

        Optional<User> userOptional = userService.updateUserInfo(
                userUpdateDto,
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName());

        return userOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());

    }

    @PatchMapping(value = "me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Image> updateUserImage(@RequestBody MultipartFile image) {
        Optional<Image> imageOptional = userService.getUserImage(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        return imageOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}