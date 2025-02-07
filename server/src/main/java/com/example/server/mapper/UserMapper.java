package com.example.server.mapper;

import com.example.server.dto.UserDTO;
import com.example.server.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userToDto(User user);

}