package com.pizza.pizzashop.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

public class LoginDTO implements Serializable {
    @Size(max = 50)
    private final String email;
    private final String login;
    @NotNull
    private final String password;
    @Pattern(regexp = "^\\+?\\d{11}$|^8\\d{10}$")
    private final String phonenumber;

    public LoginDTO(String email, String login, String phonenumber, @NotNull String password) {
        this.email = email;
        this.login = login;
        this.password = password;
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        if (login != null) return this.login;
        else if (email != null) return this.email;
        else return this.phonenumber;
    }

    public @NotNull String getPassword() {
        return password;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginDTO entity = (LoginDTO) o;
        return Objects.equals(this.email, entity.email) &&
                Objects.equals(this.login, entity.login) &&
                Objects.equals(this.password, entity.password) &&
                Objects.equals(this.phonenumber, entity.phonenumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, login, password, phonenumber);
    }
}