package net.myspring.cloud.modules.sys.web.controller;

import net.myspring.cloud.modules.sys.dto.AccountKingdeeBookDto;
import net.myspring.cloud.modules.sys.service.AccountKingdeeBookService;
import net.myspring.cloud.modules.sys.web.form.AccountKingdeeBookForm;
import net.myspring.cloud.modules.sys.web.query.AccountKingdeeBookQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by lihx on 2017/5/10.
 */
@RestController
@RequestMapping(value = "sys/accountKingdeeBook")
public class AccountKingdeeBookController {
    @Autowired
    private AccountKingdeeBookService accountKingdeeBookService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AccountKingdeeBookDto> page(Pageable pageable, AccountKingdeeBookQuery accountKingdeeBookQuery){
        Page<AccountKingdeeBookDto> page = accountKingdeeBookService.findPage(pageable,accountKingdeeBookQuery);
        return page;
    }

    @RequestMapping(value = "getQuery")
    public AccountKingdeeBookQuery getQuery(){
        return accountKingdeeBookService.getQuery();
    }

    @RequestMapping(value = "form")
    public AccountKingdeeBookForm getForm(AccountKingdeeBookForm accountKingdeeBookForm){
        return accountKingdeeBookService.getForm(accountKingdeeBookForm);
    }

    @RequestMapping(value = "save")
    public RestResponse save(AccountKingdeeBookForm accountKingdeeBookForm){
        accountKingdeeBookService.save(accountKingdeeBookForm);
        return new RestResponse("保存成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        accountKingdeeBookService.logicDelete(id);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }
}
