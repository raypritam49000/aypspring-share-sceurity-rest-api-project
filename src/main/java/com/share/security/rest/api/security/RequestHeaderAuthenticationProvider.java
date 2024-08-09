package com.share.security.rest.api.security;

import com.share.security.rest.api.security.jsonwebtoken.AuthTokenDetailsDTO;
import com.share.security.rest.api.security.jsonwebtoken.JsonWebTokenUtility;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RequestHeaderAuthenticationProvider implements AuthenticationProvider {
    private final Logger LOGGER = Logger.getLogger(RequestHeaderAuthenticationProvider.class.getName());

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MethodSecurityConfigProperties methodSecurityConfigProperties;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String authorizationHeader = String.valueOf(authentication.getPrincipal());

            LOGGER.info("@@@ RequestHeaderAuthenticationProvider authenticate : " + authorizationHeader);

            if (StringUtils.isBlank(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
                LOGGER.severe("Bad Request Header Credentials: " + authorizationHeader);
                throw new BadCredentialsException("Bad Request Header Credentials");
            }

            String jwt = authorizationHeader.substring(7);
            UserDetails userDetails = parseToken(jwt);

            //return new PreAuthenticatedAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());

            if (Objects.nonNull(userDetails) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                PreAuthenticatedAuthenticationToken usernamePasswordAuthenticationToken = new PreAuthenticatedAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                return usernamePasswordAuthenticationToken;
            }
            return null;

        } catch (Exception ex) {
            LOGGER.severe("Exception Occur when inside RequestHeaderAuthenticationProvider call authenticate method : " + ex.getMessage());
            throw new BadCredentialsException(ex.getMessage());
        }
    }

    private UserDetails parseToken(String tokenHeader) {
        UserDetails principal = null;
        AuthTokenDetailsDTO authTokenDetails = JsonWebTokenUtility.parseAndValidate(tokenHeader);

        if (Objects.nonNull(authTokenDetails)) {
            List<GrantedAuthority> authorities = authTokenDetails.getGrantedAuthorities()
                    .stream()
                    .flatMap(role -> Stream.of(
                            methodSecurityConfigProperties.isJsr250Enabled() ? new SimpleGrantedAuthority("ROLE_" + role) : null,
                            methodSecurityConfigProperties.isPrePostEnabled() ? new SimpleGrantedAuthority(role) : null
                    ))
                    .filter(Objects::nonNull) // Remove any null values
                    .collect(Collectors.toList());
            principal = new User(authTokenDetails.getUsername(), "", authorities);
        }

        return principal;
    }

//    private UserDetails parseToken(String tokenHeader) {
//        UserDetails principal = null;
//        AuthTokenDetailsDTO authTokenDetails = JsonWebTokenUtility.parseAndValidate(tokenHeader);
//
//        if (Objects.nonNull(authTokenDetails)) {
//            List<GrantedAuthority> authorities = authTokenDetails.getGrantedAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//            principal = new User(authTokenDetails.getUsername(), "", authorities);
//        }
//
//        return principal;
//    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}