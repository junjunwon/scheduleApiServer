package com.schedule.schedule;

import com.schedule.common.config.ApplicationConfig;
import com.schedule.domain.file.FileInfoCustomRepository;
import com.schedule.dto.file.FileInfoSaveRequestDto;
import com.schedule.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class Scheduler {

    private static final Logger logger = LogManager.getLogger(Scheduler.class);
    private final FileService fileService;
    private final FileInfoCustomRepository fileInfoCustomRepository;
    private static Path path;
    private final ApplicationConfig applicationConfig;
    private final BasicFileHandler basicFileHandler;
    private final ExcelFileHandler excelFileHandler;
    private static FileHandlerInterface fileHandlerInterface;

    private String DELIMETER = "";
    //변하지 않는 구분자
    private static final List<String> delimeterList = Arrays.asList("^", "$", "(?!)", "|", "[]", "{}", "()", ".", "*", "+", "?",
            "\\d", "\\D", "\\w", "\\W", "\\s", "\\S");
    @PostConstruct
    private void setInitData() throws IOException {

        logger.info("file path in application.yml is {}", applicationConfig.getFilePath());
        String fileExtension = "";

        Path DirPath = Paths.get(applicationConfig.getFilePath());

        if(ObjectUtils.isEmpty(getFile(DirPath))) {
            throw new IOException("FILE_NONE_EXIST_EXCEPTION");
        }
        path = getFile(DirPath);

        //확장자 추출
        fileExtension = basicFileHandler.getExtensionByGuava(path.getFileName().toString());
        getFileExecute(fileExtension);

        // delimiter : 파일에 저장하기 위한 구분자
        DELIMETER = fileHandlerInterface.getDelimiter();
    }

    private Path getFile(Path path) throws IOException {
        List<Path> getAllFiles;
        try(Stream<Path> walk = Files.walk(path)) {
            getAllFiles = walk.filter(Files::isRegularFile).toList();
        }
        return getAllFiles.get(0);
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

        String NEWLINE = System.lineSeparator();

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
                .append(hourOfToday).append(DELIMETER)
                .append(joinMemberCnt).append(DELIMETER)
                .append(leaveMemberCnt).append(DELIMETER)
                .append(payment).append(DELIMETER)
                .append(cost).append(DELIMETER)
                .append(revenue)
                .append(NEWLINE)
                .toString();

        logger.info("Check value for writing... - a content : {}",contentLine);

        appendWriteToFile(path, contentLine);
    }

//    @Scheduled(cron = "0 0 00 * * *", zone = "Asia/Seoul") //자정마다 도는 스케줄러
    @Scheduled(cron = "0 0/2 * * * *", zone = "Asia/Seoul") //2분마다 도는 스케줄러
    public void getFileListScheduler() {

        boolean isCorrect;
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
            /**
             * setSpecialDelimiter(delimiter)
             * 메타데이터 특수문자가 아닐 경우 일반 delimiter로 return
             * 그렇지 않을 경우 [delimiter]로 return
             */
            String[] contentLine = content.split(setSpecialDelimiter(DELIMETER));
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
            String[] contentLine = content.split(setSpecialDelimiter(DELIMETER));
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

    //TODO: Interface 분기를 이렇게 하는게 맞는지 점검 -> 피드백이 필요함 switch문을 통해 base interface에 구현체를 상속받아서 작업
    private void getFileExecute(String fileExtension) {
        switch (fileExtension) {
            default -> fileHandlerInterface = basicFileHandler;
            case "csv", "txt" -> fileHandlerInterface = basicFileHandler;
            case "xlsx", "xls" -> fileHandlerInterface = excelFileHandler;
        }
    }

    /**
     * split() 함수 사용 시 위 표에 있는 메타문자로 들어가는 특수문자를 구분자로 사용할 때는
     * 메타문자 앞에 \(역슬래쉬) 2번 혹은 [메타문자]를 붙여 이스케이프처리 해주면 된다.
     */
    public static String setSpecialDelimiter(String delimiter) {
        StringBuffer stringBuffer = new StringBuffer();
        if(delimeterList.contains(delimiter)) {
            stringBuffer.append("["); //특수문자 구분자를 위한 대괄호
            stringBuffer.append(delimiter);
            stringBuffer.append("]");
            return stringBuffer.toString();
        } else {
            return delimiter;
        }
    }
}
