package net.myspring.future.modules.third.web;

import net.myspring.future.common.datasource.DbContextHolder;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.third.service.OppoService;
import net.myspring.future.modules.third.service.VivoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "third/factory/vivo")
public class VivoController {
    @Autowired
    private VivoService vivoService;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value="pullFactoryData")
    public String pullFactoryData(String companyName,String date){
        logger.info("开始同步串码，同步日期："+date);
        if(StringUtils.isBlank(RequestUtils.getCompanyName())) {
            DbContextHolder.get().setCompanyName(companyName);
        }
        return vivoService.pullFactoryData(date);
    }
}
