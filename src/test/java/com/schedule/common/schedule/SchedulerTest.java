package com.schedule.common.schedule;

import com.schedule.schedule.Scheduler;
import com.schedule.schedule.SchedulingCnt;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SchedulerTest {

    @SpyBean
    Scheduler scheduler;

    @Autowired
    SchedulingCnt schedulingCntTest;

    @Test
    public void basic() throws InterruptedException {
        Thread.sleep(100L);

        assertThat(schedulingCntTest.getInvocationCount(), is(greaterThan(0)));
    }

    @Test
    public void whenWaitOneSecond_thenScheduledIsCalledAtLeastTenTimes() {
        await()
                .atMost(Duration.ONE_MINUTE)
                .untilAsserted(() -> verify(scheduler, atLeast(2)).getFileListScheduler());
    }
}
