package com.example.server.entity.user;

import com.example.server.entity.Enrollment;
import com.example.server.entity.Faculty;
import com.example.server.factory.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("STUDENT")
@NoArgsConstructor
@Getter
@Setter
public class Student extends User {
    @ManyToOne
    @JoinColumn(name = "faculty_Id")
    private Faculty faculty;

    private long studentNumber;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

    public Student(String name, String email, String password, Long studentNumber, Faculty faculty) {
        super(name, email, password, UserType.STUDENT);
        this.faculty = faculty;
        this.studentNumber = studentNumber;
    }
}


