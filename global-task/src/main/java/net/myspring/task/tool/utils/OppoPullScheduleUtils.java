package net.myspring.task.tool.utils;

import net.myspring.task.tool.client.FactoryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class OppoPullScheduleUtils {
    @Autowired
    private FactoryClient factoryClient;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "0 */1 * * * ?")
    public void pullOppoData(){
        String date="2017-07-11";
        factoryClient.pullOppoFactoryData(date);
    }
}
