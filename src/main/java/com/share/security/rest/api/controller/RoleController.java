package com.share.security.rest.api.controller;

import com.share.security.rest.api.dto.RoleDTO;
import com.share.security.rest.api.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@Tag(name="Role Controller",description = "APIs for Roles")
public class RoleController {

    private RoleService roleManagementService;

    @Autowired
    public void setRoleManagementService(RoleService roleManagementService) {
        this.roleManagementService = roleManagementService;
    }

    @PreAuthorize("hasAuthority('ROL-C')")
    @RequestMapping(method = RequestMethod.POST)
    public RoleDTO createRole(@RequestBody RoleDTO roleDTO) {
        return roleManagementService.createRole(roleDTO);
    }

    @PreAuthorize("hasAuthority('ROL-D')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteRole(@PathVariable String id) {
        roleManagementService.deleteRole(id);
    }

    @PreAuthorize("hasAuthority('ROL-R')")
    @RequestMapping(method = RequestMethod.GET)
    public List<RoleDTO> findAllRoles(@RequestHeader("Authorization") String auth) {
        return roleManagementService.findAllRoles(auth);
    }


    @PreAuthorize("hasAuthority('ROL-R')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public RoleDTO findRoleById(@RequestHeader("Authorization") String auth, @PathVariable String id) {
        return roleManagementService.findRoleById(auth, id);
    }

    @PreAuthorize("hasAuthority('ROL-U')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public RoleDTO updateRole(@PathVariable String id, @RequestBody RoleDTO roleDTO) {
        return roleManagementService.updateRole(id, roleDTO);
    }

    @PreAuthorize("hasAuthority('ROL-R')")
    @GetMapping(value = "/roleNames")
    public List<String> getPermissionsByRoleName(@RequestHeader("Authorization") String auth, @RequestParam("role") List<String> roles) {
        return roleManagementService.getAllPermissionByRoleName(auth, roles);
    }

}