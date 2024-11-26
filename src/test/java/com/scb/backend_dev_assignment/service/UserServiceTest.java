package com.scb.backend_dev_assignment.service;

import com.scb.backend_dev_assignment.dto.UserDto;
import com.scb.backend_dev_assignment.entity.User;
import com.scb.backend_dev_assignment.exception.QueryNotFoundException;
import com.scb.backend_dev_assignment.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ShouldSaveAndReturnUser() {
        UserDto userDto = new UserDto(1L, "John", "Doe", "john.doe@example.com");
        User user = new User(1L, "John", "Doe", "john.doe@example.com");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto result = userService.createUser(userDto);

        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUsers_ShouldReturnListOfUserDtos() {
        List<User> users = Arrays.asList(
                new User(1L, "John", "Doe", "john.doe@example.com"),
                new User(2L, "Jane", "Doe", "jane.doe@example.com")
        );
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> result = userService.getUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ShouldReturnUserDto_WhenUserExists() {
        User user = new User(1L, "John", "Doe", "john.doe@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        QueryNotFoundException exception = assertThrows(QueryNotFoundException.class,
                () -> userService.getUserById(1L));

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void deleteUserById_ShouldCallRepositoryDeleteById() {
        userService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void patchUser_ShouldUpdatePartialFields01() {
        UserDto patchUserDto = new UserDto(null, "Johnny", null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User(1L, "John", "Doe", "john.doe@example.com")));
        when(userRepository.save(any(User.class))).thenReturn(new User(1L, "Johnny", "Doe", "john.doe@example.com"));

        UserDto result = userService.patchUser(1L, patchUserDto);

        assertEquals("Johnny", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void patchUser_ShouldUpdatePartialFields02() {
        UserDto existingUserDto = new UserDto(1L, "John", "Doe", "john.doe@example.com");
        UserDto patchUserDto = new UserDto(null, null, "Dum", "neo@evagalion.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User(1L, "John", "Doe", "john.doe@example.com")));
        when(userRepository.save(any(User.class))).thenReturn(new User(1L, "Johnny", "Dum", "neo@evagalion.com"));

        UserDto result = userService.patchUser(1L, patchUserDto);

        assertEquals("neo@evagalion.com", result.getEmail());
        assertEquals("Dum", result.getLastName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_ShouldReplaceAllFields() {
        UserDto existingUserDto = new UserDto(1L, "John", "Doe", "john.doe@example.com");
        UserDto updateUserDto = new UserDto(1L, "Johnny", "Smith", "johnny.smith@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User(1L, "John", "Doe", "john.doe@example.com")));
        when(userRepository.save(any(User.class))).thenReturn(new User(1L, "Johnny", "Smith", "johnny.smith@example.com"));

        UserDto result = userService.updateUser(1L, updateUserDto);

        assertEquals("Johnny", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("johnny.smith@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUserByAge_ShouldReturnUsersOfSpecifiedAge() {
        List<User> users = Arrays.asList(
                new User(1L, "John", "Doe", "john.doe@example.com"),
                new User(2L, "Jane", "Doe", "jane.doe@example.com")
        );
        when(userRepository.findByAge(25)).thenReturn(users);

        List<UserDto> result = userService.getUserByAge(25);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findByAge(25);
    }
}
