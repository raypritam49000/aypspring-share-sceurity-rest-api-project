package com.share.security.rest.api.controller;

import com.share.security.rest.api.dto.PermissionDTO;
import com.share.security.rest.api.service.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@Tag(name="Permission Controller",description = "APIs for Permissions")
public class PermissionController {

    private PermissionService permissionService;

    @Autowired
    public void setUserManagementService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PreAuthorize("hasAuthority('PERM-C')")
    @RequestMapping(method = RequestMethod.POST)
    public PermissionDTO createPermission(@RequestBody PermissionDTO permissionDTO) {
        return permissionService.createPermission(permissionDTO);
    }

    @PreAuthorize("hasAuthority('PERM-D')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deletePermission(@PathVariable String id) {
        permissionService.deletePermission(id);
    }

    @PreAuthorize("hasAuthority('PERM-R')")
    @RequestMapping(method = RequestMethod.GET)
    public List<PermissionDTO> findAllPermissions() {
        return permissionService.findAllPermission();
    }

    @PreAuthorize("hasAuthority('PERM-R')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PermissionDTO findPermissionById(@PathVariable String id) {
        return permissionService.findPermissionById(id);
    }

    @PreAuthorize("hasAuthority('PERM-U')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PermissionDTO updatePermission(@PathVariable String id, @RequestBody PermissionDTO permissionDTO) {
        return permissionService.updatePermission(id, permissionDTO);
    }

}