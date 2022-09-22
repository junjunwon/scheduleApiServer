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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileApiControllerTest {

    private static final Logger logger = LogManager.getLogger(FileApiControllerTest.class);

    @Autowired
    ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

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
//        String url = "http://localhost:"+port+"/api/file/save";
        this.mvc.perform(get("/api/file/save"))
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

        mvc.perform(post("/api/file/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print());

        List<FileInfo> all = fileInfoRepository.findAll();

        assertThat(all.get(0).getRevenue(), is(equalTo(revenue)));
        assertThat(all.get(0).getPayment(), is(equalTo(payment)));
    }

    @WithMockUser(roles="USER")
    @Test
    @DisplayName("Content 수정 테스트")
    public void editContent() throws Exception {

        FileInfo savedFileInfo = fileInfoRepository.save(FileInfo.builder()
                .time(LocalDateTime.now())
                .joinMemberCnt(32)
                .leaveMemberCnt(40)
                .payment(50_000)
                .cost(40_000)
                .revenue(30_000)
                .build());

        Long updateId = savedFileInfo.getId();

        FileInfoUpdateRequestDto update =
                FileInfoUpdateRequestDto.builder()
                        .joinMemberCnt(99)
                        .leaveMemberCnt(99)
                        .payment(99_999)
                        .cost(99_999)
                        .revenue(77_777)
                        .build();

        mvc.perform(patch("/api/file/update/{updateId}", updateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andDo(print());

        FileInfo fileInfo =  fileInfoCustomRepository.findFileInfoById(updateId);

        assertThat(fileInfo.getPayment(), is(equalTo(update.getPayment())));
        assertThat(fileInfo.getCost(), is(equalTo(update.getCost())));
        assertThat(fileInfo.getRevenue(), is(equalTo(update.getRevenue())));
        assertThat(fileInfo.getLeaveMemberCnt(), is(equalTo(update.getLeaveMemberCnt())));
        assertThat(fileInfo.getJoinMemberCnt(), is(equalTo(update.getJoinMemberCnt())));
    }

    @WithMockUser(roles="USER")
    @Test
    @DisplayName("Content 수정 테스트 null값 전송 시 InvalidException 처리 테스트")
    public void failTestForEditContent() throws Exception {

        FileInfo savedFileInfo = fileInfoRepository.save(FileInfo.builder()
                .time(LocalDateTime.now())
                .joinMemberCnt(32)
                .leaveMemberCnt(40)
                .payment(50_000)
                .cost(40_000)
                .revenue(30_000)
                .build());

        Long updateId = savedFileInfo.getId();

        FileInfoUpdateRequestDto update =
                FileInfoUpdateRequestDto.builder()
                        .joinMemberCnt(-1)
                        .leaveMemberCnt(99)
                        .payment(99_999)
                        .cost(99_999)
                        .revenue(77_777)
                        .build();

        mvc.perform(patch("/api/file/update/{updateId}", updateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @WithMockUser(roles="USER")
    @Test
    public void Contents_삭제한다() throws Exception {

        //given
        List<FileInfo> requestFileInfos = IntStream.range(1,5)
                .mapToObj(i -> {
                    return FileInfo.builder()
                            .joinMemberCnt(10 + i)
                            .leaveMemberCnt(20 + i)
                            .cost(10_000 + i)
                            .time(LocalDateTime.now())
                            .payment(90_000 + i)
                            .revenue(300 + i)
                            .build();
                }).toList();
        fileInfoRepository.saveAll(requestFileInfos);

        mvc.perform(delete("/api/file/delete/{deleteId}", requestFileInfos.get(0).getId()))
                .andExpect(status().isOk())
                .andDo(print());

    }
}
