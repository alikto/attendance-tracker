package com.example.server.controllerTest;

import com.example.server.controller.UserController;
import com.example.server.dto.UserDTO;
import com.example.server.factory.UserType;
import com.example.server.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    private UserDTO userDTO;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("John Doe");
        userDTO.setEmail("john@example.com");
        userDTO.setStudentNumber(2200356816L);
        userDTO.setRole(UserType.STUDENT);
        userDTO.setFacultyId("123456");
    }

    @Test
    void testRegisterUser() throws Exception {
        Mockito.when(userService.registerUser(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.studentNumber").value(2200356816L))
                .andExpect(jsonPath("$.role").value("STUDENT"))
                .andExpect(jsonPath("$.facultyId").value("123456"));
    }

    @Test
    void testGetUserById() throws Exception {
        Mockito.when(userService.getUserById(anyLong())).thenReturn(userDTO);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.studentNumber").value(2200356816L))
                .andExpect(jsonPath("$.role").value("STUDENT"))
                .andExpect(jsonPath("$.facultyId").value("123456"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<UserDTO> users = Arrays.asList(userDTO);
        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testUpdateUser() throws Exception {
        userDTO.setFacultyId("789456");
        Mockito.when(userService.updateUser(anyLong(), any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.facultyId").value("789456"));
    }

    @Test
    void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}
