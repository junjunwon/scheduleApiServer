package com.schedule.web;

import com.schedule.domain.file.FileInfo;
import com.schedule.domain.file.FileInfoRepository;
import com.schedule.dto.file.FileInfoSaveRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @AfterEach
    public void cleanup() {
        fileInfoRepository.deleteAll();
    }

    @Test
    public void File_등록된다() throws Exception {
        LocalDateTime time = LocalDateTime.now(); //시간
        int joinMemberCnt = 32; //가입자수
        int leaveMemberCnt = 40; //탈퇴자수
        int payment = 50_000; //결제금액
        int cost = 40_000; //사용금액
        int revenue = 30_000; //매출
        FileInfoSaveRequestDto requestDto = FileInfoSaveRequestDto.builder()
                .time(time)
                .joinMemberCnt(joinMemberCnt)
                .leaveMemberCnt(leaveMemberCnt)
                .payment(payment)
                .cost(cost)
                .revenue(revenue)
                .build();

        String url = "http://localhost:"+port+"/api/saveFile";

        ResponseEntity<Long> responseEntity = restTemplate
                .postForEntity(url, requestDto, Long.class);

        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.OK)));
        assertThat(responseEntity.getBody(), greaterThan(0L));

        List<FileInfo> all = fileInfoRepository.findAll();

        assertThat(all.get(0).getRevenue(), is(equalTo(revenue)));
        assertThat(all.get(0).getPayment(), is(equalTo(payment)));
    }
}
