package com.schedule.domain.file;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> { //Long : PK type
}
