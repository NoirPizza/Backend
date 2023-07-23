package com.pizza.pizzashop.security;

import com.pizza.pizzashop.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    private Integer id;

    private String login;

    private String email;

    private String phonenumber;

    private String password;

    private String name;

    private String surname;

    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(
            Integer id,
            String login,
            String email,
            String phonenumber,
            String password,
            String name,
            String surname,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.phonenumber = phonenumber;
        this.password = password;
        this.name = name;
        this.surname = surname;
        if (authorities == null) {
            this.authorities = null;
        } else {
            this.authorities = new ArrayList<>(authorities);
        }
    }

    public static CustomUserDetails create(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

        return new CustomUserDetails(
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getPhonenumber(),
                user.getPassword(),
                user.getName(),
                user.getSurname(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities == null ? null : new ArrayList<>(authorities);
    }

    public Integer getId() {
        return this.id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        CustomUserDetails that = (CustomUserDetails) object;
        return Objects.equals(id, that.id);
    }

    public int hashCode() {
        return Objects.hash(login);
    }
}
