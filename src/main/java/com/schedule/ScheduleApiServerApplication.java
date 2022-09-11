package com.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableJpaAuditing
@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication
public class ScheduleApiServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScheduleApiServerApplication.class, args);
	}

}
