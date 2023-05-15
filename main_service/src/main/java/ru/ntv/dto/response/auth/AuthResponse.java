package ru.ntv.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse implements Serializable {
    private String jwt;
    private String refreshToken;
    private String message;
    private String errorMessage;
}