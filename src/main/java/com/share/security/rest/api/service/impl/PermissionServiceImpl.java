package com.share.security.rest.api.service.impl;

import com.share.security.rest.api.dto.PermissionDTO;
import com.share.security.rest.api.entity.Permission;
import com.share.security.rest.api.exception.ResourceConflictException;
import com.share.security.rest.api.exception.ResourceNotFoundException;
import com.share.security.rest.api.mappers.PermissionsMapper;
import com.share.security.rest.api.repository.PermissionRepository;
import com.share.security.rest.api.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public PermissionDTO createPermission(PermissionDTO permissionDTO) {
        if (permissionRepository.findByNameAndAbbr(permissionDTO.getName().trim(), permissionDTO.getAbbr().trim()).isPresent()) {
            throw new ResourceConflictException(String.format("Permission already exists with given permission name: %s and abbr: %s", permissionDTO.getName(), permissionDTO.getAbbr()));
        }
        Permission permission = new Permission(permissionDTO.getAbbr().trim(), permissionDTO.getName().trim());
        permission = permissionRepository.save(permission);
        return PermissionsMapper.INSTANCE.toDto(permission);
    }

    @Override
    public void deletePermission(String id) {
        Permission permission = permissionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Permission not found with given id: " + id));
        permission.setDeleted(true);
        permissionRepository.save(permission);
    }

    @Override
    public List<PermissionDTO> findAllPermission() {
        List<Permission> permissions = permissionRepository.findAllByOrderByNameAsc();
        return PermissionsMapper.INSTANCE.toDtoList(permissions);
    }

    @Override
    public PermissionDTO findPermissionById(String id) {
        Permission permission = permissionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Permission not found with given id: " + id));
        return PermissionsMapper.INSTANCE.toDto(permission);
    }

    @Override
    public PermissionDTO updatePermission(String id, PermissionDTO permissionDTO) {
        Permission permission = permissionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Permission not found with given id: " + id));
        if (!permission.getName().equals(permissionDTO.getName().trim())) {
            permissionRepository.findByName(permissionDTO.getName().trim()).ifPresent(existingPermission -> {
                throw new ResourceConflictException("Permission already exists with given permission name : " + existingPermission.getName().trim());
            });
        }
        permission.setName(permissionDTO.getName().trim());
        return PermissionsMapper.INSTANCE.toDto(permissionRepository.save(permission));
    }
}