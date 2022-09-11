package com.schedule.web.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedule.domain.account.UserInfo;
import com.schedule.domain.account.UserInfoRepository;
import com.schedule.dto.account.UserInfoSaveRequestDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserInfoControllerTest {

    private static final Logger logger = LogManager.getLogger(UserInfoControllerTest.class);

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @AfterEach
    public void cleanup() {
        userInfoRepository.deleteAll();
    }

    @BeforeAll
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles="USER")
    public void USER_등록된다() throws Exception {
        String username = "testUser";
        String password = "testUser";
        String role = "ROLE_USER";

        UserInfoSaveRequestDto requestDto = UserInfoSaveRequestDto.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();

        mvc.perform(post("/api/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print());

        List<UserInfo> all = userInfoRepository.findAll();

        assertEquals(all.get(0).getUsername(), username);
        assertEquals(all.get(0).getRole(), role);
    }

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("유저 등록 시 유저 이름이 없을 경우 InvalidRequest Exception 처리")
    public void failTestForcreateUser() throws Exception {

        UserInfoSaveRequestDto requestDto = UserInfoSaveRequestDto.builder()
                .password("testUser")
                .role("ROLE_USER")
                .build();

        mvc.perform(post("/api/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles="USER")
    @DisplayName("유저 등록 시 비밀번호가 없을 경우 InvalidRequest Exception 처리")
    public void failTestForcreateUser2() throws Exception {

        UserInfoSaveRequestDto requestDto = UserInfoSaveRequestDto.builder()
                .username("testUser")
                .role("ROLE_USER")
                .build();

        mvc.perform(post("/api/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles="GUEST")
    @DisplayName("유저 등록 시 권한이 없을 경우 실패 테스트")
    public void failTestForAuthority() throws Exception {

        UserInfoSaveRequestDto requestDto = UserInfoSaveRequestDto.builder()
                .username("testUser")
                .role("ROLE_USER")
                .build();

        mvc.perform(post("/api/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
}

