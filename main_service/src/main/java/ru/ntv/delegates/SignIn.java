package ru.ntv.delegates;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class SignIn implements JavaDelegate {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationPeriod}")
    private long jwtExpirationPeriod;

    @Value("${app.refreshTokenExpirationPeriod}")
    private long refreshTokenExpirationPeriod;


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        String username = (String) delegateExecution.getVariable("username");
        String password = (String) delegateExecution.getVariable("password");

        String accessToken = generateToken(username, jwtExpirationPeriod, ChronoUnit.MINUTES, true);
        String refreshToken = generateToken(username, refreshTokenExpirationPeriod, ChronoUnit.MINUTES, false);

        delegateExecution.setVariable("accessToken", accessToken);
        delegateExecution.setVariable("refreshToken", refreshToken);

    }

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
                .signWith(SignatureAlgorithm.HS512, jwtSecret) //TODO
                .compact();

    }
}
