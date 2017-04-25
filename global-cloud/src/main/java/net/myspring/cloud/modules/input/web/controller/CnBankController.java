package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.enums.DateFormat;
import net.myspring.cloud.modules.input.domain.CnBank;
import net.myspring.cloud.modules.input.service.CnBankService;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by lihx on 2017/4/6.
 */
@RestController
@RequestMapping(value = "kingdee/cnBank")
public class CnBankController {
    @Autowired
    private CnBankService cnBankService;

    @RequestMapping(value = "getBankList", method = RequestMethod.GET)
    public List<CnBank> getBankList(String maxOutDate) {
        LocalDateTime localDateTime = null;
        if(StringUtils.isNotBlank(maxOutDate)){
            localDateTime = LocalDateTime.parse(maxOutDate, DateTimeFormatter.ofPattern(DateFormat.DATE_TIME.getValue()));
        }
        List<CnBank> bankList = cnBankService.findAll(localDateTime);
        return bankList;
    }
}
