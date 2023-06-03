package ru.ntv.delegates.auth;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.ntv.etc.DatabasePrivilege;
import ru.ntv.security.JwtTokenProvider;

import javax.inject.Named;
import java.util.Arrays;


@Component
@Named
@Slf4j
public class SignIn implements JavaDelegate {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
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
            final var ownPrivileges = authentication.getAuthorities();

            Arrays.stream(DatabasePrivilege.values()).sequential()
                    .map(Enum::name)
                    .forEach(privilege -> {
                        final var hasPrivilege = ownPrivileges
                                .stream()
                                .anyMatch(p -> p.getAuthority().equals(privilege));
                        log.info(privilege + ": " + hasPrivilege);
                        delegateExecution.setVariable(privilege, hasPrivilege);
                    });
    
            delegateExecution.setVariable("accessToken", jwt);
            delegateExecution.setVariable("refreshToken", refreshToken);
            delegateExecution.setVariable("username", username);
        } catch (BadCredentialsException e){
            throw new BpmnError("BadCredentialsException");
        }
    }
}