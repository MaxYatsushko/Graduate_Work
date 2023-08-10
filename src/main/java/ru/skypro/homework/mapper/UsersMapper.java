package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.model.User;

public class UsersMapper {

    /**
     * creates userDto from user
     * @param user - exist entity
     * @return userDto - created dto
     */
    public static UserDto createUserDtoFromUser(User user){

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

    /**
     * creates updateUserDto from user
     * @param user - exist entity
     * @return updateUserDto - created dto
     */
    public static UpdateUserDto createUpdateUserDtoFromUser(User user){

        return new UpdateUserDto(
                user.getFirstName(),
                user.getLastName(),
                user.getPhone()
        );
    }

    /**
     * updates entity user by updateUserDto
     * @param user - exist entity
     * @param userUpdateDto - dto
     * @return user - updated entity user
     */
    public static User updateUserFromUpdateUserDto (User user, UpdateUserDto userUpdateDto){

        user.setFirstName(userUpdateDto.getFirstName().toUpperCase());
        user.setLastName(userUpdateDto.getLastName().toUpperCase());
        user.setPhone(user.getPhone());

        return user;
    }
}
