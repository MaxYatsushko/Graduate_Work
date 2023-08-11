package ru.skypro.homework.service;

import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.model.Role;

public interface AuthService {

    boolean login(String userName, String password);
    boolean register(RegisterDto register, Role role);
    boolean changeUserPassword(String name, NewPasswordDto newPassword);
}
