package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.model.User;

public class UsersMapper {
    public static UserDto userToUserGet(User user){
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getRole(),
                user.getImage() != null? ("/images/" + user.getImage().getFileName()):null
        );
    }

    public static UpdateUserDto userToUpdateUser(User user){
        return new UpdateUserDto(
                user.getFirstName(),
                user.getLastName(),
                user.getPhone()
        );
    }

    public static User userUpdateToUser(User user, UpdateUserDto userUpdateDto){
        user.setFirstName(userUpdateDto.getFirstName().toUpperCase());
        user.setLastName(userUpdateDto.getLastName().toUpperCase());
        user.setPhone(user.getPhone());
        return user;
    }
}
