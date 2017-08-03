package net.myspring.task.tool.schedule;

import net.myspring.common.enums.CompanyNameEnum;
import net.myspring.task.tool.client.FutureVivoClient;
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
public class FutureVivoSchedule {
    @Autowired
    private FutureVivoClient futureVivoClient;

    @Value("${companyNames}")
    private String[] companyNames;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void pullIdvivoFactoryData(){
        List<String> companyNameList = Arrays.asList(companyNames);
        if(companyNameList.contains(CompanyNameEnum.IDVIVO.name())) {
            logger.info("同步串码数据开始");
            String date=LocalDateUtils.format(LocalDate.now());
            futureVivoClient.pullFactoryData(CompanyNameEnum.IDVIVO.name(),date);
            logger.info("同步串码数据结束");
        }
    }
}
