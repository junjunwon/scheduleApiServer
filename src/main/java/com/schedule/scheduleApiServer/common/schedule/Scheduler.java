package com.schedule.scheduleApiServer.common.schedule;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class Scheduler {

    private static final Path path = Paths.get("src/main//resources/files/inputList.txt");

    private static final String NEWLINE = System.lineSeparator();
    private final HashMap<String, String> map = new HashMap<>();
    private String fileExtension = "";
    private String delimiter = "";

    public Scheduler() {
        map.put("txt", "|");
        map.put("csv", ",");
    }

    //    @Scheduled(cron = "0 0 00 * * *", zone = "Asia/Seoul")
    @Scheduled(fixedRate = 10000)
    public void getFileListScheduler() {


        StringBuffer stringBuffer = new StringBuffer();
        try {

            //확장자 추출
            fileExtension = getExtensionByGuava(path.getFileName().toString());
            //구분자 추출
            stringBuffer.append("[");
            stringBuffer.append(map.get(fileExtension));
            stringBuffer.append("]");
            delimiter = stringBuffer.toString();

            List<String> contentList = Files.readAllLines(path);
            contentList.forEach((content) -> {
                String[] contentLine = content.split(delimiter);
                //값이 누락된 경우
                if(contentLine.length != 6) {
                    //초기화
                    return;
                }
                //db에 저장
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Scheduled(cron = "0 0 0/1 * * *") //1시간마다 도는 스케줄러
    public void writeValueInFile() {
        int i = 0;
        while(i<24) {
            appendWriteToFile(path, NEWLINE+"2022-07-22 01|32|4|45100|27300|95000");
            i++;
        }
    }

    /**
     * get 파일 확장자
     *
     * @author jh.won
     * @since 2022.0819
     * @param fileName
     * @return
     */
    private String getExtensionByGuava(String fileName) {
        return com.google.common.io.Files.getFileExtension(fileName);
    }


    /**
     * 파일이 없을 경우 생성 혹은 line 추가
     *
     * @author jh.won
     * @since 2022.08.19
     * @param path
     * @param line
     */
    private void appendWriteToFile(Path path, String line) {
        try {
            Files.write(path, line.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
