package com.example.server.service;

import com.example.server.dto.EnrollmentDTO;
import com.example.server.entity.Course;
import com.example.server.entity.Enrollment;
import com.example.server.entity.user.Student;
import com.example.server.entity.user.User;
import com.example.server.repository.CourseRepository;
import com.example.server.repository.EnrollmentRepository;
import com.example.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public Enrollment createEnrollment(EnrollmentDTO enrollmentDTO) {
        Optional<Course> courseOpt = courseRepository.findById(enrollmentDTO.getCourseId());
        Optional<User> studentOpt = userRepository.findById(enrollmentDTO.getStudentId());

        if (courseOpt.isEmpty() || studentOpt.isEmpty()) {
            throw new RuntimeException("Invalid course or student ID");
        }

        if (!(studentOpt.get() instanceof Student student)){
            throw new RuntimeException("User is not Student");
        }

        Enrollment enrollment = new Enrollment(courseOpt.get(), student);
        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    public void deleteEnrollment(Long enrollmentId) {
        enrollmentRepository.deleteById(enrollmentId);
    }
}
