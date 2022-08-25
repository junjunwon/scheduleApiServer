package com.schedule;

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

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+"classpath:application.yml";
//			+"classpath:application.properties";

	public static void main(String[] args) {
		new SpringApplicationBuilder(ScheduleApiServerApplication.class)
				.properties(APPLICATION_LOCATIONS)
						.run(args);
	}

}
