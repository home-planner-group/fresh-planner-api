package com.freshplanner.api.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freshplanner.api.database.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * <h2>Implementation of {@link UserDetails}</h2>
 * <p>Uses {@link User} and adds the {@link GrantedAuthority} information.</p>
 */
public class UserAuthority implements UserDetails {

    private final String username;
    @JsonIgnore
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    private UserAuthority(User user) {
        this.username = user.getName();
        this.password = user.getPassword();
        this.authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
    }

    /**
     * @param user all information of the {@link User} will be used and from  {@link User#getRoles()} the 'authorities' will be built.
     * @return new instance of {@link UserAuthority}.
     */
    public static UserAuthority build(User user) {
        return new UserAuthority(user);
    }

    // =================================================================================================================

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAuthority user)) return false;
        return username.equals(user.getUsername());
    }
}
