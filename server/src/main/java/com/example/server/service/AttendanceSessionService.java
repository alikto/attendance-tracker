package com.example.server.service;

import com.example.server.entity.AttendanceSession;
import com.example.server.repository.AttendanceSessionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AttendanceSessionService {

    private final AttendanceSessionRepository sessionRepository;

    public AttendanceSessionService(AttendanceSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Optional<AttendanceSession> getSessionByQRCode(String qrCodeKey) {
        return sessionRepository.findByQrCodeKey(qrCodeKey);
    }
}

