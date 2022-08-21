package com.schedule.domain.file;

import java.time.LocalDateTime;
import java.util.List;

public interface FileInfoCustomRepository {

    /**
     * 전체 데이터 조회
     *
     * @author jh.won
     * @since 2022.08.21
     * @return
     */
    List<FileInfo> findFileList();

    /**
     * 시간대별 가입자 수 조회
     *
     * @author jh.won
     * @since 2022.08.21
     * @return
     */

    /**
     * 시간대별 결제금액 조회
     *
     * @author jh.won
     * @since 2022.08.21
     * @return
     */

    /**
     * DB에 같은 시간이 있는지 체크
     *
     * @author jh.won
     * @since 2022.08.21
     * @return
     */
    int findEqualDateTime(LocalDateTime time);

    /**
     * 전체 데이터 조회
     *
     * @author jh.won
     * @since 2022.08.21
     * @return
     */

}
