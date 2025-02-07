package com.example.server.serviceTest;

import com.example.server.dto.AttendanceDTO;
import com.example.server.entity.Attendance;
import com.example.server.entity.Course;
import com.example.server.entity.user.Student;
import com.example.server.entity.user.Teacher;
import com.example.server.entity.user.User;
import com.example.server.repository.AttendanceRepository;
import com.example.server.repository.CourseRepository;
import com.example.server.repository.UserRepository;
import com.example.server.service.AttendanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    private AttendanceDTO attendanceDTO;
    private Attendance attendance;
    private Student student;
    private Course course;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize the test data
        student = new Student();
        student.setId(1L);

        course = new Course();
        course.setId(101L);

        attendanceDTO = new AttendanceDTO(1L, 101L, LocalDate.now(), true);

        attendance = new Attendance(student, course, LocalDate.now(), true);
    }

    @Test
    public void testRecordAttendance_Success() {
        // Mock the repositories
        when(userRepository.findById(attendanceDTO.getStudentId())).thenReturn(Optional.of(student));
        when(courseRepository.findById(attendanceDTO.getCourseId())).thenReturn(Optional.of(course));
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance);

        // Call the service method
        Attendance result = attendanceService.recordAttendance(attendanceDTO);

        // Assertions
        assertNotNull(result);
        assertEquals(student, result.getStudent());
        assertEquals(course, result.getCourse());
        assertEquals(attendanceDTO.getDate(), result.getDate());
        assertTrue(result.isPresent());

        verify(userRepository, times(1)).findById(attendanceDTO.getStudentId());
        verify(courseRepository, times(1)).findById(attendanceDTO.getCourseId());
        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    public void testRecordAttendance_InvalidStudent() {
        // Mock the repositories
        when(userRepository.findById(attendanceDTO.getStudentId())).thenReturn(Optional.empty());
        when(courseRepository.findById(attendanceDTO.getCourseId())).thenReturn(Optional.of(course)); // Mock course lookup

        // Call the service method and expect an exception
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            attendanceService.recordAttendance(attendanceDTO);
        });

        System.out.println("GET MESSAGE"+thrown.getMessage());
        assertEquals("Invalid course or student ID", thrown.getMessage());

        // Verify that courseRepository is not called when the student is invalid
        verify(courseRepository, times(0)).findById(anyLong());
        verify(attendanceRepository, times(0)).save(any(Attendance.class));
    }



    @Test
    public void testRecordAttendance_InvalidCourse() {
        // Mock the repositories
        when(userRepository.findById(attendanceDTO.getStudentId())).thenReturn(Optional.of(student));
        when(courseRepository.findById(attendanceDTO.getCourseId())).thenReturn(Optional.empty());

        // Call the service method and expect an exception
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            attendanceService.recordAttendance(attendanceDTO);
        });

        // Assertions
        assertEquals("Invalid course or student ID", thrown.getMessage());

        verify(userRepository, times(1)).findById(attendanceDTO.getStudentId());
        verify(courseRepository, times(1)).findById(attendanceDTO.getCourseId());
        verify(attendanceRepository, times(0)).save(any(Attendance.class));
    }

    @Test
    public void testRecordAttendance_UserIsNotStudent() {
        // Create a User that is not a Student
        User nonStudentUser = new Teacher();
        nonStudentUser.setId(1L);

        // Mock the repositories
        when(userRepository.findById(attendanceDTO.getStudentId())).thenReturn(Optional.of(nonStudentUser));
        when(courseRepository.findById(attendanceDTO.getCourseId())).thenReturn(Optional.of(course));

        // Call the service method and expect an exception
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            attendanceService.recordAttendance(attendanceDTO);
        });

        // Assertions
        assertEquals("User is not Student", thrown.getMessage());

        verify(userRepository, times(1)).findById(attendanceDTO.getStudentId());
        verify(courseRepository, times(1)).findById(attendanceDTO.getCourseId());
        verify(attendanceRepository, times(0)).save(any(Attendance.class));
    }

    @Test
    public void testGetAttendanceByStudent() {
        // Mock the repository
        when(attendanceRepository.findByStudentId(1L)).thenReturn(Collections.singletonList(attendance));

        // Call the service method
        List<Attendance> result = attendanceService.getAttendanceByStudent(1L);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(attendance, result.get(0));

        verify(attendanceRepository, times(1)).findByStudentId(1L);
    }

    @Test
    public void testGetAttendanceByCourse() {
        // Mock the repository
        when(attendanceRepository.findByCourseId(101L)).thenReturn(Collections.singletonList(attendance));

        // Call the service method
        List<Attendance> result = attendanceService.getAttendanceByCourse(101L);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(attendance, result.get(0));

        verify(attendanceRepository, times(1)).findByCourseId(101L);
    }

    @Test
    public void testDeleteAttendance() {
        // Mock the repository
        doNothing().when(attendanceRepository).deleteById(1L);

        // Call the service method
        attendanceService.deleteAttendance(1L);

        // Assertions
        verify(attendanceRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAttendance_NotFound() {
        // Mock the repository to throw EmptyResultDataAccessException
        doThrow(new EmptyResultDataAccessException(1)).when(attendanceRepository).deleteById(1L);

        // Call the service method and expect an exception
        EmptyResultDataAccessException thrown = assertThrows(EmptyResultDataAccessException.class, () -> {
            attendanceService.deleteAttendance(1L);
        });

        // Assertions
        assertEquals("Incorrect result size: expected 1, actual 0", thrown.getMessage());

        verify(attendanceRepository, times(1)).deleteById(1L);
    }

}
