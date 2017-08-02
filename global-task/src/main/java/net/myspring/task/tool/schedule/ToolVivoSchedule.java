package net.myspring.task.tool.schedule;

import net.myspring.common.enums.CompanyNameEnum;
import net.myspring.task.tool.client.ToolVivoClient;
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
public class ToolVivoSchedule {
    @Autowired
    private ToolVivoClient toolVivoClient;

    @Value("${companyNames}")
    private String[] companyNames;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void pullIdvivoData(){
        List<String> companyNameList = Arrays.asList(companyNames);
        if(companyNameList.contains(CompanyNameEnum.IDVIVO.name())) {
            logger.info("下拉工厂数据开始");
            String date=LocalDateUtils.format(LocalDate.now());
            toolVivoClient.pullFactoryData(CompanyNameEnum.IDVIVO.name(),date);
            logger.info("下拉工厂数据结束");
        }
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void pullVivoData(){
        List<String> companyNameList = Arrays.asList(companyNames);
        if(companyNameList.contains(CompanyNameEnum.JXVIVO.name())) {
            logger.info("下拉工厂数据开始");
            String date= "2017-07-15";
            toolVivoClient.pullFactoryData(CompanyNameEnum.JXVIVO.name(),date);
            logger.info("下拉工厂数据结束");
        }
    }

    @Scheduled(cron = "0 20 21,22,23 * * ?")
    public void pushIdvivoToLocal(){
        List<String> companyNameList = Arrays.asList(companyNames);
        if(companyNameList.contains(CompanyNameEnum.IDVIVO.name())) {
            logger.info("同步业务数据至中转库开始");
            String date= LocalDateUtils.format(LocalDate.now());
            toolVivoClient.pushToLocal(CompanyNameEnum.IDVIVO.name(),date);
            logger.info("同步业务数据至中转库结束");
        }
    }

    @Scheduled(cron = "0 20 21,22,23 * * ?")
    public void pushVivoToLocal(){
        List<String> companyNameList = Arrays.asList(companyNames);
        if(companyNameList.contains(CompanyNameEnum.JXVIVO.name())) {
            logger.info("同步业务数据至中转库开始");
            String date= LocalDateUtils.format(LocalDate.now());
            toolVivoClient.pushToLocal(CompanyNameEnum.JXVIVO.name(),date);
            logger.info("同步业务数据至中转库结束");
        }
    }

    @Scheduled(cron = "0 40 21,22,23 * * ?")
    public void pushIdvivoData(){
        List<String> companyNameList = Arrays.asList(companyNames);
        if(companyNameList.contains(CompanyNameEnum.IDVIVO.name())) {
            logger.info("上抛中转库数据至工厂数据库开始");
            String date=LocalDateUtils.format(LocalDate.now());
            toolVivoClient.pushFactoryData(CompanyNameEnum.IDVIVO.name(),date);
            logger.info("上抛中转库数据至工厂数据库结束");
        }
    }

//    @Scheduled(cron = "0 40 21,22,23 * * ?")
    public void pushVivoData(){
        List<String> companyNameList = Arrays.asList(companyNames);
        if(companyNameList.contains(CompanyNameEnum.JXVIVO.name())) {
            logger.info("上抛中转库数据至工厂数据库开始");
            String date=LocalDateUtils.format(LocalDate.now());
            toolVivoClient.pushFactoryData(CompanyNameEnum.JXVIVO.name(),date);
            logger.info("上抛中转库数据至工厂数据库结束");
        }
    }

}
