package net.myspring.future.modules.third.web;

import net.myspring.future.common.config.AuditorContextHolder;
import net.myspring.future.common.datasource.DbContextHolder;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.third.service.OppoService;
import org.apache.commons.lang.StringUtils;
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

    @RequestMapping(value="pullFactoryData")
    public String pullFactoryData(String companyName,String date){
        logger.info("开始同步串码，同步日期："+date);
        if(StringUtils.isBlank(RequestUtils.getCompanyName())) {
            DbContextHolder.get().setCompanyName(companyName);
        }
        if(StringUtils.isBlank(RequestUtils.getAccountId())) {
            AuditorContextHolder.get().setAccountId("1");
        }
        return oppoService.pullFactoryData(date);
    }
}
