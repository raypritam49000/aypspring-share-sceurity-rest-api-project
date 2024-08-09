package com.share.security.rest.api.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class MethodSecurityConfigProperties {

    @Value("${spring.security.method.pre-post-enabled:true}")
    private boolean prePostEnabled;

    @Value("${spring.security.method.jsr250-enabled:true}")
    private boolean jsr250Enabled;
}
