package com.freshplanner.api.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <h2>Jwt Manager </h2>
 * <p>Required: application.properties -> app.settings.jwt.secret</p>
 */
@Component
public class JwtManager {

    @Value("${app.settings.jwt.expiration-ms}")
    private Integer expirationMs;

    @Value("${app.settings.jwt.secret}")
    private String jwtSecret;

    public String generateJwtToken(Authentication authentication) {
        UserAuthority userPrincipal = (UserAuthority) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Validates the JWT:
     * <ul>
     *     <li>Invalid signature</li>
     *     <li>Expired token</li>
     *     <li>Unsupported token</li>
     *     <li>Illegal argument or malformed</li>
     * </ul>
     *
     * @param authToken token to validate
     * @return true if valid
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
