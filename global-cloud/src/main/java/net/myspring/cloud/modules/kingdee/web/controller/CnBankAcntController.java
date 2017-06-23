package net.myspring.cloud.modules.kingdee.web.controller;

import net.myspring.cloud.modules.kingdee.domain.CnBankAcnt;
import net.myspring.cloud.modules.kingdee.service.CnBankAcntService;
import net.myspring.util.time.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lihx on 2017/6/16.
 */
@RestController
@RequestMapping(value = "kingdee/cnBankAcnt")
public class CnBankAcntController {
    @Autowired
    private CnBankAcntService cnBankAcntService;

    @RequestMapping(value = "findAll")
    public List<CnBankAcnt> findAll(){
        return cnBankAcntService.findAll();
    }

    @RequestMapping(value = "findByMaxModifyDate")
    public List<CnBankAcnt> findByMaxModifyDate(LocalDateTime maxModifyDate){
        return cnBankAcntService.findByMaxModifyDate(maxModifyDate);
    }

}
