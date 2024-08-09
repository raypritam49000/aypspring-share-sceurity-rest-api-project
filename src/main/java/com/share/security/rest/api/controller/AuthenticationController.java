package com.share.security.rest.api.controller;

import com.share.security.rest.api.dto.AuthTokenDTO;
import com.share.security.rest.api.dto.AuthenticationDTO;
import com.share.security.rest.api.dto.RefreshTokenRequest;
import com.share.security.rest.api.entity.User;
import com.share.security.rest.api.exception.ResourceNotFoundException;
import com.share.security.rest.api.security.jsonwebtoken.AuthTokenDetailsDTO;
import com.share.security.rest.api.security.jsonwebtoken.JsonWebTokenUtility;
import com.share.security.rest.api.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name="Auth Controller",description = "APIs for Authentication")
@RestController
public class AuthenticationController {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    public AuthTokenDTO authenticate(@RequestBody AuthenticationDTO authenticationDTO, HttpServletRequest request) {
        logger.info("@@@ authenticate ::: {}", authenticationDTO);
        String logIp = extractClientIp(request);
        return userService.authenticateUser(authenticationDTO.getUsername(), authenticationDTO.getPassword(), logIp, authenticationDTO.getUserZoneId());
    }

    @PostMapping("/refresh-token")
    public AuthTokenDTO refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return userService.generateRefreshToken(refreshTokenRequest.getRefreshToken());
    }

    private String extractClientIp(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("X-FORWARDED-FOR"))
                .map(header -> header.split(",")[0])
                .orElse(request.getRemoteAddr());
    }


}