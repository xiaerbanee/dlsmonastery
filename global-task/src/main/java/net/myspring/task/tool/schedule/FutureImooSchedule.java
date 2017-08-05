package net.myspring.task.tool.schedule;

import net.myspring.common.enums.CompanyNameEnum;
import net.myspring.task.tool.client.FutureImooClient;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class FutureImooSchedule {
    @Autowired
    private FutureImooClient futureImooClient;

    @Value("${companyNames}")
    private String[] companyNames;


    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "0 30 2-18 * * ?")
    public void pullJximooFactoryData(){
        List<String> companyNameList = Arrays.asList(companyNames);
        if(companyNameList.contains(CompanyNameEnum.JXDJ.name())) {
            logger.info("同步串码数据开始");
            String date= LocalDateUtils.format(LocalDate.now());
            futureImooClient.pullFactoryData(CompanyNameEnum.JXDJ.name(),date);
            logger.info("同步串码数据结束");
        }
    }
}
