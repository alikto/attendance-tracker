package com.example.server.service;

import com.example.server.dto.UserDTO;
import com.example.server.entity.*;
import com.example.server.entity.user.Student;
import com.example.server.entity.user.Teacher;
import com.example.server.entity.user.User;
import com.example.server.factory.UserFactory;
import com.example.server.factory.UserType;
import com.example.server.mapper.StudentMapper;
import com.example.server.mapper.TeacherMapper;
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
        if (dto.getRole() != UserType.ADMIN && dto.getFacultyId() == null) {
            throw new IllegalArgumentException("Faculty must be provided for STUDENT and TEACHER roles");
        }

        Faculty faculty = null;
        if (dto.getRole() != UserType.ADMIN) {
            faculty = facultyRepository.findById(Long.parseLong(dto.getFacultyId()))
                    .orElseThrow(() -> new IllegalArgumentException("Faculty not found"));
        }

        User user = UserFactory.createUser(dto, faculty);
        userRepository.save(user);

        return mapUserToDto(user);
    }



    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapUserToDto(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapUserToDto) // Use dynamic mapping based on type
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(Long id, UserDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        //user.setRole(request.getRole());

        if (user instanceof Student student) {
            if (request.getStudentNumber() != null) {
                student.setStudentNumber(request.getStudentNumber());
            }
            if (request.getFacultyId() != null) {
                Faculty faculty = facultyRepository.findById(Long.parseLong(request.getFacultyId()))
                        .orElseThrow(() -> new RuntimeException("Faculty not found"));
                student.setFaculty(faculty);
            }
        } else if (user instanceof Teacher teacher) {
            if (request.getFacultyId() != null) {
                Faculty faculty = facultyRepository.findById(Long.parseLong(request.getFacultyId()))
                        .orElseThrow(() -> new RuntimeException("Faculty not found"));
                teacher.setFaculty(faculty);
            }
        }

        user = userRepository.save(user);
        return mapUserToDto(user);
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

    private UserDTO mapUserToDto(User user) {
        if (user instanceof Student) {
            return StudentMapper.INSTANCE.studentToDto((Student) user);
        } else if (user instanceof Teacher) {
            return TeacherMapper.INSTANCE.teacherToDto((Teacher) user);
        } else {
            return UserMapper.INSTANCE.userToDto(user);
        }
    }
}
