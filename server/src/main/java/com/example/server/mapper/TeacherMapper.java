package com.example.server.mapper;

import com.example.server.dto.UserDTO;
import com.example.server.entity.Teacher;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TeacherMapper extends UserMapper {

    @InheritConfiguration
    UserDTO teacherToDto(Teacher teacher);

    @InheritConfiguration
    @Mapping(target = "faculty", ignore = true)
    Teacher dtoToTeacher(UserDTO userDTO);
}