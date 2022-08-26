package com.schedule.common.config;

import com.schedule.domain.account.UserInfoContext;
import com.schedule.service.account.UserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.HashSet;
import java.util.Set;

class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    Set<String> allowedIpList = new HashSet<String>();

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${allowed-ips}")
    private Set<String> allowedIps;

    public CustomAuthenticationProvider() {
        allowedIpList.add("0:0:0:0:0:0:0:1");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String userIp = details.getRemoteAddress();
        logger.info("Client Ip is ....{}", userIp);
        if(!allowedIps.contains(userIp)) {
            throw new BadCredentialsException("Invalid IP Address");
        }

        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();

        UserInfoContext userInfoContext = (UserInfoContext) userDetailsService.loadUserByUsername(username);

        if(!passwordEncoder.matches(password, userInfoContext.getUserInfo().getPassword())) {
            throw new BadCredentialsException("BadCredentialsException");
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userInfoContext.getUserInfo(), null, userInfoContext.getAuthorities());

        return  authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
