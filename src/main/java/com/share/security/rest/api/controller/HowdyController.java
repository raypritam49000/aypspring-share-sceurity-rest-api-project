package com.share.security.rest.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Howdy Controller", description = "Test APIs for HowdyController")
public class HowdyController {

    @GetMapping("/howdy")
    @PreAuthorize("hasAuthority('ROL-C')")
    public String howdy(@AuthenticationPrincipal UserDetails currentUser) {
        String currentUserName = currentUser.getUsername();
        return "Hello Howdy, " + currentUserName;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROL-C')")
    public String admin() {
        return "Hello Admin";
    }
}
