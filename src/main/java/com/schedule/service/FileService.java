package com.schedule.service;

import com.schedule.dto.file.FileInfoSaveRequestDto;

public interface FileService {
    public Long save(FileInfoSaveRequestDto fileInfoSaveRequestDto);
}
