package com.share.security.rest.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Admin Controller", description = "Test APIs for AdminController")
public class AdminController {

    @GetMapping("/admin")
    @RolesAllowed("ROL-C") // Only users with the "Admin" role can access this method
    public String admin() {
        return "Hello Admin";
    }

    @GetMapping("/user")
    @RolesAllowed("USR-D") // Only users with the "User" role can access this method
    public String user() {
        return "Hello User";
    }

    @GetMapping("/all")
    @PermitAll // Anyone can access this method
    public String all() {
        return "Hello Everyone";
    }

    @GetMapping("/deny")
    @DenyAll // No one can access this method
    public String deny() {
        return "Access Denied";
    }
}
