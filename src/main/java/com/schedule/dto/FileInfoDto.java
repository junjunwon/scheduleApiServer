package com.schedule.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FileInfoDto {
    private Long id;

    private LocalDateTime time; //시간

    private int joinMemberCnt; //가입자수

    private int leaveMemberCnt; //탈퇴자수

    private int payment; //결제금액

    private int cost; //사용금액

    private int revenue; //매출
}
