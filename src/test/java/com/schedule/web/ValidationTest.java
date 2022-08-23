package com.schedule.web;


import com.schedule.dto.file.FileInfoSaveRequestDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationTest {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    public static void close() {
        factory.close();
    }

    @DisplayName("저장 시 빈 문자열일 경우 에러 발생")
    @Test
    void validation_blank_test() {
        FileInfoSaveRequestDto fileInfoSaveRequestDto =
                new FileInfoSaveRequestDto(null, 1, 1, 1,1, 1);

        Set<ConstraintViolation<FileInfoSaveRequestDto>> violations = validator.validate(fileInfoSaveRequestDto);

        assertThat(violations).isNotEmpty();
        violations
                .forEach(error -> {
                    System.out.println(error.getMessage());
                    assertThat(error.getMessage()).isEqualTo("TIME_IS_MANDATORY");
                });

    }

    // TODO : localdatetime formatter을 맞춰주던, String으로 변환해서 전달하던 validation체크가 동작하지 않음.
    @DisplayName("저장 시 날짜 형식이 잘못된 경우에 에러 발생")
    @Test
    void validation_dateformat_test() {

        String dateTime = "2018-09-25T10:13:14.743";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME);

        FileInfoSaveRequestDto fileInfoSaveRequestDto =
                new FileInfoSaveRequestDto(time, 1, 1, 1,1, 1);

        Set<ConstraintViolation<FileInfoSaveRequestDto>> violations = validator.validate(fileInfoSaveRequestDto);

        assertThat(violations).isNotEmpty();
        violations
                .forEach(error -> {
                    System.out.println(error.getMessage());
                    assertThat(error.getMessage()).isEqualTo("TIME_IS_MANDATORY");
                });
    }

    @DisplayName("저장 시 유효성 검사 완료(성공)")
    @Test
    void validation_complete_success_test() {
        String dateTime = "2018-09-25T10:13:14.743";
        LocalDateTime time = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME);

        FileInfoSaveRequestDto fileInfoSaveRequestDto =
                new FileInfoSaveRequestDto(time, 10, 20, 40000, 30000, 90000);

        Set<ConstraintViolation<FileInfoSaveRequestDto>> violations = validator.validate(fileInfoSaveRequestDto);

        assertThat(violations).isEmpty();
    }
}
