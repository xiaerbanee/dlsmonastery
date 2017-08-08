package net.myspring.future.modules.basic.web.controller;

import com.google.common.collect.Lists;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.service.AccountDepotService;
import net.myspring.future.modules.basic.web.form.AccountDepotForm;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "basic/accountDepot")
public class AccountDepotController {

    @Autowired
    private AccountDepotService accountDepotService;

    @RequestMapping(value = "findByAccountId")
    public List<String> findByAccountId(String accountId){
        List<String> depotIdList= Lists.newArrayList();
        if(StringUtils.isNotBlank(accountId)){
            depotIdList=accountDepotService.findByAccountId(accountId);
        }
        return depotIdList;
    }

    @RequestMapping(value = "save")
    public RestResponse findByAccountId(AccountDepotForm accountDepotForm){
        accountDepotService.save(accountDepotForm);
        return new RestResponse("保存成功",null);
    }
}
