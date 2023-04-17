package ru.ntv.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.ntv.entity.users.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {

    private String login;

    private String password;


    private GrantedAuthority authority;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(authority);
    }

    public MyUserDetails(String login, String password, GrantedAuthority authority) {
        this.login = login;
        this.password = password;
        this.authority = authority;
    }

    public static MyUserDetails build(User user) {

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRoleName());

        return new MyUserDetails(user.getLogin(), user.getPassword(), authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
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


}