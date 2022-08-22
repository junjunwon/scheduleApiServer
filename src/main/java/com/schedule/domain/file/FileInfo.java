package com.schedule.domain.file;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Getter
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32, nullable = false)
    private LocalDateTime time; //시간

    @Column(nullable = false)
    private int joinMemberCnt; //가입자수

    @Column(nullable = false)
    private int leaveMemberCnt; //탈퇴자수

    @Column(nullable = false)
    private int payment; //결제금액

    @Column(nullable = false)
    private int cost; //사용금액

    @Column(nullable = false)
    private int revenue; //매출

    @Builder
    public FileInfo(LocalDateTime time, int joinMemberCnt, int leaveMemberCnt,
                    int payment, int cost, int revenue) {
        this.time = time;
        this.joinMemberCnt = joinMemberCnt;
        this.leaveMemberCnt = leaveMemberCnt;
        this.payment = payment;
        this.cost = cost;
        this.revenue = revenue;
    }

    public void update(
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
