package com.example.server.controllerTest;

import com.example.server.entity.Faculty;
import com.example.server.repository.FacultyRepository;
import com.example.server.service.FacultyService;
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
class FacultyTest {

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private FacultyService facultyService;

    private Faculty faculty;

    @BeforeEach
    void setUp() {
        faculty = new Faculty("Engineering");
        faculty.setId(1L);
    }

    @Test
    void createFaculty_ShouldSaveAndReturnFaculty() {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        Faculty savedFaculty = facultyService.createFaculty("Engineering");

        assertNotNull(savedFaculty);
        assertEquals("Engineering", savedFaculty.getName());

        verify(facultyRepository, times(1)).save(any(Faculty.class));
    }

    @Test
    void getAllFaculties_ShouldReturnFacultyList() {
        when(facultyRepository.findAll()).thenReturn(List.of(faculty));

        List<Faculty> faculties = facultyService.getAllFaculties();

        assertEquals(1, faculties.size());
        assertEquals("Engineering", faculties.get(0).getName());

        verify(facultyRepository, times(1)).findAll();
    }

    @Test
    void getFacultyById_ShouldReturnFaculty_WhenExists() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        Faculty foundFaculty = facultyService.getFacultyById(1L);

        assertNotNull(foundFaculty);
        assertEquals(faculty.getId(), foundFaculty.getId());
        assertEquals(faculty.getName(), foundFaculty.getName());

        verify(facultyRepository, times(1)).findById(1L);
    }

    @Test
    void getFacultyById_ShouldThrowException_WhenNotFound() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> facultyService.getFacultyById(1L));

        assertEquals("Faculty not found", exception.getMessage());
        verify(facultyRepository, times(1)).findById(1L);
    }

    @Test
    void updateFaculty_ShouldUpdateFacultyDetails() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        Faculty updatedFaculty = facultyService.updateFaculty(1L, "Science");

        assertNotNull(updatedFaculty);
        assertEquals("Science", updatedFaculty.getName());

        verify(facultyRepository, times(1)).findById(1L);
        verify(facultyRepository, times(1)).save(any(Faculty.class));
    }

    @Test
    void deleteFaculty_ShouldDeleteFaculty_WhenExists() {
        doNothing().when(facultyRepository).deleteById(1L);

        assertDoesNotThrow(() -> facultyService.deleteFaculty(1L));

        verify(facultyRepository, times(1)).deleteById(1L);
    }
}
