package com.schedule.common.object;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
@Setter
public class Response {
    private HttpStatus status;
    private String message;
    private Object data;
    private long availableTokens;
    private Error error = new Error();

    public Response() {
    }

    @Getter
    @Setter
    class Error {
        private String code;
        private String msg;
    }

    public void setErrorMsg(String msg) {
        this.error.setMsg(msg);
    }

    public void setErrorMsg(Exception e) {
        Optional<Throwable> rootCause = Stream.iterate(e, Throwable::getCause)
                .filter(element -> element.getCause() == null)
                .findFirst();

        if(rootCause.isEmpty())
            this.error.setMsg(e.getMessage());
        else
            this.error.setMsg(rootCause.toString());
    }

    public void setErrorCode(String code) {
        this.error.setCode(code);
    }

    public String getErrorCode() {
        return this.error.getCode();
    }

    public String getErrorMsg() {
        return this.error.getMsg();
    }
}
