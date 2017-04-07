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
    public Map<String,Object> getListProperty(){
        Map<String,Object> map = Maps.newHashMap();
        map.put("areas",officeService.findByType(Const.OFFICE_TYPE_AREA));
        map.put("types", AccountChangeTypeEnum.values());
        return map;
    }

    @RequestMapping(value = "getFormProperty")
    public Map<String,Object> getFormProperty(){
        Map<String,Object> map = Maps.newHashMap();
        map.put("types",AccountChangeTypeEnum.values());
        map.put("positions",positionService.findAll());
        return map;
    }

    @RequestMapping(value = "findOne")
    public AccountChangeDto findOne(String id ){
        AccountChangeDto accountChangeDto=accountChangeService.findDto(id);
        return accountChangeDto;
    }
}
