package com.schedule.service;

import com.schedule.domain.file.FileInfoRepository;
import com.schedule.dto.file.FileInfoSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService{
    private final FileInfoRepository fileInfoRepository;

    @Transactional
    public Long save(FileInfoSaveRequestDto fileInfoSaveRequestDto) {
        return fileInfoRepository.save(fileInfoSaveRequestDto.toEntity()).getId();
    }
}
