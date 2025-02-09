package com.example.server.controllerTest;

import com.example.server.controller.AttendanceController;
import com.example.server.dto.AttendanceDTO;
import com.example.server.entity.Attendance;
import com.example.server.mapper.AttendanceMapper;
import com.example.server.service.AttendanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AttendanceControllerTest {

    @Mock
    private AttendanceService attendanceService;

    @InjectMocks
    private AttendanceController attendanceController;

    private AttendanceDTO attendanceDTO;
    private Attendance attendance;

    @BeforeEach
    public void setUp() {
        attendanceDTO = new AttendanceDTO(1L,1L, 101L, LocalDate.now(), true);
        attendance = new Attendance(null, null, LocalDate.now(), true);
    }

    @Test
    public void testRecordAttendance_Success() {
        when(attendanceService.recordAttendance(attendanceDTO)).thenReturn(attendance);

        ResponseEntity<Attendance> response = attendanceController.createAttendance(attendanceDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(attendanceService, times(1)).recordAttendance(attendanceDTO);
    }

    @Test
    public void testRecordAttendance_BadRequest() {
        when(attendanceService.recordAttendance(attendanceDTO)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<Attendance> response = attendanceController.createAttendance(attendanceDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(attendanceService, times(1)).recordAttendance(attendanceDTO);
    }

    @Test
    public void testGetAttendanceByStudent() {
        List<Attendance> attendances = Collections.singletonList(attendance);
        List<AttendanceDTO> attendanceDTOs = attendances.stream()
                .map(AttendanceMapper.INSTANCE::attendanceToDto)
                .collect(Collectors.toList());

        when(attendanceService.getAttendanceByStudent(1L)).thenReturn(attendanceDTOs);

        ResponseEntity<List<AttendanceDTO>> response = attendanceController.getAttendanceByStudent(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());

        verify(attendanceService, times(1)).getAttendanceByStudent(1L);
    }

    @Test
    public void testGetAttendanceByCourse() {
        List<Attendance> attendances = Collections.singletonList(attendance);
        List<AttendanceDTO> attendanceDTOs = attendances.stream()
                .map(AttendanceMapper.INSTANCE::attendanceToDto)
                .collect(Collectors.toList());

        when(attendanceService.getAttendanceByCourse(101L)).thenReturn(attendanceDTOs);

        ResponseEntity<List<AttendanceDTO>> response = attendanceController.getAttendanceByCourse(101L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());

        verify(attendanceService, times(1)).getAttendanceByCourse(101L);
    }



    @Test
    public void testDeleteAttendance() {
        doNothing().when(attendanceService).deleteAttendance(1L);

        ResponseEntity<Void> response = attendanceController.deleteAttendance(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(attendanceService, times(1)).deleteAttendance(1L);
    }
}
