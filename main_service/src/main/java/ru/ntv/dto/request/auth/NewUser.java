package ru.ntv.dto.request.auth;

import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUser implements Serializable {
    @Size(min = 2, max = 255)
    private String username;
    @Size(min = 2, max = 255)
    private String password;
    private String telegram_name;
    private String email;
}