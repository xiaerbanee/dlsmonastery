package net.myspring.cloud.modules.kingdee.web;

import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.kingdee.service.BdCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lihx on 2017/5/12.
 */
@RestController
@RequestMapping(value = "kingdee/bdCustomer")
public class BdCustomerController {
    @Autowired
    private BdCustomerService bdCustomerService;

    @RequestMapping(value = "getNameByNameLike")
    public List<String> getNameByNameLike(String name){
        return bdCustomerService.getNameByNameLike(name);
    }

    @RequestMapping(value = "getCustomerGroupList")
    public List<BdCustomer> getCustomerGroupList(){
        return bdCustomerService.getCustomerGroupList();
    }

    @RequestMapping(value = "getByNameLike")
    public List<BdCustomer> getByNameLike(String name){
        return bdCustomerService.getByNameLike(name);
    }
}
