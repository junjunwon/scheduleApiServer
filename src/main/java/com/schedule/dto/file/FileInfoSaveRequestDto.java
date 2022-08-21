package com.schedule.dto.file;


import com.schedule.domain.file.FileInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FileInfoSaveRequestDto {
    private LocalDateTime time; //시간
    private int joinMemberCnt; //가입자수
    private int leaveMemberCnt; //탈퇴자수
    private int payment; //결제금액
    private int cost; //사용금액
    private int revenue; //매출

    @Builder
    public FileInfoSaveRequestDto(LocalDateTime time, int joinMemberCnt, int leaveMemberCnt,
                                  int payment, int cost, int revenue) {
        this.time = time;
        this.joinMemberCnt = joinMemberCnt;
        this.leaveMemberCnt = leaveMemberCnt;
        this.payment = payment;
        this.cost = cost;
        this.revenue = revenue;
    }

    public FileInfo toEntity() {
        return FileInfo.builder()
                .time(time)
                .joinMemberCnt(joinMemberCnt)
                .leaveMemberCnt(leaveMemberCnt)
                .payment(payment)
                .cost(cost)
                .revenue(revenue)
                .build();
    }
}
