package net.myspring.task.tool.schedule;

import net.myspring.common.enums.CompanyNameEnum;
import net.myspring.task.tool.client.ToolOppoClient;
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
public class ToolOppoSchedule {
    @Autowired
    private ToolOppoClient toolOppoClient;

    @Value("${companyNames}")
    private String[] companyNames;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "0 0 2-18 * * ?")
    public void pullJxoppoData(){
        List<String> companyNameList = Arrays.asList(companyNames);
        if(companyNameList.contains(CompanyNameEnum.JXOPPO.name())) {
            logger.info("同步工厂数据开始");
            String date=LocalDateUtils.format(LocalDate.now());
            toolOppoClient.pullFactoryData(CompanyNameEnum.JXOPPO.name(),date);
            logger.info("同步工厂数据结束");
        }
    }

    @Scheduled(cron = "0 0 20,21,22 * * ?")
    public void pushJxoppoToLocal(){
        List<String> companyNameList = Arrays.asList(companyNames);
        if(companyNameList.contains(CompanyNameEnum.JXOPPO.name())) {
            logger.info("工厂上抛数据开始");
            String date= LocalDateUtils.format(LocalDate.now());
            toolOppoClient.pushToLocal(CompanyNameEnum.JXOPPO.name(),date);
            logger.info("工厂上抛数据结束");
        }
    }

}
