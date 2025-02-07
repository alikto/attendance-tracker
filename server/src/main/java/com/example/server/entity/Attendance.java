package com.example.server.entity;

import com.example.server.entity.user.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private boolean present;

    public Attendance(Student student, Course course, LocalDate date, boolean present) {
        this.student = student;
        this.course = course;
        this.date = date;
        this.present = present;
    }
}
