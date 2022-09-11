package com.schedule.common.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@ConfigurationProperties(prefix = "config")
@Getter
@Setter
@Component
public class ApplicationConfig {

    private Set<String> allowedIps;
    private String filePath;
}
