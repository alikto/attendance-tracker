package com.example.server.factory;

import com.example.server.dto.UserDTO;
import com.example.server.entity.*;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    public static User createUser(UserDTO userDTO, Faculty faculty) {
        UserType role = userDTO.getRole();
        return switch (role) {
            case STUDENT ->
                    new Student(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getStudentNumber(), faculty);
            case TEACHER -> new Teacher(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword(), faculty);
            case ADMIN -> new Admin(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword());
            default -> throw new IllegalArgumentException("Invalid role type: " + role);
        };
    }

}
