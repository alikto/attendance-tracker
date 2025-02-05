package com.example.server.repository;

import com.example.server.entity.Course;
import com.example.server.entity.Faculty;
import com.example.server.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByTeacher(Teacher teacher);

    List<Course> findByFaculty(Faculty faculty);

    List<Course> findByNameContainingIgnoreCase(String courseName);
}
