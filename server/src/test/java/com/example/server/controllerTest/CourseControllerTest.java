package com.example.server.controllerTest;

import com.example.server.controller.CourseController;
import com.example.server.dto.CourseDTO;
import com.example.server.entity.Course;
import com.example.server.entity.Faculty;
import com.example.server.entity.user.Teacher;
import com.example.server.service.CourseService;
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
class CourseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CourseService courseService;

    private Course course;
    private Faculty faculty;
    private Teacher teacher;

    @InjectMocks
    private CourseController courseController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();

        faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Faculty Name");
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Teacher Name");

        course = new Course("Software Engineering", faculty, teacher);
        course.setId(1L);
    }

    @Test
    void testCreateOrUpdateCourse() throws Exception {
        CourseDTO courseDTO = new CourseDTO(1L, "Software Engineering", 1L, 1L);

        Mockito.when(courseService.createOrUpdateCourse(any(CourseDTO.class))).thenReturn(course);

        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(courseDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(course.getId()))
                .andExpect(jsonPath("$.name").value("Software Engineering"))
                .andExpect(jsonPath("$.faculty.id").value(faculty.getId()))
                .andExpect(jsonPath("$.teacher.id").value(teacher.getId()));
    }



    @Test
    void testCreateOrUpdateCourse_BadRequest() throws Exception {
        Mockito.when(courseService.createOrUpdateCourse(any(CourseDTO.class))).thenThrow(RuntimeException.class);

        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Software Engineering\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteCourse() throws Exception {
        Mockito.doNothing().when(courseService).deleteCourse(1L);

        mockMvc.perform(delete("/courses/{courseId}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(courseService, Mockito.times(1)).deleteCourse(1L);
    }

    @Test
    void testDeleteCourse_NotFound() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(courseService).deleteCourse(2L);

        mockMvc.perform(delete("/courses/{courseId}", 2L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCoursesByTeacher() throws Exception {
        List<Course> courses = Arrays.asList(course);
        Mockito.when(courseService.getCoursesByTeacher(teacher.getId())).thenReturn(courses);

        mockMvc.perform(get("/courses/teacher/{teacherId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Software Engineering"));
    }

    @Test
    void testGetCoursesByTeacher_NotFound() throws Exception {
        Mockito.when(courseService.getCoursesByTeacher(2L)).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/courses/teacher/{teacherId}", 2L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCoursesByFaculty() throws Exception {
        List<Course> courses = Arrays.asList(course);
        Mockito.when(courseService.getCoursesByFaculty(faculty.getId())).thenReturn(courses);

        mockMvc.perform(get("/courses/faculty/{facultyId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Software Engineering"));
    }

    @Test
    void testGetCoursesByFaculty_NotFound() throws Exception {
        Mockito.when(courseService.getCoursesByFaculty(2L)).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/courses/faculty/{facultyId}", 2L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCourseById() throws Exception {
        Mockito.when(courseService.getCourseById(1L)).thenReturn(course);

        mockMvc.perform(get("/courses/{courseId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Software Engineering"));
    }

    @Test
    void testGetCourseById_NotFound() throws Exception {
        Mockito.when(courseService.getCourseById(2L)).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/courses/{courseId}", 2L))
                .andExpect(status().isNotFound());
    }
}
