package com.share.security.rest.api.controller;

import com.share.security.rest.api.dto.RoleDTO;
import com.share.security.rest.api.dto.UserDTO;
import com.share.security.rest.api.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(
        name = "User Controller",
        description = "CRUD REST APIs - Create User, Update User, Get User, Get All User, User Student"
)
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('USR-C')")
    @PostMapping
    public UserDTO createUser(@RequestHeader("Authorization") String auth, @RequestBody UserDTO userDTO) {
        logger.info("@@@ Call Create User API : {} ", userDTO);
        return userService.createUser(auth, userDTO);
    }

    @PreAuthorize("hasAuthority('USR-D')")
    @DeleteMapping("/{id}")
    public void deleteUser(@RequestHeader("Authorization") String auth, @PathVariable String id) {
        logger.info("@@@@ deleteUser :: {} ", id);
        userService.deleteUser(auth, id);
    }

    @PreAuthorize("hasAuthority('USR-U')")
    @PutMapping(value = "/{id}")
    public UserDTO updateUser(@RequestHeader("Authorization") String auth, @PathVariable String id, @RequestBody UserDTO userDTO) {
        logger.info("@@@ Call Update User API : {} ", userDTO);
        return userService.updateUser(auth, id, userDTO);
    }

    @PreAuthorize("hasAuthority('USR-R')")
    @GetMapping(value = "/{id}")
    public UserDTO findUserById(@RequestHeader("Authorization") String auth, @PathVariable String id) {
        logger.info("@@@ Call findUserById User API ID : {} ", id);
        return userService.findUserById(auth, id);
    }

    @PreAuthorize("hasAuthority('USR-R')")
    @GetMapping(value = "/username/{username}")
    public UserDTO findUserByUsername(@RequestHeader("Authorization") String auth, @PathVariable String username) {
        logger.info("@@@ Call findUserByUsername User API Username : {} ", username);
        return userService.findUserByUsername(auth, username);
    }

    @PostMapping(value = "/findUserByEmailInternal")
    public UserDTO findUserByEmailInternal(@RequestParam("emailId") String emailId) {
        logger.info("@@@ findUserByEmailInternal Email : {} ", emailId);
        return userService.findUserByEmailInternal(emailId);
    }

    @PostMapping(value = "/findUserByEmail")
    public UserDTO findUserByEmail(@RequestHeader("Authorization") String auth, @RequestParam("emailId") String emailId) {
        logger.info("@@@ findUserByEmail :::: ");
        return userService.findUserByEmail(auth, emailId);
    }

    @PreAuthorize("hasAuthority('USR-R')")
    @GetMapping(value = "/{id}/roles")
    public List<RoleDTO> findUserRoles(@RequestHeader("Authorization") String auth, @PathVariable String id) {
        return userService.findAllRolesForUser(auth, id);
    }

    @PreAuthorize("hasAuthority('USR-R')")
    @GetMapping(value = "/email/username")
    public UserDTO findByUsernameOrEmailAndDeletedFalse(@RequestHeader("Authorization") String auth, @RequestParam("username") String username, @RequestParam("email") String email) {
        return userService.findByUsernameOrEmailAndDeletedFalse(auth, username, email);
    }

}
