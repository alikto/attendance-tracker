package com.example.server.mapper;

import com.example.server.dto.UserDTO;
import com.example.server.entity.user.Student;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface StudentMapper extends UserMapper {

    @InheritConfiguration
    UserDTO studentToDto(Student student);

    @InheritConfiguration
    @Mapping(target = "faculty", ignore = true)
    Student dtoToStudent(UserDTO userDTO);
}
