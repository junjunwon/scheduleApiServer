package com.schedule.dto.file;

import com.schedule.domain.file.FileInfo;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FileInfoResponseDto {
    private LocalDateTime time; //시간
    private int joinMemberCnt; //가입자수
    private int leaveMemberCnt; //탈퇴자수
    private int payment; //결제금액
    private int cost; //사용금액
    private int revenue; //매출

    public FileInfoResponseDto(FileInfo entity) {
        this.time = entity.getTime();
        this.joinMemberCnt = entity.getJoinMemberCnt();
        this.leaveMemberCnt = entity.getLeaveMemberCnt();
        this.payment = entity.getPayment();
        this.cost = entity.getCost();
        this.revenue = entity.getRevenue();
    }
}
