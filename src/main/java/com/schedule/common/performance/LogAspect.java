package com.schedule.common.performance;

import com.google.common.base.Joiner;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
public class LogAspect {

//    private final Logger logger = LogManager.getLogger(LogAspect.class);

    @Pointcut("within(com.schedule.web..*)")
    private void publicTarget() {}

    @Around("publicTarget()")
    public Object PerformanceAdvice(ProceedingJoinPoint joinPoint) throws  Throwable {
        Logger logger = LogManager.getLogger(joinPoint.getSignature().getDeclaringTypeName());

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.now();

        try {
          return joinPoint.proceed(joinPoint.getArgs());
        } finally {
            LocalDateTime endDate = LocalDateTime.now();

            logger.info("[{}]-[{}]", joinPoint.getSignature().getName(), "Request Time : "+startDate.format(formatter));
            logger.info("[{}]-[{}]", joinPoint.getSignature().getName(), "Response Time : "+endDate.format(formatter));
            logger.info("[{}]-[{}]", joinPoint.getSignature().getName(), "Duration of Time : "+ Duration.between(startDate, endDate).getNano()+"ns");
        }
    }
}
