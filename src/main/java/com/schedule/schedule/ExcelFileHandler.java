package com.schedule.schedule;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class ExcelFileHandler implements FileHandlerInterface {
    @Override
    public String getDelimiter() {
        return ",";
    }

    @Override
    public List<String> getReadAllLines(Path path) throws IOException {
        return null;
    }

    @Override
    public void appendWriteToFile(Path path, String line) throws IOException {

    }
}
