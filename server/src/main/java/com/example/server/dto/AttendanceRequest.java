package com.example.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AttendanceRequest {
    private Long studentId;
    private Long courseId;
    private String qrCodeKey;
}
