package ru.ntv.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;
    
    @Value("${app.jwtExpirationPeriod}")
    private long jwtExpirationPeriod;

    @Value("${app.refreshTokenExpirationPeriod}")
    private long refreshTokenExpirationPeriod;

    public String generateToken(String login, long period, ChronoUnit unit, boolean isAccessToken) {
        Instant now = Instant.now();
        Instant expiration = now.plus(period, unit);
        Map<String, Object> claims = new HashMap<>();
        claims.put("isAccessToken", isAccessToken);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(login)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

    }

    public String generateJWT(String username) {
        return generateToken(username, jwtExpirationPeriod, ChronoUnit.MINUTES, true);
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, refreshTokenExpirationPeriod, ChronoUnit.MINUTES, false);
    }

    public String generateJWTFromAuthentication(Authentication authentication) {
        final User user = (User) authentication.getPrincipal();
        
        return generateJWT(user.getUsername());
    }

    public String generateRefreshTokenFromAuthentication(Authentication authentication) {
        final User user = (User) authentication.getPrincipal();
        
        return generateRefreshToken(user.getUsername());
    }

    public String getUserLoginFromToken(String token) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }

        return false;
    }

    public boolean validateAccessToken(String jwt) {
        if (!validateToken(jwt)) return false;

        Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
        Boolean isAccessToken = claims.getBody().get("isAccessToken", Boolean.class);

        return isAccessToken != null && isAccessToken;
    }

    public boolean validateRefreshToken(String jwt) {
        if (!validateToken(jwt)) return false;

        Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
        Boolean isAccessToken = claims.getBody().get("isAccessToken", Boolean.class);

        return isAccessToken != null && !isAccessToken;
    }
}