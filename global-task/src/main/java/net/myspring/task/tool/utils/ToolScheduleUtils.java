package net.myspring.task.tool.utils;

import net.myspring.task.tool.client.ToolClient;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class ToolScheduleUtils {
    @Autowired
    private ToolClient factoryClient;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void pullOppoData(){
        logger.info("同步工厂数据开始");
        String date=LocalDateUtils.format(LocalDate.now());
        factoryClient.pullJxoppoFactoryData(date);
        logger.info("同步工厂数据结束");
    }



    @Scheduled(cron = "0 0 21,22,23 * * ?")
    public void synJxoppoToLocal(){
        logger.info("工厂上抛数据开始");
        String date= LocalDateUtils.format(LocalDate.now());
        factoryClient.synJxoppoToLocal(date);
        logger.info("工厂上抛数据结束");
    }
}
