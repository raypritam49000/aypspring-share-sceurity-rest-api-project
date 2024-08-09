package com.share.security.rest.api.mappers;

import com.share.security.rest.api.dto.PermissionDTO;
import com.share.security.rest.api.entity.Permission;
import com.share.security.rest.api.mappers.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PermissionsMapper extends BaseMapper<PermissionDTO, Permission> {
    PermissionsMapper INSTANCE = Mappers.getMapper(PermissionsMapper.class);
}