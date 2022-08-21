package com.schedule.domain;


import com.schedule.domain.file.FileInfo;
import com.schedule.domain.file.FileInfoCustomRepositoryImpl;
import com.schedule.domain.file.FileInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FileInfoRepositoryTest {

    @Autowired
    FileInfoRepository fileInfoRepository;

    @Autowired
    FileInfoCustomRepositoryImpl fileInfoCustomRepository;

    @AfterEach
    public void cleanup() {
        fileInfoRepository.deleteAll();
    }

    @Test
    public void 파일저장_불러오기() {
        LocalDateTime time = LocalDateTime.now(); //시간
        int joinMemberCnt = 32; //가입자수
        int leaveMemberCnt = 40; //탈퇴자수
        int payment = 50_000; //결제금액
        int cost = 40_000; //사용금액
        int revenue = 30_000; //매출

        fileInfoRepository.save(FileInfo.builder()
                .time(time)
                .joinMemberCnt(joinMemberCnt)
                .leaveMemberCnt(leaveMemberCnt)
                .payment(payment)
                .cost(cost)
                .revenue(revenue)
                .build());

        List<FileInfo> fileInfoList = fileInfoRepository.findAll();

        FileInfo fileInfo = fileInfoList.get(0);
        assertThat(fileInfo.getTime(), is(equalTo(time)));
        assertThat(fileInfo.getJoinMemberCnt(), is(equalTo(joinMemberCnt)));
        assertThat(fileInfo.getLeaveMemberCnt(), is(equalTo(leaveMemberCnt)));
        assertThat(fileInfo.getPayment(), is(equalTo(payment)));
        assertThat(fileInfo.getCost(), is(equalTo(cost)));
        assertThat(fileInfo.getRevenue(), is(equalTo(revenue)));
    }

    @DisplayName("전체 데이터 조회 테스트")
    @Test
    public void queryDSL_fetch_all_data() {
//        EntityManager entityManager = EntityManagerFactory.class.
        List<FileInfo> list = fileInfoCustomRepository.findFileList();

        System.out.print("abc");
    }
}
