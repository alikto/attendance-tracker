package com.example.server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDTO {
    private Long id;
    private String courseName;
    private Long facultyId;
    private Long teacherId;

    public CourseDTO(Long id, String courseName, Long facultyId, Long teacherId) {
        this.id = id;
        this.courseName = courseName;
        this.facultyId = facultyId;
        this.teacherId = teacherId;
    }
}
