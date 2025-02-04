package com.example.server.mapper;

import com.example.server.dto.UserDTO;
import com.example.server.entity.Student;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface StudentMapper extends UserMapper {

    // Use @InheritConfiguration to inherit common mappings from UserMapper
    @InheritConfiguration
    UserDTO studentToDto(Student student);

    @InheritConfiguration
    @Mapping(target = "faculty", ignore = true) // Faculty handled manually
    Student dtoToStudent(UserDTO userDTO);
}
