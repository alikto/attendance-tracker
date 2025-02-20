package com.example.server.repository;

import com.example.server.entity.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {
    Optional<AttendanceSession> findByQrCodeKey(String qrCodeKey);
}
