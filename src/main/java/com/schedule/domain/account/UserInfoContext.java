package com.schedule.domain.account;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserInfoContext extends User {

    private final UserInfo userInfo;

    public UserInfoContext(UserInfo userInfo, Collection<? extends GrantedAuthority> authorities) {
        super(userInfo.getUsername(), userInfo.getPassword(), authorities);
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
