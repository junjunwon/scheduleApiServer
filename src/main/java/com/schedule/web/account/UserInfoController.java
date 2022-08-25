package com.schedule.web.account;

import com.schedule.common.response.ErrorResponse;
import com.schedule.common.response.Response;
import com.schedule.dto.account.UserInfoSaveRequestDto;
import com.schedule.service.account.UserInfoService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/api")
@RestController
public class UserInfoController {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    private final UserInfoService userInfoService;

    private final Bucket bucket;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;

        Bandwidth limit = Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(10)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Validated @RequestBody final UserInfoSaveRequestDto userInfoSaveRequestDto,
                                       BindingResult bindingResult) {
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        if(bucket.tryConsume(1)) {

            //유효성 검사
            if (bindingResult.hasErrors()) {
                List<String> errors = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
                return ResponseEntity.ok(new ErrorResponse("404", "Validation failure", errors));
                // or 404 request
            }

            Response response = new Response();

            long index = userInfoService.createUser(userInfoSaveRequestDto);

            if(index>0) {
                response.setMessage("Success");
                response.setStatus(HttpStatus.OK);
                response.setData(index);
            } else {
                response.setMessage("Fail to create user");
            }
            response.setAvailableTokens(bucket.getAvailableTokens()); // 임계치까지 남은 api호출 횟수

            logger.info("success");
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        }
        logger.info("too many requests");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
