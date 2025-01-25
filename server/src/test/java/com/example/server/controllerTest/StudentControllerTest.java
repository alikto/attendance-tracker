package com.example.server.controllerTest;

import com.example.server.controller.StudentController;
import com.example.server.entity.Student;
import com.example.server.exception.ResourceNotFoundException;
import com.example.server.service.StudentService;
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
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private StudentController studentController;

    @Mock
    private StudentService studentService;


    private ObjectMapper objectMapper = new ObjectMapper();
    private Student student;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
        student = new Student();
        student.setId(1L);
        student.setName("John Doe");
        student.setEmail("johndoe@example.com");
    }

    @Test
    void shouldGetAllStudents() throws Exception {
        Mockito.when(studentService.getAllStudents())
                .thenReturn(Arrays.asList(student));

        mockMvc.perform(get("/api/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(student.getName())))
                .andExpect(jsonPath("$[0].email", is(student.getEmail())));
    }

    @Test
    void shouldGetStudentById() throws Exception {
        Mockito.when(studentService.getStudentById(1L))
                .thenReturn(student);

        mockMvc.perform(get("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(student.getName())))
                .andExpect(jsonPath("$.email", is(student.getEmail())));
    }

    @Test
    void shouldReturnNotFoundForGetStudentById() throws Exception {
        Mockito.when(studentService.getStudentById(anyLong()))
                .thenThrow(new ResourceNotFoundException("Student not found"));

        mockMvc.perform(get("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateStudent() throws Exception {
        Mockito.when(studentService.createStudent(any(Student.class)))
                .thenReturn(student);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(student.getName())))
                .andExpect(jsonPath("$.email", is(student.getEmail())));
    }

    @Test
    void shouldUpdateStudent() throws Exception {
        Mockito.when(studentService.updateStudent(anyLong(), any(Student.class)))
                .thenReturn(student);

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(student.getName())))
                .andExpect(jsonPath("$.email", is(student.getEmail())));
    }

    @Test
    void shouldReturnNotFoundForUpdateStudent() throws Exception {
        Mockito.when(studentService.updateStudent(anyLong(), any(Student.class)))
                .thenThrow(new ResourceNotFoundException("Student not found"));

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteStudent() throws Exception {
        doNothing().when(studentService).deleteStudent(anyLong());

        mockMvc.perform(delete("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundForDeleteStudent() throws Exception {
        doThrow(new ResourceNotFoundException("Student not found"))
                .when(studentService).deleteStudent(anyLong());

        mockMvc.perform(delete("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
