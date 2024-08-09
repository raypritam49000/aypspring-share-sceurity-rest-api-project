package com.share.security.rest.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenDTO {
    private String token;
    private String refreshToken;
    private String type = "Bearer";
}