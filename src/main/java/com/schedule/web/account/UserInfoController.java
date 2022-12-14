package com.schedule.web.account;

import com.schedule.common.response.Response;
import com.schedule.dto.account.UserInfoSaveRequestDto;
import com.schedule.exception.BucketExceedException;
import com.schedule.service.account.UserInfoService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Duration;

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
    public ResponseEntity<?> createUser(@Valid @RequestBody final UserInfoSaveRequestDto userInfoSaveRequestDto) {
        if(bucket.tryConsume(1)) {

            Response response = new Response();

            long index = userInfoService.createUser(userInfoSaveRequestDto);

            if(index>0) {
                response.setMessage("Success");
                response.setStatus(HttpStatus.OK);
                response.setData(index);
            } else {
                response.setMessage("Fail to create user");
            }
            response.setAvailableTokens(bucket.getAvailableTokens()); // ??????????????? ?????? api?????? ??????

            logger.info("success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        throw new BucketExceedException("title", "?????? ????????? ?????????????????????.");
    }
}
