package com.schedule.web.file;

import com.schedule.common.response.Response;
import com.schedule.dto.file.FileInfoRequestDto;
import com.schedule.dto.file.FileInfoResponseDto;
import com.schedule.dto.file.FileInfoSaveRequestDto;
import com.schedule.dto.file.FileInfoUpdateRequestDto;
import com.schedule.exception.BucketExceedException;
import com.schedule.service.file.FileService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.util.List;

@Slf4j
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
        Bandwidth limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofSeconds(20)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @GetMapping("/fileList/list")
    public ResponseEntity<Response> getFileList(@ModelAttribute FileInfoRequestDto requestDto) {

        if(bucket.tryConsume(1)) {

            List<FileInfoResponseDto> dtoList = fileService.findFileList(requestDto);

            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setMessage("Success getFileList!!");
            response.setData(dtoList);

            response.setAvailableTokens(bucket.getAvailableTokens()); // 임계치까지 남은 api호출 횟수

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new BucketExceedException("title", "요청 횟수를 초과하였습니다.");
    }

    @PostMapping("/file/save")
    public ResponseEntity<?> save(@Valid @RequestBody final FileInfoSaveRequestDto fileInfoSaveRequestDto) {

        if(bucket.tryConsume(1)) {

            Response response = new Response();

            fileService.save(fileInfoSaveRequestDto);
            response.setMessage("Success");
            response.setStatus(HttpStatus.OK);

            response.setAvailableTokens(bucket.getAvailableTokens()); // 임계치까지 남은 api호출 횟수

            return new ResponseEntity<>(response, HttpStatus.OK);

        }

        throw new BucketExceedException("title", "요청 횟수를 초과하였습니다.");
    }

    @PatchMapping("/file/update/{id}")
    public ResponseEntity<?> updateContentById(@PathVariable Long id,
                                               @Valid @RequestBody final FileInfoUpdateRequestDto fileInfoUpdateRequestDto) {

        if(bucket.tryConsume(1)) {

            Response response = new Response();
            fileService.updateContentById(id, fileInfoUpdateRequestDto);

            response.setMessage("Success");
            response.setStatus(HttpStatus.OK);
            response.setAvailableTokens(bucket.getAvailableTokens()); // 임계치까지 남은 api호출 횟수

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new BucketExceedException("title", "요청 횟수를 초과하였습니다.");
    }


    @DeleteMapping("/file/delete/{ids}")
    public ResponseEntity<Response> deleteContentsById(@PathVariable List<Long> ids) {
        if(bucket.tryConsume(1)) {
            Response response = new Response();
            fileService.deleteContentsByIds(ids);

            response.setMessage("Success");
            response.setStatus(HttpStatus.OK);

            response.setAvailableTokens(bucket.getAvailableTokens()); // 임계치까지 남은 api호출 횟수

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        throw new BucketExceedException("title", "요청 횟수를 초과하였습니다.");
    }
}

