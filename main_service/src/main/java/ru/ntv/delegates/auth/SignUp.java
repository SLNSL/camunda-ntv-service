package ru.ntv.delegates.auth;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.ntv.dto.response.auth.AuthResponse;
import ru.ntv.entity.users.TelegramUser;
import ru.ntv.entity.users.User;
import ru.ntv.etc.DatabaseRole;
import ru.ntv.repo.RoleRepository;
import ru.ntv.repo.TelegramUserRepository;
import ru.ntv.repo.UserRepository;
import ru.ntv.security.JwtTokenProvider;


@Component
public class SignUp implements JavaDelegate {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TelegramUserRepository telegramUserRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtUtils;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = (String) delegateExecution.getVariable("username");
        String password = (String) delegateExecution.getVariable("password");
        String telegramName = (String) delegateExecution.getVariable("telegram_name");
        //todo добавить логику с почтой сори(
        String mail = (String) delegateExecution.getVariable("mail");

        if (userRepository.existsByLogin(username)) {
            throw new BpmnError("This login is already taken");
        }

        final var user = new User();
        user.setLogin(username);
        user.setPassword(encoder.encode(password));
        user.setRole(
                roleRepository.findRoleByRoleName(
                        DatabaseRole.ROLE_CLIENT.name()
                )
        );
        userRepository.save(user);

        final var telegramUser = new TelegramUser();
        telegramUser.setTelegramName(telegramName);
        telegramUser.setUserId(user.getId());
        telegramUserRepository.save(telegramUser);

        // Sign in
        final var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final var jwt = jwtUtils.generateJWTFromAuthentication(authentication);
        final var refreshToken = jwtUtils.generateRefreshTokenFromAuthentication(authentication);
        delegateExecution.setVariable("accessToken", jwt);
        delegateExecution.setVariable("refreshToken", refreshToken);
    }
}