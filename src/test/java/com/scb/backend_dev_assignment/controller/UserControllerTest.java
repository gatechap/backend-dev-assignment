package com.scb.backend_dev_assignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scb.backend_dev_assignment.dto.UserDto;
import com.scb.backend_dev_assignment.exception.QueryNotFoundException;
import com.scb.backend_dev_assignment.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllUsers_ShouldReturnUserList() throws Exception {
        Long id = 1L;
        UserDto userDto = new UserDto(id, "Kakashi", "Hatake", null);
        List<UserDto> users = List.of(userDto);
        Mockito.when(userService.getUsers()).thenReturn(users);

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userDto.getId()))
                .andExpect(jsonPath("$[0].firstName").value(userDto.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(userDto.getLastName()));
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        Long id = 1L;
        UserDto userDto = new UserDto(id, "Kakashi", "Hatake", null);
        Mockito.when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value("Kakashi"))
                .andExpect(jsonPath("$.lastName").value("Hatake"));
    }

    @Test
    void createUser_ShouldReturnErrorValidation() throws Exception {
        UserDto userRequestDto = new UserDto(null, null, "Hatake", null);

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.firstName").value("must not be null"))
                .andExpect(jsonPath("$.errType").value("MethodArgumentNotValidException"));
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        Long id = 1L;
        UserDto userDto = new UserDto(id, "Kakashi", "Hatake", null);
        Mockito.when(userService.getUserById(id)).thenReturn(userDto);
        mockMvc.perform(get("/api/user/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.firstName").value(userDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDto.getLastName()));
    }

    @Test
    void getUserById_ShouldReturnQueryNotFoundException() throws Exception {
        Long id = 1L;
        String errMsg = "User not found with id: " + id;
        Mockito.when(userService.getUserById(id)).thenThrow(new QueryNotFoundException(errMsg));
        mockMvc.perform(get("/api/user/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errType").value("QueryNotFoundException"))
                .andExpect(jsonPath("$.errMsg").value(errMsg));
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/api/user/{id}", id))
                .andExpect(status().isNoContent());
        Mockito.verify(userService).deleteUserById(id);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        Long id = 1L;
        UserDto userDto = new UserDto(id, "Kakashi", "Hatake", "thai@land.com");
        Mockito.when(userService.updateUser(eq(id), any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(put("/api/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.firstName").value(userDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDto.getLastName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void patchUser_ShouldReturnPatchedUser() throws Exception {
        Long id = 1L;
        UserDto userDto = new UserDto(id, "Kakashi", "Hatake", null);
        Mockito.when(userService.patchUser(eq(id), any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(patch("/api/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.firstName").value(userDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDto.getLastName()));
    }

    @Test
    void getUserByAge_ShouldReturnUsersByAge() throws Exception {
        Long id = 1L;
        UserDto userDto = new UserDto(id, "Kakashi", "Hatake", null);
        List<UserDto> users = List.of(userDto);
        Mockito.when(userService.getUserByAge(30)).thenReturn(users);

        mockMvc.perform(get("/api/user/age/{age}", 30))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userDto.getId()))
                .andExpect(jsonPath("$[0].firstName").value(userDto.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(userDto.getLastName()));
    }

    @Test
    void getUserByAge_ShouldReturnInvalidDataAccessResourceUsageException() throws Exception {
        String errMsg = "Invalid statement";
        Mockito.when(userService.getUserByAge(anyInt())).thenThrow(new InvalidDataAccessResourceUsageException((errMsg)));

        mockMvc.perform(get("/api/user/age/{age}", anyInt()))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errType").value("InvalidDataAccessResourceUsageException"))
                .andExpect(jsonPath("$.errMsg").value(errMsg));
    }
}
