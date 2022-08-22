package com.schedule.dto.file;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FileInfoUpdateRequestDto {

    private int joinMemberCnt; //가입자수
    private int leaveMemberCnt; //탈퇴자수
    private int payment; //결제금액
    private int cost; //사용금액
    private int revenue; //매출

    @Builder
    public FileInfoUpdateRequestDto(
            int joinMemberCnt,
            int leaveMemberCnt,
            int payment,
            int cost,
            int revenue
    ) {
        this.joinMemberCnt = joinMemberCnt;
        this.leaveMemberCnt = leaveMemberCnt;
        this.payment = payment;
        this.cost = cost;
        this.revenue = revenue;
    }
}
