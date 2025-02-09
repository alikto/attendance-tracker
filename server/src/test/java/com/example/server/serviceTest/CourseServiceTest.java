package com.example.server.serviceTest;

import com.example.server.dto.CourseDTO;
import com.example.server.entity.Course;
import com.example.server.entity.Faculty;
import com.example.server.entity.user.Teacher;
import com.example.server.repository.CourseRepository;
import com.example.server.repository.FacultyRepository;
import com.example.server.repository.UserRepository;
import com.example.server.service.CourseService;
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
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private Faculty faculty;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        faculty = new Faculty("Engineering");
        faculty.setId(1L);

        teacher = new Teacher("John Doe", "example@email.com", "password", faculty);
        teacher.setId(1L);


        course = new Course("Math 101", faculty, teacher);
        course.setId(1L);
    }

    @Test
    void createOrUpdateCourse_ShouldSaveCourse() {
        CourseDTO courseDTO = new CourseDTO(1L, "Math 101",  1L, 1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course savedCourse = courseService.createOrUpdateCourse(courseDTO);

        assertNotNull(savedCourse);
        assertEquals("Math 101", savedCourse.getName());
        assertEquals(teacher, savedCourse.getTeacher());
        assertEquals(faculty, savedCourse.getFaculty());

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void createOrUpdateCourse_ShouldThrowException_WhenTeacherNotFound() {
        CourseDTO courseDTO = new CourseDTO(1L,"Math 101", 1L, 1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> courseService.createOrUpdateCourse(courseDTO));

        assertEquals("Teacher with ID 1 not found.", exception.getMessage());
        verify(courseRepository, times(0)).save(any(Course.class));
    }

    @Test
    void deleteCourse_ShouldDeleteCourse_WhenExists() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).delete(any(Course.class));

        assertDoesNotThrow(() -> courseService.deleteCourse(1L));

        verify(courseRepository, times(1)).delete(any(Course.class));
    }

    @Test
    void deleteCourse_ShouldThrowException_WhenNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> courseService.deleteCourse(1L));

        assertEquals("Course with ID 1 not found.", exception.getMessage());
        verify(courseRepository, times(0)).delete(any(Course.class));
    }

    @Test
    void getCoursesByTeacher_ShouldReturnCourses() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseRepository.findByTeacher(teacher)).thenReturn(List.of(course));

        List<Course> courses = courseService.getCoursesByTeacher(1L);

        assertNotNull(courses);
        assertEquals(1, courses.size());
        assertEquals(course.getName(), courses.get(0).getName());

        verify(courseRepository, times(1)).findByTeacher(teacher);
    }

    @Test
    void getCoursesByTeacher_ShouldThrowException_WhenTeacherNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> courseService.getCoursesByTeacher(1L));

        assertEquals("Teacher with ID 1 not found.", exception.getMessage());
        verify(courseRepository, times(0)).findByTeacher(any(Teacher.class));
    }

    @Test
    void getCoursesByFaculty_ShouldReturnCourses() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        when(courseRepository.findByFaculty(faculty)).thenReturn(List.of(course));

        List<Course> courses = courseService.getCoursesByFaculty(1L);

        assertNotNull(courses);
        assertEquals(1, courses.size());
        assertEquals(course.getName(), courses.get(0).getName());

        verify(courseRepository, times(1)).findByFaculty(faculty);
    }

    @Test
    void getCoursesByFaculty_ShouldThrowException_WhenFacultyNotFound() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> courseService.getCoursesByFaculty(1L));

        assertEquals("Faculty not found.", exception.getMessage());
        verify(courseRepository, times(0)).findByFaculty(any(Faculty.class));
    }

    @Test
    void getCourseById_ShouldReturnCourse_WhenExists() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course foundCourse = courseService.getCourseById(1L);

        assertNotNull(foundCourse);
        assertEquals(course.getId(), foundCourse.getId());
        assertEquals(course.getName(), foundCourse.getName());

        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void getCourseById_ShouldThrowException_WhenNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> courseService.getCourseById(1L));

        assertEquals("Course not found.", exception.getMessage());
        verify(courseRepository, times(1)).findById(1L);
    }
}
