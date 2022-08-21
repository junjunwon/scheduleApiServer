package com.schedule.web;

import com.schedule.dto.file.FileInfoSaveRequestDto;
import com.schedule.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FileApiController {

    private final FileService fileService;

    @PostMapping("/api/saveFile")
    public Long save(@RequestBody FileInfoSaveRequestDto fileInfoSaveRequestDto) {
        return fileService.save(fileInfoSaveRequestDto);
    }
}
