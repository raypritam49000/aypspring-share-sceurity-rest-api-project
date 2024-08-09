package com.share.security.rest.api.mappers;


import com.share.security.rest.api.dto.UserDTO;
import com.share.security.rest.api.entity.Role;
import com.share.security.rest.api.entity.User;
import com.share.security.rest.api.mappers.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserEntityMapper extends BaseMapper<UserDTO, User> {

    UserEntityMapper INSTANCE = Mappers.getMapper(UserEntityMapper.class);

    @Override
    @Mapping(source = "passwordHash", target = "password")
    UserDTO toDto(User entity);

    @Override
    @Mapping(source = "password", target = "passwordHash")
    User toEntity(UserDTO dto);

    @Override
    List<UserDTO> toDtoList(List<User> entities);

    @Override
    List<User> toEntityList(List<UserDTO> entities);

    default String fromRoles(Role role) {
        return role == null ? null : role.getName();
    }

    default Role fromStringToRole(String role) {
        // Implement your custom mapping logic here
        return null;
    }
}