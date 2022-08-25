package com.schedule.common.schedule;

import com.schedule.ScheduleApiServerApplication;
import com.schedule.schedule.Scheduler;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringJUnitConfig(ScheduleApiServerApplication.class)
public class SchedulerTest {

    @SpyBean
    Scheduler scheduler;

    @Test
    public void whenWaitOneSecond_thenScheduledIsCalledAtLeastTenTimes() {
        await()
                .atMost(Duration.ONE_MINUTE)
                .untilAsserted(() -> verify(scheduler, atLeast(4)).getFileListScheduler());
    }
}
