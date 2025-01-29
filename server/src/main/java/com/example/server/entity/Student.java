package com.example.server.entity;

import com.example.server.factory.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("STUDENT")
@NoArgsConstructor
@Getter
@Setter
public class Student extends User {
    public Student(String name, String email, String password) {
        super(name, email, password, UserType.STUDENT);
    }
}


