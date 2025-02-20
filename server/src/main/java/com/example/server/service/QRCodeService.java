package com.example.server.service;

import com.example.server.entity.AttendanceSession;
import com.example.server.entity.Course;
import com.example.server.repository.AttendanceSessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class QRCodeService {

    private final AttendanceSessionRepository sessionRepository;

    public QRCodeService(AttendanceSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public String generateQRCodeKey(Long courseId, LocalDateTime startTime, LocalDateTime endTime) {
        String qrCodeKey = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10); // QR code valid for 10 minutes

        Course course = new Course();
        course.setId(courseId);
        AttendanceSession session = new AttendanceSession(
                course,
                qrCodeKey,
                startTime,
                endTime,
                expirationTime
        );

        sessionRepository.save(session);

        return qrCodeKey;
    }
}

