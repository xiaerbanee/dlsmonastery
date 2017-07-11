package net.myspring.future.modules.third.web;

import net.myspring.future.common.utils.ScheduleUtils;
import net.myspring.future.modules.third.service.OppoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "third/factory/oppo")
public class OppoController {
    @Autowired
    private OppoService oppoService;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value="synIme")
    public String synIme(String date){
        logger.info("开始同步串码，同步日期："+date);
        return oppoService.synOppo(date);
    }
}
