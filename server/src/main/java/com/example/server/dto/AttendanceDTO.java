package com.example.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class AttendanceDTO {
    private Long id;
    private Long studentId;
    private Long courseId;
    private LocalDate date;
    private boolean present;
}
