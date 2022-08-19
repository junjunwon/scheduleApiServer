package com.schedule.scheduleApiServer.common.schedule;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@Component
public class Scheduler {

    private final HashMap<String, String> map = new HashMap<>();
    private String fileExtension = "";
    private String delimiter = "";
    public Scheduler() {
        map.put("txt", "|");
        map.put("csv", ",");
    }

    //    @Scheduled(cron = "0 0 00 * * *", zone = "Asia/Seoul")
    @Scheduled(fixedRate = 100000)
    public void getFileListScheduler() {

        ClassPathResource resource = new ClassPathResource("files/inputList.txt");
        try {

            //확장자 추출
            fileExtension = getExtensionByGuava(resource.getFilename());
            //구분자 추출
            delimiter = map.get(fileExtension);

            Path path = Paths.get(resource.getURI());
            List<String> contentList = Files.readAllLines(path);
            contentList.forEach((content) -> {

            });
        } catch (IOException e) {

        }

    }

    private String getExtensionByGuava(String fileName) {
        return com.google.common.io.Files.getFileExtension(fileName);
    }
}
