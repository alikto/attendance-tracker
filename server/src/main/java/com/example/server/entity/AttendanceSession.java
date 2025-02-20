package com.example.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_session")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "qr_code_key", unique = true, nullable = false)
    private String qrCodeKey;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public AttendanceSession(Course course, String qrCodeKey, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime expirationTime) {
        this.course = course;
        this.qrCodeKey = qrCodeKey;
        this.startTime = startTime;
        this.endTime = endTime;
        this.expirationTime = expirationTime;
    }
}

