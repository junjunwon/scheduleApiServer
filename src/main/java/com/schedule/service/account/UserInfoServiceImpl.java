package com.schedule.service.account;


import com.schedule.domain.account.UserInfo;
import com.schedule.domain.account.UserInfoRepository;
import com.schedule.dto.account.UserInfoSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoRepository userInfoRepository;

    @Override
    public Long createUser(UserInfoSaveRequestDto requestDto) {
        UserInfo userInfo = requestDto.toEntity();
        userInfoRepository.save(userInfo);

        return userInfo.getId();
    }
}
