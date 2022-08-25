package com.schedule.service.account;


import com.schedule.domain.account.UserInfo;
import com.schedule.dto.account.UserInfoSaveRequestDto;
import org.springframework.transaction.annotation.Transactional;

public interface UserInfoService {

    /**
     * 계정 생성
     *
     * @author jh.won
     * @since 2022.08.24
     * @param requestDto
     * @return
     */
    public Long createUser(UserInfoSaveRequestDto requestDto);
}
