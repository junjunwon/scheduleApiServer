package com.schedule.dto.file;


import com.schedule.domain.file.FileInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FileInfoSaveRequestDto {
    @NotNull(message = "TIME_IS_MANDATORY")
    private LocalDateTime time; //시간
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
