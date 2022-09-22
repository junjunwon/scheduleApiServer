package com.schedule.dto.file;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileInfoRequestDto {

    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

    public long offset() {
        return (long) (Math.max(1, page) -1) * Math.max(MAX_SIZE, size);
    }
}
