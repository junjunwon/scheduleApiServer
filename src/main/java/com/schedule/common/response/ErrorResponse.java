package com.schedule.common.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ErrorResponse {

    private final String statusCode;
    private final String errorContent;
    private final Map<String, String> validation; //초기값이 null이기 때문에.

    @Builder
    public ErrorResponse(String statusCode, String errorContent, Map<String, String> validation) {
        this.statusCode = statusCode;
        this.errorContent = errorContent;
        this.validation = validation;
    }

    public void addValidation(String field, String errorMessage) {
        this.validation.put(field, errorMessage);
    }
}
