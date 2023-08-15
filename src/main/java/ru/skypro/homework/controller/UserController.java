package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.impl.ImageService;
import ru.skypro.homework.service.impl.UserService;
import java.io.IOException;


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

    @Operation(summary = "Set password of user", tags = "Users",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK"),
                    @ApiResponse(
                            responseCode = "403", description = "Forbidden")
            }
    )
    @PostMapping("set_password")
    public ResponseEntity<?> setPassword(@RequestBody NewPasswordDto newPassword) {

        if (authService.changeUserPassword(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName(), newPassword))
            return new ResponseEntity<>(HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @Operation(summary = "Get user", tags = "Users",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))}),
                    @ApiResponse(
                            responseCode = "401", description = "Unauthorized")
            }
    )
    @GetMapping("me")
    public ResponseEntity<UserDto> getUser() {

        return userService.getUserDtoByLogin(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName()
                )
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @Operation(summary = "Update user", tags = "Users",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UpdateUserDto.class))}),
                    @ApiResponse(
                            responseCode = "401", description = "Unauthorized")
            }
    )
    @PatchMapping("me")
    public ResponseEntity<UpdateUserDto> updateUser(@RequestBody UpdateUserDto userUpdateDto) {

       return userService.updateUserInfo(
                userUpdateDto,
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
               .map(ResponseEntity::ok)
               .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT)
                       .build());
    }

    @Operation(summary = "Update avatar of user", tags = "Users",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK"),
                    @ApiResponse(
                            responseCode = "401", description = "Unauthorized")
            }
    )
    @PatchMapping(value = "me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Image> updateUserImage(@RequestPart  MultipartFile image)  throws IOException {

        imageService.updateUserAvatar(image, SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());

        return ResponseEntity.ok().build();
    }
}