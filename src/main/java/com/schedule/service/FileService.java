package com.schedule.service;

import com.schedule.dto.file.FileInfoResponseDto;
import com.schedule.dto.file.FileInfoSaveRequestDto;
import com.schedule.dto.file.FileInfoUpdateRequestDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FileService {

    /**
     * 전체 조회
     * readonly : 조회 속도 개선
     *
     * @author jh.won
     * @since 2022.08.22
     * @return
     */
    @Transactional(readOnly = true)
    List<FileInfoResponseDto> findFileList();

    /**
     * content 저장
     *
     * @author jh.won
     * @since 2022.08.22
     * @param fileInfoSaveRequestDto
     * @return
     */
    @Transactional
    Long save(FileInfoSaveRequestDto fileInfoSaveRequestDto);

    /**
     * content 단일/다중 삭제
     *
     * @author jh.won
     * @since 2022.08.22
     * @param ids
     * @return
     */
    @Transactional
    Long deleteContentsByIds(List<Long> ids);

    /**
     * content 수정
     *
     * @author jh.won
     * @since 2022.08.22
     * @param id
     * @param fileInfoUpdateRequestDto
     * @return
     */
    @Transactional
    Long updateContentById(Long id, FileInfoUpdateRequestDto fileInfoUpdateRequestDto);
}
