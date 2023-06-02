package ru.ntv.delegates.auth;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.ntv.etc.DatabaseRole;
import ru.ntv.security.JwtTokenProvider;
import ru.ntv.service.UserService;

import javax.inject.Named;
import java.util.Objects;

@Component
@Named
@Log4j2
public class SignInBoss implements JavaDelegate {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserService userService;

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

            log.info(authentication.getName());

            final var user = userService.findByLogin(username);

            log.info(user.getRole().getRoleName());
            if (Objects.equals(user.getRole().getRoleName(), DatabaseRole.ROLE_BOSS.name())) {
                throw new BpmnError("BadCredentialsException");
            }

            final var jwt = jwtTokenProvider.generateJWTFromAuthentication(authentication);
            final var refreshToken = jwtTokenProvider.generateRefreshTokenFromAuthentication(authentication);

            delegateExecution.setVariable("accessToken", jwt);
            delegateExecution.setVariable("refreshToken", refreshToken);
            delegateExecution.setVariable("username", user.getLogin());
        } catch (BadCredentialsException e){
            throw new BpmnError("BadCredentialsException");
        }
    }
}