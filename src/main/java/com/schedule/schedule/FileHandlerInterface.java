package com.schedule.schedule;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
public interface FileHandlerInterface {
    //확장자 파라미터로 받아서 delimeter return
    String getDelimiter();

    default String getExtensionByGuava(String fileName) {
        return com.google.common.io.Files.getFileExtension(fileName);
    }

    //TODO: dir경로에 files 중 일치하는 확장자에 한해서만 파일리스트를 생성하도록 수정. <- 현재는 단일 파일만 가져오도록 로컬에 적용
    default List<Path> getListFiles(Path path) throws IOException {
        List<Path> getAllFiles;

        try(Stream<Path> walk = Files.walk(path)) {
            getAllFiles = walk.filter(Files::isRegularFile).toList();
        }
//        for(int i = 0; i < getAllFiles.size(); i++) {
//            Path element = getAllFiles.get(i);
//            fileExtension = getExtensionByGuava(element.getFileName().toString());
//
//            if(map.containsKey(fileExtension)) result.add(element);
//        }
        return getAllFiles;
    }

    List<String> getReadAllLines(Path path) throws IOException;

    void appendWriteToFile(Path path, String line) throws IOException;
}
