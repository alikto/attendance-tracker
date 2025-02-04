package com.example.server.mapper;

import com.example.server.dto.UserDTO;
import com.example.server.entity.Teacher;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TeacherMapper extends UserMapper {

    // Use @InheritConfiguration to inherit common mappings from UserMapper
    @InheritConfiguration // Mapping faculty-specific fields
    UserDTO teacherToDto(Teacher teacher);

    @InheritConfiguration
    @Mapping(target = "faculty", ignore = true) // Faculty handled manually
    Teacher dtoToTeacher(UserDTO userDTO);
}