package com.nikozka.app.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticationRequest {
    @NotNull
    @Size(min = 5, max = 255, message = "User name must be between 5 and 255 characters")
    private String username;
    @NotNull
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    private String password;
}
