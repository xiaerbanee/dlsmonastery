package net.myspring.cloud.modules.kingdee.web.controller;

import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.kingdee.service.BdCustomerService;
import net.myspring.cloud.modules.kingdee.web.query.BdCustomerQuery;
import net.myspring.common.dto.NameValueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(method = RequestMethod.GET)
    public Page<BdCustomer> list(Pageable pageable, BdCustomerQuery bdCustomerQuery){
        Page<BdCustomer> page = bdCustomerService.findPageIncloudeForbid(pageable,bdCustomerQuery);
        return page;
    }


    @RequestMapping(value = "findByNameLike")
    public List<BdCustomer> findByNameLike(String name){
        List<BdCustomer> bdCustomerList = bdCustomerService.findByNameLike(name);
        return bdCustomerList;
    }

    @RequestMapping(value = "findCustomerGroupList")
    public List<NameValueDto> getCustomerGroupList(){
        return bdCustomerService.findCustomerGroupList();
    }

    @RequestMapping(value = "findAll")
    public List<BdCustomer>  findAll(){
        return bdCustomerService.findAll();
    }

}
