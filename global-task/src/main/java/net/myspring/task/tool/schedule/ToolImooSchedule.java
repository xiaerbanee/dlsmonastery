package net.myspring.task.tool.schedule;

import net.myspring.common.enums.CompanyNameEnum;
import net.myspring.task.tool.client.ToolImooClient;
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
public class ToolImooSchedule {
    @Autowired
    private ToolImooClient toolImooClient;

    @Value("${companyNames}")
    private String[] companyNames;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void pullJximooData(){
        List<String> companyNameList = Arrays.asList(companyNames);
        if(companyNameList.contains(CompanyNameEnum.JXDJ.name())) {
            logger.info("同步工厂数据开始");
            String date= LocalDateUtils.format(LocalDate.now());
            toolImooClient.pullFactoryDate(CompanyNameEnum.JXDJ.name(),date);
            logger.info("同步工厂数据结束");
        }
    }
}
