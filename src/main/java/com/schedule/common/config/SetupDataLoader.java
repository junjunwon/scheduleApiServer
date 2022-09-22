package com.schedule.common.config;

import com.schedule.dto.account.UserInfoSaveRequestDto;
import com.schedule.service.account.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(SetupDataLoader.class);
    boolean alreadySetup = false;

    private final UserInfoService userInfoService;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("SetupDataLoader onApplicationEvent...");
        if(alreadySetup) return;

        UserInfoSaveRequestDto userInfoSaveRequestDto = UserInfoSaveRequestDto.builder()
                .username("admin")
                .password("P@ssw0rd")
                .role("ROLE_USER")
                .build();

        Long index = userInfoService.createUser(userInfoSaveRequestDto);

        if(index > 0) alreadySetup =true;

    }
}
