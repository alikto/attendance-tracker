package com.example.server.factory;

import com.example.server.entity.Student;
import com.example.server.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    public static User createUser(UserType role, String name, String email, String password) {
        switch (role) {
            case STUDENT:
                return new Student(name, email, password);
            default:
                throw new IllegalArgumentException("Invalid role type: " + role);
        }
    }
}
