package com.scb.backend_dev_assignment.service;

import com.scb.backend_dev_assignment.dto.UserDto;
import com.scb.backend_dev_assignment.entity.User;
import com.scb.backend_dev_assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserDto convertToDto(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    private List<UserDto> convertToDto(List<User> users) {
        List<UserDto> usersDto = new ArrayList<>();
        users.forEach(user -> usersDto.add(convertToDto(user)));
        return usersDto;
    }

    private User convertToEntity(UserDto userDto) {
        return new User(userDto.getId(), userDto.getFirstName(), userDto.getLastName(), userDto.getEmail());
    }

    private List<User> convertToEntity(List<UserDto> usersDto) {
        List<User> users = new ArrayList<>();
        usersDto.forEach(userDto -> users.add(convertToEntity(userDto)));
        return users;
    }

    public List<UserDto> createUser(List<UserDto> usersDto) {
        List<User> users = convertToEntity(usersDto);
        return convertToDto(userRepository.saveAll(users));
    }

    public UserDto createUser(UserDto userDto) {
        User user = convertToEntity(userDto);
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    public List<UserDto> getUsers() {
        return convertToDto(userRepository.findAll());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDto(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}
