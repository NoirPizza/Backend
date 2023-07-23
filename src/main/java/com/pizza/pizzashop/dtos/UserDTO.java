package com.pizza.pizzashop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * DTO for {@link com.pizza.pizzashop.entities.User}
 */
public class UserDTO implements Serializable {
    private final Integer id;
    @Size(max = 40)
    @NotBlank
    private final String surname;
    @Size(max = 50)
    @NotBlank
    private final String email;
    @Size(max = 10)
    private final String birthday;
    @NotNull
    @Size(min = 1)
    private final List<RoleDTO> roles;
    @NotNull
    @NotBlank
    private final String login;
    @NotNull
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private final String password;

    @NotNull
    private final String name;
    @NotNull
    private final String phonenumber;

    public UserDTO(
            Integer id,
            String surname,
            String email,
            String birthday,
            @NotNull List<RoleDTO> roles,
            @NotNull String login,
            @NotNull String password,
            @NotNull String name,
            @NotNull String phonenumber) {
        this.id = id;
        this.surname = surname;
        this.email = email;
        this.birthday = birthday;
        this.roles = roles;
        this.login = login;
        this.password = password;
        this.name = name;
        this.phonenumber = phonenumber;
    }

    public Integer getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthday() {
        return birthday;
    }

    public @NotNull List<RoleDTO> getRoles() {
        return roles;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String getPhonenumber() {
        return phonenumber;
    }

    public @NotNull String getLogin() {
        return login;
    }

    public @NotNull String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO entity = (UserDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.surname, entity.surname) &&
                Objects.equals(this.email, entity.email) &&
                Objects.equals(this.birthday, entity.birthday) &&
                Objects.equals(this.roles, entity.roles) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.phonenumber, entity.phonenumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surname, email, birthday, roles, name, phonenumber);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "surname = " + surname + ", " +
                "email = " + email + ", " +
                "birthday = " + birthday + ", " +
                "roles = " + roles + ", " +
                "name = " + name + ", " +
                "phonenumber = " + phonenumber + ")";
    }
}