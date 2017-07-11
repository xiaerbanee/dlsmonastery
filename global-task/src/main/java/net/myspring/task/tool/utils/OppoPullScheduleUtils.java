package net.myspring.task.tool.utils;

import net.myspring.task.tool.client.FactoryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class OppoPullScheduleUtils {
    @Autowired
    private FactoryClient factoryClient;

    public void pullOppoData(){
        String date="2017-07-09";
        factoryClient.pullOppoFactoryData(date);
    }
}
