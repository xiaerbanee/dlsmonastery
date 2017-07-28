package net.myspring.future.modules.third.web;

import net.myspring.common.enums.CompanyNameEnum;
import net.myspring.future.common.config.AuditorContextHolder;
import net.myspring.future.common.datasource.DbContextHolder;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.third.service.ImooService;
import net.myspring.util.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "third/factory/imoo")
public class ImooController {
    @Autowired
    private ImooService imooService;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "pullFactoryData")
    public String pullFactoryData(String companyName,String date){
        logger.info("imoo:开始同步串码"+ LocalDateTime.now());
        if (StringUtils.isBlank(RequestUtils.getCompanyName())){
            DbContextHolder.get().setCompanyName(CompanyNameEnum.JXDJ.name());
        }
        if(org.apache.commons.lang.StringUtils.isBlank(RequestUtils.getAccountId())) {
            AuditorContextHolder.get().setAccountId("1");
        }
        return imooService.pullFactoryData(date);
    }

}
