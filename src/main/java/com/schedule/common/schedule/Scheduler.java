package com.schedule.common.schedule;

import com.schedule.domain.file.FileInfoCustomRepository;
import com.schedule.dto.file.FileInfoSaveRequestDto;
import com.schedule.service.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
@Component
public class Scheduler {

    private static final Logger log = LogManager.getLogger(Scheduler.class);
    static {
        String pid = ManagementFactory.getRuntimeMXBean().getName().replaceAll("@.*", "");
        //MDC
        ThreadContext.put("pid", pid);
    }
    private final FileService fileService;
    private final FileInfoCustomRepository fileInfoCustomRepository;
    private static final Path path = Paths.get("src/main/resources/files/inputList.txt");
    private static final String NEWLINE = System.lineSeparator();
    private String fileExtension = "";
    private String delimiter = "";
    private static boolean isCorrect;
    private static Map<String, String> map = new HashMap<>();
    @PostConstruct
    private void setInitData() {

        StringBuffer stringBuffer = new StringBuffer();

        map = new HashMap<>() {{
            put("txt", "|");
            put("csv", ",");
            put("xlsx", ",");
            put("hwp", "-");
        }};
        //확장자 추출
        fileExtension = getExtensionByGuava(path.getFileName().toString());
        //구분자 추출
        stringBuffer.append("["); //특수문자 구분자를 위한 대괄호
        stringBuffer.append(map.get(fileExtension));
        stringBuffer.append("]");
        delimiter = stringBuffer.toString();

        log.debug("Hello from Log4j 2 - num : {}", path);
    }

    /**
     * write content in 파일 스케줄러
     *
     * @author jh.won
     * @since 2022.08.21
     */
//    @Scheduled(cron = "0 0 0/1 * * *") //1시간마다 도는 스케줄러
    @Scheduled(fixedRate = 10000)
    public void writeValueInFile() {
        StringBuffer stringBuffer = new StringBuffer();
        String hourOfToday = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int joinMemberCnt = getRandomNumberUsingNextInt(1, 99);
        int leaveMemberCnt = getRandomNumberUsingNextInt(1, 99);
        int payment = getRandomNumberUsingNextInt(1000, 100000);
        int cost = getRandomNumberUsingNextInt(1000, 100000);
        int revenue = getRandomNumberUsingNextInt(1000, 100000);

        String contentLine = stringBuffer
                .append(hourOfToday).append("|")
                .append(joinMemberCnt).append("|")
                .append(leaveMemberCnt).append("|")
                .append(payment).append("|")
                .append(cost).append("|")
                .append(revenue)
                .append(NEWLINE)
                .toString();

//        appendWriteToFile(path, "2022-07-22 01|32|4|45100|27300|95000"+NEWLINE);
        appendWriteToFile(path, contentLine);
    }

    //    @Scheduled(cron = "0 0 00 * * *", zone = "Asia/Seoul")
    @Scheduled(fixedRate = 10000)
    public void getFileListScheduler() {

        try {
            log.debug("Hello from Log4j 2 - num : {}",path);
            List<String> contentList = Files.readAllLines(path);
            //체크
            isCorrect = checkContentFiles(contentList);

            //입력파일에 문제가 없으면 db에 저장
            if(isCorrect) saveContentFiles(contentList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveContentFiles(List<String> contentList) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time;
        LocalDateTime today = LocalDateTime.now();
        int isExist;
        for(String content : contentList) {
            String[] contentLine = content.split(delimiter);
            time = LocalDateTime.parse(contentLine[0], formatter);
            isExist = fileInfoCustomRepository.findEqualDateTime(time);
            if(isExist == 0) {
                FileInfoSaveRequestDto requestDto = contentStringToDto(contentLine, time);
                fileService.save(requestDto);
            }
        }
    }

    FileInfoSaveRequestDto contentStringToDto(String[] contentLine, LocalDateTime time) {
        FileInfoSaveRequestDto requestDto = FileInfoSaveRequestDto.builder()
                .time(time)
                .joinMemberCnt(Integer.parseInt(contentLine[1]))
                .leaveMemberCnt(Integer.parseInt(contentLine[2]))
                .payment(Integer.parseInt(contentLine[3]))
                .cost(Integer.parseInt(contentLine[4]))
                .revenue(Integer.parseInt(contentLine[5]))
                .build();

        return requestDto;
    }

    private boolean checkContentFiles(List<String> contentList) {
        for(String content : contentList) {
            String[] contentLine = content.split(delimiter);
            //값이 누락된 경우
            if(contentLine.length != 6) {
                return false;
            }
        }
        return true;
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

    /**
     * 범위에 따른 Random 값 생성
     *
     * @author jh.won
     * @since 2022.08.22
     * @param min
     * @param max
     * @return
     */
    public int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}
