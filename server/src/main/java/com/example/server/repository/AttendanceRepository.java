package com.example.server.repository;

import com.example.server.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findByCourseId(Long courseId);
}
