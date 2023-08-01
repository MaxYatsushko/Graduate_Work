package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UserUpdateDto;
import ru.skypro.homework.model.Image;
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

    public void registerUser(Register register, String encodedPassword, Role role) {
        userRepository.save(
                new User(
                        register.getUsername().toUpperCase(),
                        register.getFirstName().toUpperCase(),
                        register.getLastName().toUpperCase(),
                        encodedPassword,
                        register.getPhone(),
                        LocalDate.now(),
                        null,
                        role.name()));
    }

    public List<UserDetails> getUserDetails() {
        List<UserDetails> result = new ArrayList<>();
        userRepository.findAll()
                .forEach(u -> result.add(org.springframework.security.core.userdetails.User.builder()
                                .username(u.getEmail())
                                .password(u.getPassword())
                                .roles("USER")
                                .build()
                        )
                );
        return result;
    }

    public Optional<User> updateUserInfo(UserUpdateDto userUpdate, String login) {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(login);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFirstName(userUpdate.getFirstName().toUpperCase());
            user.setLastName(userUpdate.getLastName().toUpperCase());
            user.setPhone(userUpdate.getPhone());
            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();
    }

//    public boolean checkOldPassword(String login, String currentPassword) {
//        return getUserByLogin(login).getPassword().equals(currentPassword);
//    }

    public Optional<User> updatePassword(String login, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(login);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(newPassword);
            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();
    }

    public Optional<Image> getUserImage(String login) {
        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(login);
        return userOptional.map(User::getImage);
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByEmailIgnoreCase(login);
    }
}