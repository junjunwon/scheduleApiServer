package com.schedule.domain.file;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.schedule.domain.file.QFileInfo.fileInfo;

@Repository
public class FileInfoCustomRepositoryImpl implements FileInfoCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public FileInfoCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<FileInfo> findFileList() {
        return jpaQueryFactory
                .selectFrom(fileInfo)
                .fetch();
    }

    public int findEqualDateTime(LocalDateTime time) {
        int check =  jpaQueryFactory
                .selectFrom(fileInfo)
                .where(fileInfo.time.eq(time))
                .limit(1)
                .fetch().size();

        return check;
    }
}