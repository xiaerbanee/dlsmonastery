package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.crm.domain.Bank;
import net.myspring.future.modules.crm.service.BankService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "crm/bank")
public class BankController {

    @Autowired
    private BankService bankService;

    @ModelAttribute
    public Bank get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new Bank() : bankService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Page<Bank> page = bankService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(Bank bank: page.getContent()){
            bank.setActionList(getActionList());
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "save")
    public String save(Bank bank){
        bankService.save(bank);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));

    }

    @RequestMapping(value = "syn")
    public String syn(){
        bankService.syn();
        return ObjectMapperUtils.writeValueAsString(new RestResponse("同步成功"));
    }

    @RequestMapping(value = "delete")
    public String delete(Bank bank, BindingResult bindingResult) {
        bankService.delete(bank);
        RestResponse restResponse=new RestResponse("删除成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "findOne")
    public String findOne(Bank bank){
        return ObjectMapperUtils.writeValueAsString(bank);
    }

    private List<String> getActionList() {
        List<String> actionList = Lists.newArrayList();
        if(SecurityUtils.getAuthorityList().contains("crm:bank:edit")){
            actionList.add(Const.ITEM_ACTION_EDIT);
        }
        return actionList;
    }

}
