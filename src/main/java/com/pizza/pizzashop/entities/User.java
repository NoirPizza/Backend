package com.pizza.pizzashop.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "\"user\"", indexes = {
        @Index(name = "user_login_key", columnList = "login", unique = true),
        @Index(name = "user_phonenumber_key", columnList = "phonenumber", unique = true),
        @Index(name = "user_email_key", columnList = "email", unique = true),
        @Index(name = "idx_phonenumber", columnList = "phonenumber"),
        @Index(name = "idx_email", columnList = "email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @NotBlank
    @Column(name = "login", nullable = false)
    private String login;

    @Size(max = 255)
    @NotNull
    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 20)
    @NotNull
    @NotBlank
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Size(max = 40)
    @Column(name = "surname", length = 40)
    private String surname;

    @Size(max = 12)
    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\+?\\d{11}$|^8\\d{10}$")
    @Column(name = "phonenumber", nullable = false, length = 12)
    private String phonenumber;

    @Size(max = 50)
    @Column(name = "email", length = 50)
    private String email;

    @Size(max = 10)
    @Column(name = "birthday", length = 10)
    private String birthday;

    @PastOrPresent
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PastOrPresent
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_on_user",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "roleid"))
    private List<Role> roles;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotNull String getLogin() {
        return login;
    }

    public void setLogin(@NotNull String login) {
        this.login = login;
    }

    public @NotNull String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public @NotNull String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(@NotNull String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public @NotNull Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NotNull Instant createdAt) {
        this.createdAt = createdAt;
    }

    public @NotNull Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(@NotNull Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}