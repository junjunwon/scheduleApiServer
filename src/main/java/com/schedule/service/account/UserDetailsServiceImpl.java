package com.schedule.service.account;

import com.schedule.domain.account.UserInfo;
import com.schedule.domain.account.UserInfoContext;
import com.schedule.domain.account.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService{

    private final UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserInfo userInfo = userInfoRepository.findByUsername(username);

        if(ObjectUtils.isEmpty(userInfo)) {
            throw new UsernameNotFoundException("USERNAME_NOT_FOUND_EXCEPTION");
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(userInfo.getRole()));

        UserInfoContext userInfoContext = new UserInfoContext(userInfo, roles);

        return userInfoContext;
    }
}
