package com.example.server.serviceTest;

import com.example.server.dto.EnrollmentDTO;
import com.example.server.entity.Course;
import com.example.server.entity.Enrollment;
import com.example.server.entity.user.Student;
import com.example.server.entity.user.User;
import com.example.server.repository.CourseRepository;
import com.example.server.repository.EnrollmentRepository;
import com.example.server.repository.UserRepository;
import com.example.server.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment enrollment;
    private Course course;
    private Student student;
    private EnrollmentDTO enrollmentDTO;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(1L);

        student = new Student();
        student.setId(1L);

        enrollment = new Enrollment(course, student);
        enrollmentDTO = new EnrollmentDTO();
        enrollmentDTO.setCourseId(1L);
        enrollmentDTO.setStudentId(1L);
    }

    @Test
    void createEnrollment_ValidData_ReturnsEnrollment() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        Enrollment savedEnrollment = enrollmentService.createEnrollment(enrollmentDTO);

        assertNotNull(savedEnrollment);
        assertEquals(course, savedEnrollment.getCourse());
        assertEquals(student, savedEnrollment.getStudent());

        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void createEnrollment_InvalidStudent_ThrowsException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mock(User.class))); // Mock a non-student user

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                enrollmentService.createEnrollment(enrollmentDTO));

        assertEquals("User is not Student", exception.getMessage());
    }

    @Test
    void createEnrollment_InvalidCourseOrStudent_ThrowsException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                enrollmentService.createEnrollment(enrollmentDTO));

        assertEquals("Invalid course or student ID", exception.getMessage());
    }

    @Test
    void getAllEnrollments_ReturnsList() {
        when(enrollmentRepository.findAll()).thenReturn(List.of(enrollment));

        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();

        assertEquals(1, enrollments.size());
        assertEquals(enrollment, enrollments.get(0));
    }

    @Test
    void getEnrollmentsByStudent_ReturnsList() {
        when(enrollmentRepository.findByStudentId(1L)).thenReturn(List.of(enrollment));

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(1L);

        assertEquals(1, enrollments.size());
        assertEquals(enrollment, enrollments.get(0));
    }

    @Test
    void getEnrollmentsByCourse_ReturnsList() {
        when(enrollmentRepository.findByCourseId(1L)).thenReturn(List.of(enrollment));

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(1L);

        assertEquals(1, enrollments.size());
        assertEquals(enrollment, enrollments.get(0));
    }

    @Test
    void deleteEnrollment_Success() {
        doNothing().when(enrollmentRepository).deleteById(1L);

        enrollmentService.deleteEnrollment(1L);

        verify(enrollmentRepository, times(1)).deleteById(1L);
    }
}
