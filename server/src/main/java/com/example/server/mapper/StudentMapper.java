package com.example.server.mapper;

import com.example.server.dto.UserDTO;
import com.example.server.entity.user.Student;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentMapper extends UserMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(source = "studentNumber", target = "studentNumber")
    @Mapping(source = "faculty.id", target = "facultyId")
    UserDTO studentToDto(Student student);

    @InheritConfiguration
    @Mapping(target = "faculty", ignore = true)
    Student dtoToStudent(UserDTO userDTO);
}
