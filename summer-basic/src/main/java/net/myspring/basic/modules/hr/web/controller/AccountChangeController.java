package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.AccountChangeTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.modules.hr.dto.AccountChangeDto;
import net.myspring.basic.modules.hr.service.AccountChangeService;
import net.myspring.basic.modules.hr.service.OfficeService;
import net.myspring.basic.modules.hr.service.PositionService;
import net.myspring.basic.modules.hr.web.form.AccountChangeForm;
import net.myspring.basic.modules.hr.web.query.AccountChangeQuery;
import net.myspring.basic.modules.sys.domain.ProcessType;
import net.myspring.basic.modules.sys.service.ProcessTypeService;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @Autowired
    private ProcessTypeService processTypeService;

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
        accountChangeQuery.setAreaList(officeService.findByType(Const.OFFICE_TYPE_AREA));
        accountChangeQuery.setTypeList(AccountChangeTypeEnum.getList());
        return accountChangeQuery;
    }

    @RequestMapping(value = "findData")
    public AccountChangeForm findOne(AccountChangeQuery accountChangeQuery){
        AccountChangeForm accountChangeForm=accountChangeService.findForm(accountChangeQuery);
        accountChangeForm.setTypeList(AccountChangeTypeEnum.getList());
        accountChangeForm.setPositionList(positionService.findAll());
        return accountChangeForm;
    }
}
