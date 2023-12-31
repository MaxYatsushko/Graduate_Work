package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.service.AuthService;
import static ru.skypro.homework.model.Role.USER;


@CrossOrigin(value = "http://localhost:3000")
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login", tags = "Authorization",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK"),
                    @ApiResponse(
                            responseCode = "401", description = "Unauthorized")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto login) {

        if (authService.login(login.getUsername(), login.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Register", tags = "Registration",
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Created"),
                    @ApiResponse(
                            responseCode = "400", description = "Bad Request")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto register) {

        Role role = register.getRole() == null ? USER : register.getRole();
        if (authService.register(register, role)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
