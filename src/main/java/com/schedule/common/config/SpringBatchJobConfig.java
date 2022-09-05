package com.schedule.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
public class SpringBatchJobConfig {

    private static final Logger logger = LoggerFactory.getLogger(SpringBatchJobConfig.class);

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job testJob() {
        Job testJob = jobBuilderFactory.get("testJob")
                .start(step())
                .build();

        return testJob;
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step")
                .tasklet((contribution, chunkContext) -> {
                    logger.info("Step!");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}