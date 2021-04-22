package com.docky.wiki.job;

import com.docky.wiki.service.EbookSnapshotService;
import com.docky.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EbookSnapshotJob {

    private static final Logger Log = LoggerFactory.getLogger(EbookSnapshotJob.class);

    @Autowired
    private EbookSnapshotService ebookSnapshotService;

    @Autowired
    private SnowFlake snowFlake;

    /**
     * 每30秒更新电子书信息
     */
    @Scheduled(cron = "0 0 0 1/1 * ? ")
    public void doSnapshot() throws InterruptedException {
        MDC.put("LOG_ID",String.valueOf(snowFlake.nextId()));
        Log.info("生成今日电子书快照开始");
        Long start = System.currentTimeMillis();
        ebookSnapshotService.genSnapshot();
        Log.info("生成今日电子书快照结束，耗时：{}毫秒",System.currentTimeMillis()-start);
    }

}
