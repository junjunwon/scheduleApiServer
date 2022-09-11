package com.schedule.exception;

import lombok.Getter;

@Getter
public class BucketExceedException extends RootException {

    private static final String MESSAGE = "Validation failure";

    /**
     * 생성자 오버로딩
     */
    public BucketExceedException() {
        super(MESSAGE);
    }

    public BucketExceedException(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    public BucketExceedException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return 429;
    }
}
