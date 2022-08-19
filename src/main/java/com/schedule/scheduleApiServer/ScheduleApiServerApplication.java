package com.schedule.scheduleApiServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class ScheduleApiServerApplication {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+"classpath:application.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(ScheduleApiServerApplication.class)
				.properties(APPLICATION_LOCATIONS)
						.run(args);
//		SpringApplication.run(ScheduleApiServerApplication.class, args);
	}

}
