package com.nikozka.app.dto;

import com.sun.istack.NotNull;

import javax.validation.constraints.Size;

public class UserDTO {
    @NotNull
    @Size(min = 5, max = 255, message = "User name must be between 5 and 255 characters")
    private String username;
    @NotNull
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    private String password;

    public UserDTO() {
    }

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
