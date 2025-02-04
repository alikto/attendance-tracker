package com.example.server.service;

import com.example.server.dto.UserDTO;
import com.example.server.entity.Faculty;
import com.example.server.entity.Student;
import com.example.server.entity.Teacher;
import com.example.server.entity.User;
import com.example.server.factory.UserFactory;
import com.example.server.mapper.UserMapper;
import com.example.server.repository.FacultyRepository;
import com.example.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final FacultyRepository facultyRepository;

    @Autowired
    public UserService(UserRepository userRepository, FacultyRepository facultyRepository) {
        this.userRepository = userRepository;
        this.facultyRepository = facultyRepository;
    }

    public UserDTO registerUser(UserDTO dto) {

        Faculty faculty = facultyRepository.findById(Long.parseLong(dto.getFacultyId()))
                .orElseThrow(() -> new IllegalArgumentException("Faculty not found"));

        User user = UserFactory.createUser(dto, faculty);
        userRepository.save(user);
        UserDTO userDTOResult = UserMapper.INSTANCE.userToDto(user);

        if (user instanceof Student) {
            userDTOResult.setFacultyId(((Student) user).getFaculty().getId().toString());
            userDTOResult.setStudentNumber(((Student) user).getStudentNumber());
        } else if (user instanceof Teacher) {
            userDTOResult.setFacultyId(((Teacher) user).getFaculty().getId().toString());
        }

        // Save the user entity


        return userDTOResult;
    }


    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDto(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(Long id, UserDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        user = userRepository.save(user);
        return mapToDto(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private UserDTO mapToDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setPassword("********"); // Mask password for security
        return dto;
    }


}
