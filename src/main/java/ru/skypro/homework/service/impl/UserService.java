package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.mapper.UsersMapper;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    /**
     * saves user after registration via registerDto
     * @return void
     */
    public void registerUser(RegisterDto registerDto, String encodedPassword, Role role) {

        userRepository.save(
                new User(
                        registerDto.getUsername(),
                        registerDto.getFirstName().toUpperCase(),
                        registerDto.getLastName().toUpperCase(),
                        encodedPassword,
                        registerDto.getPhone(),
                        LocalDate.now(),
                        null,
                        role));
    }

    /**
     * gets list of details of users which exist in db
     * @return List'<'UserDetails'>'
     */
    public List<UserDetails> getUserDetails() {

        List<UserDetails> result = new ArrayList<>();
        userRepository.findAll()
                .forEach(user -> result.add(org.springframework.security.core.userdetails.User.builder()
                                .username(user.getEmail())
                                .password(user.getPassword())
                                .roles(user.getRole().name())
                                .build()
                        )
                );

        return result;
    }

    /**
     * updates user entity finding by login from web updateUserDto
     * @param updateUserDto - dto
     * @param login - string
     * @return Optional'<'UpdateUserDto'>' - created dto after updating user
     */
    public Optional<UpdateUserDto> updateUserInfo(UpdateUserDto updateUserDto, String login) {

        return userRepository.findByEmailIgnoreCase(login)
                .map(
                        user -> UsersMapper.createUpdateUserDtoFromUser(
                        userRepository.save(UsersMapper.updateUserFromUpdateUserDto(user, updateUserDto))
                        )
                );
    }

    public void updateUserImage(User user){
        userRepository.save(user);
    }

    /**
     * updates password of user by login from web
     * @param login - string
     * @param newPassword - string
     * @return Optional'<'User'>' - updated user if exists
     */
    public Optional<User> updatePassword(String login, String newPassword) {

        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(login);
        if (userOptional.isEmpty())
            return Optional.empty();

        User user = userOptional.get();
        user.setPassword(newPassword);
        return Optional.of(userRepository.save(user));
    }

    /**
     * gets userDto by login from web
     * @param login - string
     * @return Optional'<'UserDto'>' - created UserDto via finding user if exists
     */
    public Optional<UserDto> getUserDtoByLogin(String login){

        Optional<User> userOptional =  userRepository.findByEmailIgnoreCase(login);
        return userOptional.map(UsersMapper::createUserDtoFromUser);
    }

    /**
     * gets user by login from web
     * @param login - string
     * @return Optional'<'User'>' - finding user if exists
     */
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByEmailIgnoreCase(login);
    }
}