package com.example.server.factory;

import com.example.server.dto.UserDTO;
import com.example.server.entity.Faculty;
import com.example.server.entity.Student;
import com.example.server.entity.Teacher;
import com.example.server.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    public static User createUser(UserDTO userDTO, Faculty faculty) {
        UserType role = userDTO.getRole();
        return switch (role) {
            case STUDENT ->
                    new Student(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getStudentNumber(), faculty);
            case TEACHER -> new Teacher(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword(), faculty);
            default -> throw new IllegalArgumentException("Invalid role type: " + role);
        };
    }

}
