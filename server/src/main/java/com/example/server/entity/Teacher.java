package com.example.server.entity;

import com.example.server.factory.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("TEACHER")
@NoArgsConstructor
@Getter
@Setter
public class Teacher extends User {
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    public Teacher(String name, String email, String password, Faculty faculty) {
        super(name, email, password, UserType.TEACHER);
        this.faculty = faculty;
    }
}


