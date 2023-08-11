package ru.skypro.homework.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skypro.homework.model.Role;

@Data
@RequiredArgsConstructor
public class UserDto {

    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private String image;

    public UserDto(int id, String email, String firstName, String lastName, String phone, Role role, String image) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.image = image;
    }
}