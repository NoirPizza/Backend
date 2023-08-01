package com.pizza.pizzashop.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents a data transfer object (DTO) that encapsulates user login information in a standardized format.
 * It is used to provide a consistent structure for passing user login details, including email, login, password, and phone number.
 * This class is read-only, so there's no setter methods presented.
 */
public class LoginDTO implements Serializable {
    @Size(max = 50)
    private final String email;
    private final String login;
    @NotNull
    private final String password;
    @Pattern(regexp = "^\\+?\\d{11}$|^8\\d{10}$")
    private final String phoneNumber;

    public LoginDTO(String login, String email, String phoneNumber, @NotNull String password) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Get the user's login name.
     * If the login is not provided, the email will be returned.
     * If both login and email are not provided, the phone number will be returned.
     *
     * @return One of the provided credential types: the login or email or phone number as a String.
     */
    public String getLogin() {
        if (login != null) return this.login;
        else if (email != null) return this.email;
        else return this.phoneNumber;
    }

    public @NotNull String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginDTO entity = (LoginDTO) o;
        return Objects.equals(this.email, entity.email) &&
                Objects.equals(this.login, entity.login) &&
                Objects.equals(this.password, entity.password) &&
                Objects.equals(this.phoneNumber, entity.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, login, password, phoneNumber);
    }
}