package com.example.server.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AttendanceDTO {
    private Long studentId;
    private Long courseId;
    private LocalDate date;
    private boolean present;

    public AttendanceDTO(Long studentId, Long courseId, LocalDate date, boolean present) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.date = date;
        this.present = present;
    }
}
