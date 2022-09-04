package com.schedule.schedule;

import com.schedule.domain.file.FileInfoCustomRepository;
import com.schedule.dto.file.FileInfoSaveRequestDto;
import com.schedule.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class Scheduler {

    private static final Logger logger = LogManager.getLogger(Scheduler.class);
    private final FileService fileService;
    private final FileInfoCustomRepository fileInfoCustomRepository;
    private static Path path;
    private static ClassPathResource resourceDir = null;
    private static final String NEWLINE = System.lineSeparator();
    private String fileExtension = "";
    private String delimiter = "";
    private String seperator = "";
    private static boolean isCorrect;
    private static Map<String, String> map;
    static {
        map = new HashMap<>() {{
            put("txt", "|");
            put("csv", ",");
            // TODO : 파일 형식 확장성 고려
        }};
    }

    @PostConstruct
    private void setInitData() throws IOException {

        StringBuffer stringBuffer = new StringBuffer();

        Path DirPath = Paths.get("src/main/resources/files/");
        //FileNotFoundException 방지
        String filePath =  "/files";

        if(ObjectUtils.isEmpty(getListFiles(DirPath).get(0))) {
            throw new IOException("FILE_NONE_EXIST_EXCEPTION");
        }
        path = getListFiles(DirPath).get(0);

        // TODO : 클래스 경로에 inputList.txt파일이 정상적으로 쓰기/읽기 되는지 확인.
        //확장자 추출
        fileExtension = getExtensionByGuava(path.getFileName().toString());
        //구분자 추출
        delimiter = map.get(fileExtension);
        seperator = delimiter;
        if(Objects.equals(delimiter, "|")) {
            stringBuffer.append("["); //특수문자 구분자를 위한 대괄호
            stringBuffer.append(delimiter);
            stringBuffer.append("]");
            seperator = stringBuffer.toString();
        }
    }

    private List<Path> getListFiles(Path path) throws IOException {
        List<Path> getAllFiles;
        List<Path> result = new ArrayList<>();

        try(Stream<Path> walk = Files.walk(path)) {
            getAllFiles = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }

        for(int i = 0; i < getAllFiles.size(); i++) {
            Path element = getAllFiles.get(i);
            fileExtension = getExtensionByGuava(element.getFileName().toString());

            if(map.containsKey(fileExtension)) result.add(element);
        }
        return result;
    }

    /**
     * write content in 파일 스케줄러
     *
     * @author jh.won
     * @since 2022.08.21
     */
//    @Scheduled(cron = "0 0 0/1 * * *") //1시간마다 도는 스케줄러
    @Scheduled(cron = "0 0/1 * * * *") //1분마다 도는 스케줄러
    public void writeValueInFile() throws IOException {
        // TODO : check 후 제거
        logger.info("Start writeValueInFile... - path : {}");

        setInitData();

        StringBuffer stringBuffer = new StringBuffer();

        // TODO : date format 형식 체크
        String hourOfToday = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
        int joinMemberCnt = getRandomNumberUsingNextInt(1, 99);
        int leaveMemberCnt = getRandomNumberUsingNextInt(1, 99);
        int payment = getRandomNumberUsingNextInt(1000, 100000);
        int cost = getRandomNumberUsingNextInt(1000, 100000);
        int revenue = getRandomNumberUsingNextInt(1000, 100000);

        String contentLine = stringBuffer
                .append(hourOfToday).append(delimiter)
                .append(joinMemberCnt).append(delimiter)
                .append(leaveMemberCnt).append(delimiter)
                .append(payment).append(delimiter)
                .append(cost).append(delimiter)
                .append(revenue)
                .append(NEWLINE)
                .toString();

        logger.info("Check value for writing... - a content : {}",contentLine);

        appendWriteToFile(path, contentLine);
    }

//    @Scheduled(cron = "0 0 00 * * *", zone = "Asia/Seoul") //자정마다 도는 스케줄러
    @Scheduled(cron = "0 0/2 * * * *", zone = "Asia/Seoul") //2분마다 도는 스케줄러
    public void getFileListScheduler() {

        try {
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

        // TODO : date format 형식 체크
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        LocalDateTime time;
        int isExist;

        for(String content : contentList) {
            String[] contentLine = content.split(seperator);
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
            String[] contentLine = content.split(seperator);
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
