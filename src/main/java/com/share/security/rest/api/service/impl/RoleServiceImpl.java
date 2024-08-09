package com.share.security.rest.api.service.impl;

import com.share.security.rest.api.dto.RoleDTO;
import com.share.security.rest.api.entity.Permission;
import com.share.security.rest.api.entity.Role;
import com.share.security.rest.api.entity.User;
import com.share.security.rest.api.exception.ResourceConflictException;
import com.share.security.rest.api.exception.ResourceNotFoundException;
import com.share.security.rest.api.mappers.RolesMapper;
import com.share.security.rest.api.repository.PermissionRepository;
import com.share.security.rest.api.repository.RoleRepository;
import com.share.security.rest.api.repository.UserEntityRepository;
import com.share.security.rest.api.security.jsonwebtoken.AuthTokenDetailsDTO;
import com.share.security.rest.api.security.jsonwebtoken.JsonWebTokenUtility;
import com.share.security.rest.api.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<RoleDTO> findAllRoles(String auth) {
        AuthTokenDetailsDTO authTokenDetailsDTO = JsonWebTokenUtility.parseAndValidate(auth);
        assert authTokenDetailsDTO != null;
        User user = userEntityRepository.findById(authTokenDetailsDTO.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Role not found with given id: " + authTokenDetailsDTO.getUserId()));
        int highestAuthLevel = user.getRoles().stream().mapToInt(Role::getAuthorityLevel).max().orElse(0);
        List<Role> roles = roleRepository.findAllByAuthorityLevelIsLessThanEqualOrderByAuthorityLevelDesc(highestAuthLevel);
        return RolesMapper.INSTANCE.toDtoList(roles);
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        roleRepository.findFirstByName(StringUtils.trimToNull(roleDTO.getName())).ifPresent(role -> {
            throw new ResourceConflictException("Role already exists with given role name: " + role.getName());
        });
        Set<Permission> permissions = roleDTO.getPermissions().stream().map(permissionRepository::findByName).flatMap(Optional::stream).collect(Collectors.toSet());
        return RolesMapper.INSTANCE.toDto(roleRepository.save(new Role(StringUtils.trimToNull(roleDTO.getName()), permissions, roleDTO.getAuthorityLevel())));
    }

    @Override
    public void deleteRole(String id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found with given id: " + id));
        role.setDeleted(true);
        roleRepository.save(role);
    }

    @Override
    public List<String> getAllPermissionByRoleName(String auth, List<String> roleNames) {
        return roleNames.stream()
                .map(roleRepository::findFirstByName)
                .flatMap(optionalRole -> optionalRole.stream().flatMap(role -> role.getPermissions().stream()))
                .map(Permission::getAbbr)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO findRoleById(String auth, String id) {
        var role = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found with given id: " + id));
        return RolesMapper.INSTANCE.toDto(role);
    }


    @Override
    public RoleDTO updateRole(String id, RoleDTO roleDTO) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found with given id: " + id));

        if (!role.getName().equalsIgnoreCase(StringUtils.trimToNull(roleDTO.getName()))) {
            roleRepository.findFirstByName(StringUtils.trimToNull(roleDTO.getName())).ifPresent(existingRole -> {
                throw new ResourceConflictException("Role already exists with given role name: " + existingRole.getName());
            });
        }

        role.setName(StringUtils.trimToNull(roleDTO.getName()));
        role.setPermissions(convertPermissionNamesToPermissions(new ArrayList<>(roleDTO.getPermissions())));
        role.setAuthorityLevel(roleDTO.getAuthorityLevel());
        return RolesMapper.INSTANCE.toDto(roleRepository.save(role));
    }

    private Set<Permission> convertPermissionNamesToPermissions(List<String> permissionNames) {
        return permissionNames.stream()
                .map(permissionRepository::findByName)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
    }

}