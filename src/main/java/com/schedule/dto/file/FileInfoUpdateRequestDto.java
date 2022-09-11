package com.schedule.dto.file;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@NoArgsConstructor
public class FileInfoUpdateRequestDto {
    @NotNull(message = "VALUE_IS_MANDATORY")
    @PositiveOrZero
    private int joinMemberCnt; //가입자수
    @NotNull(message = "VALUE_IS_MANDATORY")
    @PositiveOrZero
    private int leaveMemberCnt; //탈퇴자수
    @NotNull(message = "VALUE_IS_MANDATORY")
    @PositiveOrZero
    private int payment; //결제금액
    @NotNull(message = "VALUE_IS_MANDATORY")
    @PositiveOrZero
    private int cost; //사용금액
    @NotNull(message = "VALUE_IS_MANDATORY")
    @PositiveOrZero
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
