package com.schedule.domain.file;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
public class FileInfoEditor {
    private final int joinMemberCnt; //가입자수
    private final int leaveMemberCnt; //탈퇴자수
    private final int payment; //결제금액
    private final int cost; //사용금액
    private final int revenue; //매출

    @Builder
    public FileInfoEditor(int joinMemberCnt, int leaveMemberCnt, int payment, int cost, int revenue) {
        this.joinMemberCnt = joinMemberCnt;
        this.leaveMemberCnt = leaveMemberCnt;
        this.payment = payment;
        this.cost = cost;
        this.revenue = revenue;
    }
}
