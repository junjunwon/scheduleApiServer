package com.schedule.service.file;

import com.schedule.domain.file.FileInfo;
import com.schedule.domain.file.FileInfoCustomRepository;
import com.schedule.domain.file.FileInfoEditor;
import com.schedule.domain.file.FileInfoRepository;
import com.schedule.dto.file.FileInfoRequestDto;
import com.schedule.dto.file.FileInfoResponseDto;
import com.schedule.dto.file.FileInfoSaveRequestDto;
import com.schedule.dto.file.FileInfoUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService{
    private final FileInfoRepository fileInfoRepository;

    private final FileInfoCustomRepository fileInfoCustomRepository;

    @Transactional(readOnly = true)
    @Override
    public List<FileInfoResponseDto> findFileList(FileInfoRequestDto requestDto) {
        List<FileInfo> entityList = fileInfoCustomRepository.findFileList();
        List<FileInfoResponseDto> fileInfoResponseDtos =
                entityList.stream().map(FileInfoResponseDto::new).toList();
        return fileInfoResponseDtos;
    }

    @Transactional
    @Override
    public void save(FileInfoSaveRequestDto fileInfoSaveRequestDto) {
        fileInfoRepository.save(fileInfoSaveRequestDto.toEntity());
    }

    @Transactional
    @Override
    public void deleteContentsByIds(List<Long> ids) {
        fileInfoCustomRepository.deleteByIds(ids);
    }

    @Transactional
    @Override
    public void updateContentById(Long id, FileInfoUpdateRequestDto request) {

        FileInfo fileInfo = fileInfoCustomRepository.findFileInfoById(id);

        FileInfoEditor.FileInfoEditorBuilder fileInfoEditorBuilder = fileInfo.toEditor();

        FileInfoEditor fileInfoEditor = fileInfoEditorBuilder
                .leaveMemberCnt(request.getLeaveMemberCnt())
                .joinMemberCnt(request.getJoinMemberCnt())
                .payment(request.getPayment())
                .cost(request.getCost())
                .revenue(request.getRevenue())
                .build();

        fileInfo.update(fileInfoEditor);
    }
}
