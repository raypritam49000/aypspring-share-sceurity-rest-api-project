package com.share.security.rest.api.mappers;

import com.share.security.rest.api.dto.RoleDTO;
import com.share.security.rest.api.entity.Permission;
import com.share.security.rest.api.entity.Role;
import com.share.security.rest.api.mappers.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RolesMapper extends BaseMapper<RoleDTO, Role> {
    RolesMapper INSTANCE = Mappers.getMapper(RolesMapper.class);

    @Override
    RoleDTO toDto(Role entity);

    @Override
    Role toEntity(RoleDTO dto);

    @Override
    List<RoleDTO> toDtoList(List<Role> entities);

    @Override
    List<Role> toEntityList(List<RoleDTO> entities);

    default String fromPermission(Permission permission) {
        return permission == null ? null : permission.getName();
    }

    default Permission fromStringToPermission(String permission) {
        return null;
    }
}