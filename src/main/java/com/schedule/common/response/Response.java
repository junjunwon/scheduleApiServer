package com.schedule.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class Response {
    private HttpStatus status;
    private String message;
    private Object data;
    private long availableTokens;
}
