package com.example.server.factory;

import com.example.server.dto.UserDTO;
import com.example.server.entity.Student;
import com.example.server.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    public static User createUser(UserDTO userDTO) {
        UserType role = userDTO.getRole();
        switch (role) {
            case STUDENT:
                return new Student(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword());
            // Handle other roles as needed
            default:
                throw new IllegalArgumentException("Invalid role type: " + role);
        }
    }

}
