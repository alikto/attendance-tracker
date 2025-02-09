package com.example.server.mapper;

import com.example.server.dto.AttendanceDTO;
import com.example.server.entity.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AttendanceMapper {

    AttendanceMapper INSTANCE = Mappers.getMapper(AttendanceMapper.class);

    @Mapping(source = "attendance.student.id", target = "studentId")
    @Mapping(source = "attendance.course.id", target = "courseId")
    AttendanceDTO attendanceToDto(Attendance attendance);

}
