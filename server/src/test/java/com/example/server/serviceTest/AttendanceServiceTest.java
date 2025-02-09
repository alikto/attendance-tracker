package com.example.server.serviceTest;

import com.example.server.dto.AttendanceDTO;
import com.example.server.entity.Attendance;
import com.example.server.entity.Course;
import com.example.server.entity.user.Student;
import com.example.server.entity.user.Teacher;
import com.example.server.entity.user.User;
import com.example.server.mapper.AttendanceMapper;
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

        student = new Student();
        student.setId(1L);

        course = new Course();
        course.setId(101L);

        attendanceDTO = new AttendanceDTO(1L,1L, 101L, LocalDate.now(), true);

        attendance = new Attendance(student, course, LocalDate.now(), true);
        attendance.setId(1L);
    }

    @Test
    public void testRecordAttendance_Success() {
        when(userRepository.findById(attendanceDTO.getStudentId())).thenReturn(Optional.of(student));
        when(courseRepository.findById(attendanceDTO.getCourseId())).thenReturn(Optional.of(course));
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance);

        Attendance result = attendanceService.recordAttendance(attendanceDTO);

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
        when(userRepository.findById(attendanceDTO.getStudentId())).thenReturn(Optional.empty());
        when(courseRepository.findById(attendanceDTO.getCourseId())).thenReturn(Optional.of(course)); // Mock course lookup

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            attendanceService.recordAttendance(attendanceDTO);
        });

        System.out.println("GET MESSAGE"+thrown.getMessage());
        assertEquals("Invalid course or student ID", thrown.getMessage());

        verify(courseRepository, times(0)).findById(anyLong());
        verify(attendanceRepository, times(0)).save(any(Attendance.class));
    }

    @Test
    public void testRecordAttendance_InvalidCourse() {
        when(userRepository.findById(attendanceDTO.getStudentId())).thenReturn(Optional.of(student));
        when(courseRepository.findById(attendanceDTO.getCourseId())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            attendanceService.recordAttendance(attendanceDTO);
        });

        assertEquals("Invalid course or student ID", thrown.getMessage());

        verify(userRepository, times(1)).findById(attendanceDTO.getStudentId());
        verify(courseRepository, times(1)).findById(attendanceDTO.getCourseId());
        verify(attendanceRepository, times(0)).save(any(Attendance.class));
    }

    @Test
    public void testRecordAttendance_UserIsNotStudent() {
        User nonStudentUser = new Teacher();
        nonStudentUser.setId(1L);

        when(userRepository.findById(attendanceDTO.getStudentId())).thenReturn(Optional.of(nonStudentUser));
        when(courseRepository.findById(attendanceDTO.getCourseId())).thenReturn(Optional.of(course));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            attendanceService.recordAttendance(attendanceDTO);
        });

        assertEquals("User is not Student", thrown.getMessage());

        verify(userRepository, times(1)).findById(attendanceDTO.getStudentId());
        verify(courseRepository, times(1)).findById(attendanceDTO.getCourseId());
        verify(attendanceRepository, times(0)).save(any(Attendance.class));
    }

    @Test
    public void testGetAttendanceByStudent() {
        when(attendanceRepository.findByStudentId(1L)).thenReturn(Collections.singletonList(attendance));

        List<AttendanceDTO> result = attendanceService.getAttendanceByStudent(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        AttendanceDTO actual = result.get(0);
        assertEquals(attendanceDTO.getId(), actual.getId());
        assertEquals(attendanceDTO.getStudentId(), actual.getStudentId());
        assertEquals(attendanceDTO.getCourseId(), actual.getCourseId());
        assertEquals(attendanceDTO.getDate(), actual.getDate());
        assertEquals(attendanceDTO.isPresent(), actual.isPresent());

        verify(attendanceRepository, times(1)).findByStudentId(1L);
    }

    @Test
    public void testGetAttendanceByCourse() {
        when(attendanceRepository.findByCourseId(101L)).thenReturn(Collections.singletonList(attendance));

        List<AttendanceDTO> result = attendanceService.getAttendanceByCourse(101L);

        assertNotNull(result);
        assertEquals(1, result.size());
        AttendanceDTO actual = result.get(0);
        assertEquals(attendanceDTO.getId(), actual.getId());
        assertEquals(attendanceDTO.getStudentId(), actual.getStudentId());
        assertEquals(attendanceDTO.getCourseId(), actual.getCourseId());
        assertEquals(attendanceDTO.getDate(), actual.getDate());
        assertEquals(attendanceDTO.isPresent(), actual.isPresent());

        verify(attendanceRepository, times(1)).findByCourseId(101L);
    }


    @Test
    public void testDeleteAttendance() {
        doNothing().when(attendanceRepository).deleteById(1L);

        attendanceService.deleteAttendance(1L);

        verify(attendanceRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAttendance_NotFound() {
        doThrow(new EmptyResultDataAccessException(1)).when(attendanceRepository).deleteById(1L);

        EmptyResultDataAccessException thrown = assertThrows(EmptyResultDataAccessException.class, () -> {
            attendanceService.deleteAttendance(1L);
        });

        assertEquals("Incorrect result size: expected 1, actual 0", thrown.getMessage());

        verify(attendanceRepository, times(1)).deleteById(1L);
    }

}
