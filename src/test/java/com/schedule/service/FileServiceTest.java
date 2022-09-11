package com.schedule.service;

import com.schedule.domain.file.FileInfo;
import com.schedule.domain.file.FileInfoCustomRepository;
import com.schedule.domain.file.FileInfoRepository;
import com.schedule.dto.file.FileInfoResponseDto;
import com.schedule.service.file.FileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class FileServiceTest {

    @Autowired
    FileService fileService;

    @Autowired
    FileInfoRepository repository;

    @Autowired
    FileInfoCustomRepository customRepository;

    @BeforeEach
    void cleanAll() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("파일에 내용 저장 테스트")
    void saveContent() {

        FileInfo request =
                FileInfo.builder()
                        .time(LocalDateTime.now()) //시간
                        .joinMemberCnt(32) //가입자수
                        .leaveMemberCnt(40) //탈퇴자수
                        .payment(50_000) //결제금액
                        .cost(40_000) //사용금액
                        .revenue(30_000) //매출
                        .build();

        repository.save(request);

        List<FileInfoResponseDto> response = fileService.findFileList();

        Assertions.assertEquals(request.getCost(), response.get(0).getCost());


    }
}
