package com.example.server.controller;

import com.example.server.dto.AttendanceDTO;
import com.example.server.entity.Attendance;
import com.example.server.service.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public ResponseEntity<Attendance> recordAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        try {
            Attendance attendance = attendanceService.recordAttendance(attendanceDTO);
            return new ResponseEntity<>(attendance, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Attendance>> getAttendanceByStudent(@PathVariable Long studentId) {
        return new ResponseEntity<>(attendanceService.getAttendanceByStudent(studentId), HttpStatus.OK);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Attendance>> getAttendanceByCourse(@PathVariable Long courseId) {
        return new ResponseEntity<>(attendanceService.getAttendanceByCourse(courseId), HttpStatus.OK);
    }

    @DeleteMapping("/{attendanceId}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long attendanceId) {
        attendanceService.deleteAttendance(attendanceId);
        return ResponseEntity.noContent().build();
    }
}
