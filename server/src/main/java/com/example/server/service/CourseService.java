package com.example.server.service;

import com.example.server.dto.CourseDTO;
import com.example.server.entity.Course;
import com.example.server.entity.Faculty;
import com.example.server.entity.user.Teacher;
import com.example.server.entity.user.User;
import com.example.server.repository.CourseRepository;
import com.example.server.repository.FacultyRepository;
import com.example.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final FacultyRepository facultyRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository, FacultyRepository facultyRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.facultyRepository = facultyRepository;
    }

    public Course createOrUpdateCourse(CourseDTO courseDTO) {
        Optional<User> teacherOptional = userRepository.findById(courseDTO.getTeacherId());
        Optional<Faculty> facultyOptional = facultyRepository.findById(courseDTO.getFacultyId());

        if (teacherOptional.isEmpty()) {
            throw new RuntimeException("Teacher not found.");
        }

        if (facultyOptional.isEmpty()) {
            throw new RuntimeException("Faculty not found.");
        }

        if (!(teacherOptional.get() instanceof Teacher teacher)) {
            throw new RuntimeException("User is not a Teacher.");
        }

        Faculty faculty = facultyOptional.get();

        Course course = new Course(courseDTO.getCourseName(), faculty, teacher);
        return courseRepository.save(course);
    }

    public void deleteCourse(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            throw new RuntimeException("Course not found.");
        }
        courseRepository.delete(courseOptional.get());
    }

    public List<Course> getCoursesByTeacher(Long teacherId) {
        Optional<User> teacherOptional = userRepository.findById(teacherId);

        if (teacherOptional.isEmpty()) {
            throw new RuntimeException("Teacher not found.");
        }
        if (!(teacherOptional.get() instanceof Teacher)) {
            throw new RuntimeException("User is not a Teacher.");
        }
        return courseRepository.findByTeacher((Teacher) teacherOptional.get());
    }

    public List<Course> getCoursesByFaculty(Long facultyId) {
        Optional<Faculty> facultyOptional = facultyRepository.findById(facultyId);
        if (facultyOptional.isEmpty()) {
            throw new RuntimeException("Faculty not found.");
        }
        return courseRepository.findByFaculty(facultyOptional.get());
    }

    public List<Course> getCoursesByName(String courseName) {
        return courseRepository.findByNameContainingIgnoreCase(courseName);
    }

    public Course getCourseById(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            throw new RuntimeException("Course not found.");
        }
        return courseOptional.get();
    }
}
