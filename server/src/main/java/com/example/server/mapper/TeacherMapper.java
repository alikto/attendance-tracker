package com.example.server.mapper;

import com.example.server.dto.UserDTO;
import com.example.server.entity.user.Teacher;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeacherMapper extends UserMapper {

    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    @Mapping(source = "faculty.id", target = "facultyId")
    UserDTO teacherToDto(Teacher teacher);

    @InheritConfiguration
    @Mapping(target = "faculty", ignore = true)
    Teacher dtoToTeacher(UserDTO userDTO);
}