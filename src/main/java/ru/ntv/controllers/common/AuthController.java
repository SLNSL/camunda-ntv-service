package ru.ntv.controllers.common;

import javax.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ntv.dto.request.auth.NewUser;
import ru.ntv.dto.request.auth.OldUser;
import ru.ntv.dto.response.auth.AuthResponse;
import ru.ntv.service.AuthService;

@RestController
@RequestMapping("auth")
@Validated
public class AuthController {
    final
    AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("sign-in")
    ResponseEntity<AuthResponse> signIn(@Valid @RequestBody OldUser user) throws BadCredentialsException {
        return ResponseEntity.ok(authService.signIn(user));
    }

    @PostMapping("sign-up")
    ResponseEntity<AuthResponse> signUp(@Valid @RequestBody NewUser newUser){
        return authService.signUp(newUser);
    }
}