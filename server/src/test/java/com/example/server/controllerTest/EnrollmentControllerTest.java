package com.example.server.controllerTest;

import com.example.server.controller.EnrollmentController;
import com.example.server.dto.EnrollmentDTO;
import com.example.server.entity.Course;
import com.example.server.entity.Enrollment;
import com.example.server.entity.user.Student;
import com.example.server.service.EnrollmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EnrollmentService enrollmentService;

    private Enrollment enrollment;
    private Course course;
    private Student student;

    @InjectMocks
    private EnrollmentController enrollmentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(enrollmentController).build();

        course = new Course();
        course.setId(1L);
        course.setName("Software Engineering");

        student = new Student();
        student.setId(1L);
        student.setName("Student Name");

        enrollment = new Enrollment(course, student);
        enrollment.setId(1L);
    }

    @Test
    void testCreateEnrollment() throws Exception {
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO();
        enrollmentDTO.setCourseId(1L);
        enrollmentDTO.setStudentId(1L);

        Mockito.when(enrollmentService.createEnrollment(any(EnrollmentDTO.class))).thenReturn(enrollment);

        mockMvc.perform(post("/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(enrollmentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(enrollment.getId()))
                .andExpect(jsonPath("$.course.id").value(course.getId()))
                .andExpect(jsonPath("$.student.id").value(student.getId()));
    }

    @Test
    void testGetAllEnrollments() throws Exception {
        List<Enrollment> enrollments = Arrays.asList(enrollment);
        Mockito.when(enrollmentService.getAllEnrollments()).thenReturn(enrollments);

        mockMvc.perform(get("/enrollments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testGetEnrollmentsByStudent() throws Exception {
        List<Enrollment> enrollments = Arrays.asList(enrollment);
        Mockito.when(enrollmentService.getEnrollmentsByStudent(student.getId())).thenReturn(enrollments);

        mockMvc.perform(get("/enrollments/student/{studentId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testGetEnrollmentsByCourse() throws Exception {
        List<Enrollment> enrollments = Arrays.asList(enrollment);
        Mockito.when(enrollmentService.getEnrollmentsByCourse(course.getId())).thenReturn(enrollments);

        mockMvc.perform(get("/enrollments/course/{courseId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testDeleteEnrollment() throws Exception {
        Mockito.doNothing().when(enrollmentService).deleteEnrollment(1L);

        mockMvc.perform(delete("/enrollments/{enrollmentId}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(enrollmentService, Mockito.times(1)).deleteEnrollment(1L);
    }
}
