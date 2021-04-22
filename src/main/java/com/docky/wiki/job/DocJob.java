package com.docky.wiki.job;

import com.docky.wiki.service.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DocJob {

    private static final Logger Log = LoggerFactory.getLogger(DocJob.class);

    @Autowired
    private DocService docService;

    /**
     * 每30秒更新电子书信息
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void cron() throws InterruptedException {
        Log.info("更新电子书下的文档数据开始");
        long start = System.currentTimeMillis();
        docService.updateEbookInfo();
        Log.info("更新电子书下的文档数据结束，耗时：{}毫秒",System.currentTimeMillis()-start);
    }

}
