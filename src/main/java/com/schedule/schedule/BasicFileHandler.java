package com.schedule.schedule;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * txt, csv 파일 read && write
 */
@Component
public class BasicFileHandler implements FileHandlerInterface {
    @Override
    public String getDelimiter() {
        return "|";
    }

    @Override
    public List<String> getReadAllLines(Path path) throws IOException {
        return Files.readAllLines(path);
    }

    @Override
    public void appendWriteToFile(Path path, String line) throws IOException {
        Files.write(path, line.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

}
