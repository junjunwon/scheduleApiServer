package com.schedule.web.exception;

import com.schedule.common.response.ErrorResponse;
import com.schedule.exception.RootException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Slf4j
@ControllerAdvice
public class ExceptionController {

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> invalidRequestHandler(MethodArgumentNotValidException e) {
        ErrorResponse body = ErrorResponse.builder()
                .statusCode("400")
                .errorContent("잘못된 요청입니다.")
                .validation(new HashMap<>())
                .build();

        for(FieldError fieldError : e.getFieldErrors()) {
            body.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ResponseEntity<ErrorResponse> response = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(body);

        return response;
    }


//    //TODO : 누락된 Exception이 있을 경우 해당 Exception 로직을 수행하여, 어떤 예외처리가 있는지 파악.
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public void basicException(Exception e) {
//        log.info("Exception 처리가 필요합니다! Exception is {}", e);
//    }

    @ExceptionHandler(RootException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> rootException(RootException e) {

        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .statusCode(String.valueOf(statusCode))
                .errorContent(e.getMessage())
                .validation(e.getValidation())
                .build();

        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
                .body(body);

        return response;

    }
}
