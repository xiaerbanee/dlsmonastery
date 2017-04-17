package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.AccountChangeTypeEnum;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.modules.hr.dto.AccountChangeDto;
import net.myspring.basic.modules.hr.service.AccountChangeService;
import net.myspring.basic.modules.hr.service.OfficeService;
import net.myspring.basic.modules.hr.service.PositionService;
import net.myspring.basic.modules.hr.web.form.AccountChangeForm;
import net.myspring.basic.modules.hr.web.query.AccountChangeQuery;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "hr/accountChange")
public class AccountChangeController {

    @Autowired
    private AccountChangeService accountChangeService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private PositionService positionService;

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public Map<String,Object> detail(AccountChangeForm accountChangeForm) {
        Map<String,Object> paramMap= Maps.newHashMap();
        paramMap.put("accountChange", accountChangeForm);
        return paramMap;
    }

    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public String audit(AccountChangeForm accountChangeForm,boolean pass,String comment) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<AccountChangeDto> list(Pageable pageable, AccountChangeQuery accountChangeQuery){
        Page<AccountChangeDto> page = accountChangeService.findPage(pageable,accountChangeQuery);
        return page;
    }

    @RequestMapping(value="getListProperty")
    public AccountChangeQuery getListProperty(AccountChangeQuery accountChangeQuery){
        accountChangeQuery.setAreaList(officeService.findByType(Const.OFFICE_TYPE_AREA));
        accountChangeQuery.setTypeList(AccountChangeTypeEnum.getList());
        return accountChangeQuery;
    }

    @RequestMapping(value = "findOne")
    public AccountChangeForm findOne(AccountChangeForm accountChangeForm){
        accountChangeForm=accountChangeService.findForm(accountChangeForm.getId());
        accountChangeForm.setTypeList(AccountChangeTypeEnum.getList());
        accountChangeForm.setPositionList(positionService.findAll());
        return accountChangeForm;
    }
}
