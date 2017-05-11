package net.myspring.cloud.modules.kingdee.web;

import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.kingdee.service.BdCustomerService;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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
        LocalDateTime localDateTime = null;
        if(StringUtils.isNotBlank(maxOutDate)){
            localDateTime = LocalDateTimeUtils.parse(maxOutDate);
        }
        List<BdCustomer> customerList = bdCustomerService.findByDate(localDateTime);
        return customerList;
    }

    @RequestMapping(value = "getByName", method = RequestMethod.GET)
    public List<BdCustomer> getByName(String customerName) {
        List<BdCustomer> customerList = bdCustomerService.findByName(customerName);
        return customerList;
    }

    @RequestMapping(value = "getNameByNameLike")
    public List<String> getNameByNameLike(String name){
        return bdCustomerService.findNameByNameLike(name);
    }

}
