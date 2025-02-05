package com.example.server.mapper;

import com.example.server.dto.UserDTO;
import com.example.server.entity.Admin;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper
public interface AdminMapper extends UserMapper {

    @InheritConfiguration
    UserDTO adminToDto(Admin admin);

    @InheritConfiguration
    Admin dtoToAdmin(UserDTO userDTO);
}