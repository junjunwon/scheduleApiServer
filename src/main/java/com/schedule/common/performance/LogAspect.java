package com.schedule.common.performance;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class LogAspect {

    private final Logger logger = LogManager.getLogger(LogAspect.class);

    @Pointcut("within(com.schedule.web..*)")
    private void publicTarget() {}

    @Pointcut("within(com.schedule.schedule.Scheduler)")
    private void SchedulerTarget() {}

    @Around("publicTarget() || SchedulerTarget()")
    public Object PerformanceAdvice(ProceedingJoinPoint joinPoint) throws  Throwable {
        Logger logger = LogManager.getLogger(joinPoint.getSignature().getDeclaringTypeName());

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
