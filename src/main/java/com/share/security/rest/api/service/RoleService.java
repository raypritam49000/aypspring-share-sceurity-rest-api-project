package com.share.security.rest.api.service;

import com.share.security.rest.api.dto.RoleDTO;

import java.util.List;

public interface RoleService {
    public List<RoleDTO> findAllRoles(String auth);

    RoleDTO createRole(RoleDTO roleDTO);

    void deleteRole(String id);

    List<String> getAllPermissionByRoleName(String auth, List<String> roleName);

    RoleDTO findRoleById(String auth, String id);

    RoleDTO updateRole(String id, RoleDTO roleDTO);
}