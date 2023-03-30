package com.kkm.talkbytag.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class User implements UserDetails {

    private String id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public User() {
        // empty constructor needed by Spring Data MongoDB
    }

    public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부를 체크하는 로직을 구현하면 됩니다.
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠김 여부를 체크하는 로직을 구현하면 됩니다.
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명(비밀번호) 만료 여부를 체크하는 로직을 구현하면 됩니다.
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 사용 가능 여부를 체크하는 로직을 구현하면 됩니다.
    }
}