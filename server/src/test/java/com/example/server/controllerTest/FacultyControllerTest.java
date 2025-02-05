package com.example.server.controllerTest;

import com.example.server.controller.FacultyController;
import com.example.server.entity.Faculty;
import com.example.server.service.FacultyService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FacultyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FacultyService facultyService;

    private Faculty faculty;

    @InjectMocks
    private FacultyController facultyController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(facultyController).build();

        faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Engineering");
    }

    @Test
    void testCreateFaculty() throws Exception {
        Mockito.when(facultyService.createFaculty(any(String.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculties/create")
                        .param("name", "Engineering")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Engineering"));
    }

    @Test
    void testGetAllFaculties() throws Exception {
        List<Faculty> faculties = Arrays.asList(faculty);
        Mockito.when(facultyService.getAllFaculties()).thenReturn(faculties);

        mockMvc.perform(get("/faculties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Engineering"));
    }

    @Test
    void testGetFacultyById() throws Exception {
        Mockito.when(facultyService.getFacultyById(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculties/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Engineering"));
    }

    @Test
    void testGetFacultyById_NotFound() throws Exception {
        Mockito.when(facultyService.getFacultyById(2L)).thenReturn(null);

        mockMvc.perform(get("/faculties/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateFaculty() throws Exception {
        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(1L);
        updatedFaculty.setName("Science");

        Mockito.when(facultyService.updateFaculty(eq(1L), any(String.class))).thenReturn(updatedFaculty);

        mockMvc.perform(put("/faculties/1")
                        .param("name", "Science")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Science"));
    }

    @Test
    void testUpdateFaculty_NotFound() throws Exception {
        Mockito.when(facultyService.updateFaculty(eq(2L), any(String.class))).thenReturn(null);

        mockMvc.perform(put("/faculties/2")
                        .param("name", "Science")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteFaculty() throws Exception {
        mockMvc.perform(delete("/faculties/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(facultyService, Mockito.times(1)).deleteFaculty(1L);
    }
}
