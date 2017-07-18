package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.common.enums.DutyDateTypeEnum;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.AccountWeixin;
import net.myspring.basic.modules.hr.dto.AccountWeixinDto;
import net.myspring.basic.modules.hr.dto.DutyFreeDto;
import net.myspring.basic.modules.hr.service.AccountWeixinService;
import net.myspring.basic.modules.hr.web.form.AccountWeixinForm;
import net.myspring.basic.modules.hr.web.form.DutyFreeForm;
import net.myspring.basic.modules.hr.web.query.AccountQuery;
import net.myspring.basic.modules.hr.web.query.AccountWeixinQuery;
import net.myspring.basic.modules.hr.web.query.DutyFreeQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuj on 2017/3/19.
 */
@RestController
@RequestMapping(value = "hr/accountWeixin")
public class AccountWeixinController {

    @Autowired
    private AccountWeixinService accountWeixinService;

    @RequestMapping(value = "getQuery")
    public AccountWeixinQuery getQuery(AccountWeixinQuery accountWeixinQuery) {
        return accountWeixinQuery;
    }


    @RequestMapping(method = RequestMethod.GET)
    public Page<AccountWeixinDto> list(Pageable pageable, AccountWeixinQuery accountWeixinQuery) {
        Page<AccountWeixinDto> page = accountWeixinService.findPage(pageable, accountWeixinQuery);
        return page;
    }

    @RequestMapping(value="getForm")
    public AccountWeixinForm getForm(AccountWeixinForm accountWeixinForm){
        return accountWeixinForm;
    }


    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public RestResponse delete(String id) {
        accountWeixinService.logicDelete(id);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(AccountWeixinForm accountWeixinForm) {
        accountWeixinService.save(accountWeixinForm);
        return new RestResponse("保存成功",null);
    }


    @RequestMapping(value = "findByAccountId")
    public AccountWeixinDto findByAccountId(String accountId) {
        return accountWeixinService.findByAccountId(accountId);
    }
}
