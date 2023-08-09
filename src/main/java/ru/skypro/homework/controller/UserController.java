package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.impl.ImageService;
import ru.skypro.homework.service.impl.UserService;

import java.io.IOException;
import java.util.Optional;


@RestController
@RequestMapping("users")
@CrossOrigin(value = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    private final ImageService imageService;

    public UserController(UserService userService, AuthService authService, ImageService imageService) {
        this.userService = userService;
        this.authService = authService;
        this.imageService = imageService;
    }

    @PostMapping("set_password")
    public ResponseEntity<?> setPassword(@RequestBody NewPasswordDto newPassword) {

        if (authService.changeUserPassword(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName(), newPassword))
            return new ResponseEntity<>(HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("me")
    public ResponseEntity<UserDto> getUser() {
        Optional<UserDto> userOptional = userService
                .getUserDtoByLogin(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName()
                );
        return userOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PatchMapping("me")
    public ResponseEntity<UpdateUserDto> updateUser(@RequestPart UpdateUserDto userUpdateDto) {

        Optional<UpdateUserDto> userGetOptional = userService.updateUserInfo(
                userUpdateDto,
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName());

        return userGetOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());

    }

    @PatchMapping(value = "me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Image> updateUserImage(@RequestPart  MultipartFile image)  throws IOException {
        imageService.updateUserAvatar(image, SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        return ResponseEntity.ok().build();
    }
}