package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.common.enums.AccountChangeTypeEnum;
import net.myspring.basic.common.enums.OfficeRuleEnum;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.modules.hr.dto.AccountChangeDto;
import net.myspring.basic.modules.hr.service.AccountChangeService;
import net.myspring.basic.modules.sys.service.OfficeService;
import net.myspring.basic.modules.hr.service.PositionService;
import net.myspring.basic.modules.hr.web.form.AccountChangeForm;
import net.myspring.basic.modules.hr.web.query.AccountChangeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "hr/accountChange")
public class AccountChangeController {

    @Autowired
    private AccountChangeService accountChangeService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private PositionService positionService;

    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public String audit(AccountChangeForm accountChangeForm,boolean pass,String comment) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<AccountChangeDto> list(Pageable pageable, AccountChangeQuery accountChangeQuery){
        Page<AccountChangeDto> page = accountChangeService.findPage(pageable,accountChangeQuery);
        return page;
    }

    @RequestMapping(value="getQuery")
    public AccountChangeQuery getQuery(AccountChangeQuery accountChangeQuery){
        accountChangeQuery.setAreaList(officeService.findByOfficeRuleName("办事处"));
        accountChangeQuery.setTypeList(AccountChangeTypeEnum.getList());
        return accountChangeQuery;
    }

    @RequestMapping(value = "findData")
    public AccountChangeForm findForm(AccountChangeQuery accountChangeQuery){
        AccountChangeForm accountChangeForm=accountChangeService.findForm(accountChangeQuery);
        accountChangeForm.setTypeList(AccountChangeTypeEnum.getList());
        accountChangeForm.setPositionList(positionService.findAll());
        return accountChangeForm;
    }
}
