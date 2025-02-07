package com.example.server.factory;

import com.example.server.dto.UserDTO;
import com.example.server.entity.*;
import com.example.server.entity.user.Admin;
import com.example.server.entity.user.Student;
import com.example.server.entity.user.Teacher;
import com.example.server.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    public static User createUser(UserDTO userDTO, Faculty faculty) {
        UserType role = userDTO.getRole();
        User user = switch (role) {
            case STUDENT -> new Student(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getStudentNumber(), faculty);
            case TEACHER -> new Teacher(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword(), faculty);
            case ADMIN -> new Admin(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword());
            default -> throw new IllegalArgumentException("Invalid role type: " + role);
        };
        System.out.println("Created user: " + user.getName());  // Log or breakpoint here
        return user;
    }

}
