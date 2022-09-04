package com.schedule.web.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.schedule.domain.file.FileInfo;
import com.schedule.domain.file.FileInfoCustomRepository;
import com.schedule.domain.file.FileInfoRepository;
import com.schedule.dto.file.FileInfoSaveRequestDto;
import com.schedule.dto.file.FileInfoUpdateRequestDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileApiControllerTest {

    private static final Logger logger = LogManager.getLogger(FileApiControllerTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    private FileInfoCustomRepository fileInfoCustomRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;


    @AfterEach
    public void cleanup() {
        fileInfoRepository.deleteAll();
    }

    @BeforeAll
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void unAuthenticatedUserThenReturn401() throws Exception {
        String url = "http://localhost:"+port+"/api/file/save";
        this.mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles="USER")
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

        String url = "http://localhost:"+port+"/api/file/save";

        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        List<FileInfo> all = fileInfoRepository.findAll();

        assertThat(all.get(0).getRevenue(), is(equalTo(revenue)));
        assertThat(all.get(0).getPayment(), is(equalTo(payment)));
    }

    @WithMockUser(roles="USER")
    @Test
    public void Content_수정된다() throws Exception {

        FileInfo savedFileInfo = fileInfoRepository.save(FileInfo.builder()
                .time(LocalDateTime.now())
                .joinMemberCnt(32)
                .leaveMemberCnt(40)
                .payment(50_000)
                .cost(40_000)
                .revenue(30_000)
                .build());

        Long updateId = savedFileInfo.getId();

        int joinMemberCnt = 99; //가입자수
        int leaveMemberCnt = 99; //탈퇴자수
        int payment = 99_999; //결제금액
        int cost = 88_888; //사용금액
        int revenue = 77_777; //매출

        FileInfoUpdateRequestDto updateRequestDto =
                FileInfoUpdateRequestDto.builder()
                        .joinMemberCnt(joinMemberCnt)
                        .leaveMemberCnt(leaveMemberCnt)
                        .payment(payment)
                        .cost(cost)
                        .revenue(revenue)
                        .build();

        String url = "http://localhost:"+port+"/api/file/update/"+updateId;

        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk());

        FileInfo fileInfo =  fileInfoCustomRepository.findFileInfoById(updateId);

        assertThat(fileInfo.getPayment(), is(equalTo(payment)));
        assertThat(fileInfo.getCost(), is(equalTo(cost)));
        assertThat(fileInfo.getRevenue(), is(equalTo(revenue)));
        assertThat(fileInfo.getLeaveMemberCnt(), is(equalTo(leaveMemberCnt)));
        assertThat(fileInfo.getJoinMemberCnt(), is(equalTo(joinMemberCnt)));
    }

    @WithMockUser(roles="USER")
    @Test
    public void Contents_삭제한다() throws Exception {
        String url = "http://localhost:"+port+"/api/file/delete/1";

        mvc.perform(delete(url))
                .andExpect(status().isOk());

    }
}
