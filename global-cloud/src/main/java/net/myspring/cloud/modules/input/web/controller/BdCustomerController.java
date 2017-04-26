package net.myspring.cloud.modules.input.web.controller;

import net.myspring.cloud.common.enums.DateFormat;
import net.myspring.cloud.modules.input.domain.BdCustomer;
import net.myspring.cloud.modules.input.service.BdCustomerService;
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
@RequestMapping(value = "input/bdCustomer")
public class BdCustomerController {
    @Autowired
    private BdCustomerService bdCustomerService;

    @RequestMapping(value = "getCustomerList", method = RequestMethod.GET)
    public List<BdCustomer> getCustomerList(String maxOutDate) {
        LocalDateTime localDateTime=null;
        if(StringUtils.isNotBlank(maxOutDate)){
            localDateTime= LocalDateTime.parse(maxOutDate, DateTimeFormatter.ofPattern(DateFormat.DATE_TIME.getValue()));
        }
        List<BdCustomer> customerList = bdCustomerService.findAll(localDateTime);

        return customerList;
    }

    @RequestMapping(value = "getByName", method = RequestMethod.GET)
    public List<BdCustomer> getByName(String customerName) {
        List<BdCustomer> customerList = bdCustomerService.findByName(customerName);
        return customerList;
    }

}
