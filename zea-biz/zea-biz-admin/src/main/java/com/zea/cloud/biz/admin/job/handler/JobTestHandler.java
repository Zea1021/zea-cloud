package com.zea.cloud.biz.admin.job.handler;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class JobTestHandler {

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("testJobHandler")
    public void testJobHandler() throws Exception {
        log.info("XXL-JOB, Hello World.");

        for (int i = 0; i < 5; i++) {
            log.info("beat at:" + i);
            TimeUnit.SECONDS.sleep(2);
        }
        // default success
    }

}
