package ru.ntv.delegates.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.ntv.entity.users.User;
import ru.ntv.etc.DatabaseRole;
import ru.ntv.repo.PrivilegeRepository;
import ru.ntv.security.JwtTokenProvider;
import ru.ntv.service.UserService;

import javax.inject.Named;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Component
@Named
public class SignIn implements JavaDelegate {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationPeriod}")
    private long jwtExpirationPeriod;

    @Value("${app.refreshTokenExpirationPeriod}")
    private long refreshTokenExpirationPeriod;

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    AuthenticationManager authenticationManager;



    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        try {


        String username = (String) delegateExecution.getVariable("username");
        String password = (String) delegateExecution.getVariable("password");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final var jwt = jwtTokenProvider.generateJWTFromAuthentication(authentication);
        final var refreshToken = jwtTokenProvider.generateRefreshTokenFromAuthentication(authentication);

        delegateExecution.setVariable("accessToken", jwt);
        delegateExecution.setVariable("refreshToken", refreshToken);
        delegateExecution.setVariable("username", username);
        } catch (BadCredentialsException e){
            throw new BpmnError("BadCredentialsException");
        }
    }


}
