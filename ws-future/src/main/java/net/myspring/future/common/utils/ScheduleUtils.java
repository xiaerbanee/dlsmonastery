package net.myspring.future.common.utils;

import net.myspring.future.modules.third.client.OppoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleUtils {
    @Autowired
    private OppoClient oppoClient;

//    @Scheduled(cron = "*/5 * * * * ?")
//    public  void synFactory() {
//        oppoClient.findSynImeList("2017-06-23","M13AMB");
//    }

}
