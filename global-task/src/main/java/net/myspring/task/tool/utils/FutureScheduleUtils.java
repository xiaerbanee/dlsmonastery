package net.myspring.task.tool.utils;

import java.time.LocalDate;

import net.myspring.task.tool.client.FutureClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import net.myspring.util.time.LocalDateUtils;


@Service
public class FutureScheduleUtils {
    @Autowired
    private FutureClient futureClient;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void synOppoIme(){
        logger.info("同步串码数据开始");
        String date=LocalDateUtils.format(LocalDate.now());
        futureClient.synOppoIme(date);
        logger.info("同步串码数据结束");
    }

    public void synVivoIme(){
        logger.info("同步串码数据开始");
        String date=LocalDateUtils.format(LocalDate.now());
        futureClient.synVivoIme(date);
        logger.info("同步串码数据结束");
    }
}
