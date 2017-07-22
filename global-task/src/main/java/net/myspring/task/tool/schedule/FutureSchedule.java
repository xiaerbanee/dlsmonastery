package net.myspring.task.tool.schedule;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import net.myspring.common.enums.CompanyNameEnum;
import net.myspring.task.tool.client.FutureClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import net.myspring.util.time.LocalDateUtils;


@Service
public class FutureSchedule {
    @Autowired
    private FutureClient futureClient;

    @Value("${companyNames}")
    private String[] companyNames;


    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void pullJxoppoFactoryData(){
        List<String> companyNameList = Arrays.asList(companyNames);
        if(companyNameList.contains(CompanyNameEnum.JXOPPO.name())) {
            logger.info("同步串码数据开始");
            String date=LocalDateUtils.format(LocalDate.now());
            futureClient.pullOppoFactoryData(CompanyNameEnum.JXOPPO.name(),date);
            logger.info("同步串码数据结束");
        }
    }
}
