package com.schedule.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

public class LogControllerTest {

    private static Logger logger = null;

    @BeforeAll
    public static void setLogger() throws MalformedURLException {
        System.setProperty("log4j.configurationFile", "log4j2-spring-test.xml");
        logger = LogManager.getLogger();
    }

    @Test
    public void loggingTest() {
        logger.info("test logging");
    }
}
