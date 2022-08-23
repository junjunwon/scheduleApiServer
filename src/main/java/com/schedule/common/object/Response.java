package com.schedule.common.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
public class Response {
    private HttpStatus status;
    private String message;
    private Object data;
    private long availableTokens;
}
