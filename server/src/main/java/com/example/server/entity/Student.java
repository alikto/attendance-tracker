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
    @ManyToOne
    @JoinColumn(name = "facultyId")
    private Faculty faculty;

    private long studentNumber;

    public Student(String name, String email, String password, Long studentNumber, Faculty faculty) {
        super(name, email, password, UserType.STUDENT);
        this.faculty = faculty;
        this.studentNumber = studentNumber;
    }
}


