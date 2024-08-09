package com.share.security.rest.api.security.jsonwebtoken;

import com.share.security.rest.api.enumeration.CustomerLevel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class JsonWebTokenUtility {

    private static final Logger logger = LoggerFactory.getLogger(JsonWebTokenUtility.class);

    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    private static final String SECRET_KEY = "45d04d07434cfdb44288b98cce426c324a08bf66c615423b532a5ff3c0530ae34c1449f9dba67c5116bdedea9b935cc1f57d1901595d54bade5437ec22d7502f";

    public static String createJsonWebToken(AuthTokenDetailsDTO authTokenDetailsDTO) {
        logger.info("createJsonWebToken()...");
        return Jwts.builder()
                .setSubject(authTokenDetailsDTO.getUserId())
                .claim("username", authTokenDetailsDTO.getUsername())
                .claim("email", authTokenDetailsDTO.getEmail())
                .claim("customerLevel", authTokenDetailsDTO.getCustomerLevel())
                .claim("logIp", authTokenDetailsDTO.getLogIp())
                .claim("roles", authTokenDetailsDTO.getRoleNames())
                .claim("grantedAuthorities", authTokenDetailsDTO.getGrantedAuthorities())
                .claim("lastPasswordChangeDate", authTokenDetailsDTO.getLastPasswordChangeDate())
                .claim("userZoneId", authTokenDetailsDTO.getUserZoneId())
                .setExpiration(authTokenDetailsDTO.getExpirationDate())
                .signWith(signatureAlgorithm, deserializeKey()).compact();
    }

    private static Key deserializeKey() {
        byte[] decodedKey = Base64.getDecoder().decode(JsonWebTokenUtility.SECRET_KEY);
        return new SecretKeySpec(decodedKey, signatureAlgorithm.getJcaName());
    }

    public static AuthTokenDetailsDTO parseAndValidate(String token) {
        Claims claims = parseTokenIntoClaimsWithHandling(token);
        return Objects.isNull(claims) ? null : createTokenDTOFromClaims(claims);
    }


    private static Claims parseTokenIntoClaims(String token) throws Exception {
        return getClaimsWithoutKey(token);
    }

    private static Claims parseTokenIntoClaimsWithHandling(String token) {
        try {
            return parseTokenIntoClaims(token);
        } catch (ExpiredJwtException expiredJwtException) {
            logger.error("Failed to validate token. Token has expired.");
            return null;
        } catch (Exception ex) {
            logger.error("parseAndValidate() EXCEPTION : {} ", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized", ex);
        }
    }

    private static AuthTokenDetailsDTO createTokenDTOFromClaims(Claims claims) {
        return new AuthTokenDetailsDTO.Builder()
                .withUserId(claims.getSubject())
                .withUsername((String) claims.get("username"))
                .withEmail((String) claims.get("email"))
                .withCustomerId((String) claims.get("customerId"))
                .withCustomerLevel(CustomerLevel.valueOf((String) claims.get("customerLevel")))
                .withBranchId((String) claims.get("branchId"))
                .withLogIp((String) claims.get("logIp"))
                .withTenant((String) claims.get("tenant"))
                .withRoleNames((List<String>) claims.get("roles"))
                .withGrantedAuthorities((List<String>) claims.get("grantedAuthorities"))
                .withExpirationDate(claims.getExpiration())
                .withLastPasswordChangeDate(Objects.isNull(claims.get("lastPasswordChangeDate")) ? null : new Date((Long) claims.get("lastPasswordChangeDate")))
                .withUserZoneId((String) claims.get("userZoneId"))
                .build();
    }


    private static Claims getClaimsWithoutKey(String token) {
        return Jwts.parser().setSigningKey(deserializeKey()).parseClaimsJws(token).getBody();
    }
}