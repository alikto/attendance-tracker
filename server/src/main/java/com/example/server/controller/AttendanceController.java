package com.example.server.controller;

import com.example.server.dto.AttendanceDTO;
import com.example.server.dto.AttendanceRequest;
import com.example.server.service.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService ) {
        this.attendanceService = attendanceService;
    }

//    @PostMapping
//    public ResponseEntity<Attendance> createAttendance(@RequestBody AttendanceDTO attendanceDTO) {
//        try {
//            Attendance attendance = attendanceService.recordAttendance(attendanceDTO);
//            return new ResponseEntity<>(attendance, HttpStatus.CREATED);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        }
//    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByStudent(@PathVariable Long studentId) {
        List<AttendanceDTO> attendanceDTOs = attendanceService.getAttendanceByStudent(studentId);

        return new ResponseEntity<>(attendanceDTOs, HttpStatus.OK);
    }


    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByCourse(@PathVariable Long courseId) {
        List<AttendanceDTO> attendanceDTOs = attendanceService.getAttendanceByCourse(courseId);

        return new ResponseEntity<>(attendanceDTOs, HttpStatus.OK);
    }

    @DeleteMapping("/{attendanceId}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long attendanceId) {
        attendanceService.deleteAttendance(attendanceId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/mark-attendance")
    public ResponseEntity<String> validateQRCode(@RequestBody AttendanceRequest request) {
        String response = attendanceService.validateQRCode(request);

        if (response.equals("Attendance recorded successfully")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
