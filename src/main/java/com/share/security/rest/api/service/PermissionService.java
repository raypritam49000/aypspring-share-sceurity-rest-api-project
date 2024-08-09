package com.share.security.rest.api.service;


import com.share.security.rest.api.dto.PermissionDTO;

import java.util.List;

public interface PermissionService {
    PermissionDTO createPermission(PermissionDTO permissionDTO);

    void deletePermission(String id);

    List<PermissionDTO> findAllPermission();

    PermissionDTO findPermissionById(String id);

    PermissionDTO updatePermission(String id, PermissionDTO permissionDTO);
}