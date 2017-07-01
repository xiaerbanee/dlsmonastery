package net.myspring.future.common.utils;

import net.myspring.future.modules.third.client.OppoClient;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ScheduleUtils {
    @Autowired
    private OppoClient oppoClient;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "*/20 * * * * ?")
    public void synOppo() {
//        logger.info("工厂自动同步开始");
//        String date= LocalDateUtils.format(LocalDate.now());
//        String message=oppoClient.synFactoryOppo(date);
//        logger.info(message+"工厂自动同步成功");
    }
}
