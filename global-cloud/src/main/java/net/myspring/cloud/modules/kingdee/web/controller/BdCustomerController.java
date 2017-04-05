package net.myspring.cloud.modules.kingdee.web.controller;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.enums.DateFormat;
import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.kingdee.service.BdCustomerService;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@RestController
@RequestMapping(value = "kingdee/bdCustomer")
public class BdCustomerController {
    @Autowired
    private BdCustomerService bdCustomerService;

    @RequestMapping(value = "getCustomerList", method = RequestMethod.GET)
    public String getCustomerList(String maxOutDate) {
        LocalDateTime localDateTime=null;
        if(StringUtils.isNotBlank(maxOutDate)){
            localDateTime= LocalDateTime.parse(maxOutDate, DateTimeFormatter.ofPattern(DateFormat.DATE_TIME.getValue()));
        }
        List<BdCustomer> customerList = bdCustomerService.findAll(localDateTime);

        return ObjectMapperUtils.writeValueAsString(customerList);
    }

}
