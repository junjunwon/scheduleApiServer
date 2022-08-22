package com.schedule.web;

import com.schedule.common.object.Response;
import com.schedule.common.schedule.Scheduler;
import com.schedule.dto.file.FileInfoResponseDto;
import com.schedule.dto.file.FileInfoSaveRequestDto;
import com.schedule.service.FileService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.List;

@RequestMapping("/api")
@RestController
public class FileApiController {

    private static final Logger logger = LogManager.getLogger(FileApiController.class);
    private final FileService fileService;
    private final Bucket bucket;

    public FileApiController(FileService fileService) {
        this.fileService = fileService;

        //rate limit performance 측정
        //1시간에 10개의 요청을 처리할 수 있는 Bucket 생성
//        Bandwidth limit = Bandwidth.classic(10, Refill.intervally(10, Duration.ofHours(1)));
        Bandwidth limit = Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(10)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @GetMapping("/fileList/list")
    public ResponseEntity<Response> getFileList() {

        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        if(bucket.tryConsume(1)) {
            Response response = new Response();
            List<FileInfoResponseDto> dtoList = fileService.findFileList();
            response.setStatus(HttpStatus.OK);
            response.setMessage("Success getFileList!!");
            response.setData(dtoList);
            response.setAvailableTokens(bucket.getAvailableTokens()); // 임계치까지 남은 api호출 횟수
            logger.info("success");
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        }

        logger.info("too many requests");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @PostMapping("/file/save")
    public ResponseEntity<Response> save(@RequestBody FileInfoSaveRequestDto fileInfoSaveRequestDto) {

        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        if(bucket.tryConsume(1)) {
            Response response = new Response();

            long index = fileService.save(fileInfoSaveRequestDto);
            if(index>0) {
                response.setMessage("Success");
                response.setStatus(HttpStatus.OK);
            }
            response.setAvailableTokens(bucket.getAvailableTokens()); // 임계치까지 남은 api호출 횟수

            logger.info("success");
            return new ResponseEntity<>(response, headers, HttpStatus.OK);

        }

        logger.info("too many requests");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @DeleteMapping("/file/delete/{ids}")
    public ResponseEntity<Response> deleteContentsById(@PathVariable List<Long> ids) {
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        if(bucket.tryConsume(1)) {
            Response response = new Response();
            long check = fileService.deleteContentsById(ids);
            if(check>0) {
                response.setMessage("Success");
            } else {
                response.setMessage("Fail to delete");
            }
            response.setStatus(HttpStatus.OK);
            response.setAvailableTokens(bucket.getAvailableTokens()); // 임계치까지 남은 api호출 횟수

            logger.info("success");
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        }

        logger.info("too many requests");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
