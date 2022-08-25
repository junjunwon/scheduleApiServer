package com.schedule.service.file;

import com.schedule.domain.file.FileInfo;
import com.schedule.domain.file.FileInfoCustomRepository;
import com.schedule.domain.file.FileInfoRepository;
import com.schedule.dto.file.FileInfoResponseDto;
import com.schedule.dto.file.FileInfoSaveRequestDto;
import com.schedule.dto.file.FileInfoUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService{
    private final FileInfoRepository fileInfoRepository;

    private final FileInfoCustomRepository fileInfoCustomRepository;

    @Transactional(readOnly = true)
    @Override
    public List<FileInfoResponseDto> findFileList() {
        List<FileInfo> entityList = fileInfoCustomRepository.findFileList();
        List<FileInfoResponseDto> fileInfoResponseDtos =
                entityList.stream().map(FileInfoResponseDto::new).collect(Collectors.toList());
        return fileInfoResponseDtos;
    }

    @Transactional
    @Override
    public Long save(FileInfoSaveRequestDto fileInfoSaveRequestDto) {
        return fileInfoRepository.save(fileInfoSaveRequestDto.toEntity()).getId();
    }

    @Transactional
    @Override
    public Long deleteContentsByIds(List<Long> ids) {
        return fileInfoCustomRepository.deleteByIds(ids);
    }

    @Transactional
    @Override
    public Long updateContentById(Long id, FileInfoUpdateRequestDto fileInfoUpdateRequestDto) {

        FileInfo fileInfo = fileInfoCustomRepository.findFileInfoById(id);
        if(!ObjectUtils.isEmpty(fileInfo)) {
            fileInfo.update(
                    fileInfoUpdateRequestDto.getJoinMemberCnt(),
                    fileInfoUpdateRequestDto.getLeaveMemberCnt(),
                    fileInfoUpdateRequestDto.getPayment(),
                    fileInfoUpdateRequestDto.getCost(),
                    fileInfoUpdateRequestDto.getRevenue()
            );
            return id;
        }
        return 0L;
    }
}
