package com.example.server.controllerTest;

import com.example.server.dto.UserDTO;
import com.example.server.entity.Faculty;
import com.example.server.entity.User;
import com.example.server.factory.UserFactory;
import com.example.server.repository.FacultyRepository;
import com.example.server.repository.UserRepository;
import com.example.server.factory.UserType;
import com.example.server.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private UserService userService;


    private User user;
    private UserDTO userDTO;
    private Faculty faculty;

    @BeforeEach
    void setUp() {
        faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Engineering");

        userDTO = new UserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("john@example.com");
        userDTO.setPassword("password");
        userDTO.setRole(UserType.STUDENT);
        userDTO.setStudentNumber(123456L);
        userDTO.setFacultyId("1");

        user = UserFactory.createUser(userDTO, faculty);
        user.setId(1L);
    }

    @Test
    void registerUser_ShouldSaveAndReturnUser() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO savedUser = userService.registerUser(userDTO);

        assertNotNull(savedUser);
        assertEquals(userDTO.getName(), savedUser.getName());
        assertEquals(userDTO.getEmail(), savedUser.getEmail());
        assertEquals(userDTO.getRole(), savedUser.getRole());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getName(), foundUser.getName());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_ShouldThrowException_WhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> userService.getUserById(1L));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getAllUsers_ShouldReturnUserList() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDTO> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(user.getName(), users.get(0).getName());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void updateUser_ShouldUpdateUserDetails() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO updatedRequest = new UserDTO();
        updatedRequest.setName("Jane Doe");
        updatedRequest.setEmail("jane@example.com");
        updatedRequest.setPassword("newpassword");
        updatedRequest.setRole(UserType.TEACHER);

        UserDTO updatedUser = userService.updateUser(1L, updatedRequest);

        assertNotNull(updatedUser);
        assertEquals(updatedRequest.getName(), updatedUser.getName());
        assertEquals(updatedRequest.getEmail(), updatedUser.getEmail());
        assertEquals(updatedRequest.getRole(), updatedUser.getRole());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> userService.deleteUser(1L));

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).existsById(1L);
    }
}
