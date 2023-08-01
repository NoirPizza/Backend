package com.pizza.pizzashop.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;

/**
 * This class represents a user entity in the system.
 * It is annotated with JPA annotations to map the class to the corresponding database table.
 * It also defines indexes to improve database query performance for specific columns.
 */
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
    private Long id;

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
    private String phoneNumber;

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

    public User(
            Long id,
            String login,
            String password,
            String name,
            String surname,
            String phoneNumber,
            String email,
            String birthday,
            Instant createdAt,
            Instant updatedAt
    ) {

        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthday = birthday;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public @NotNull String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
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