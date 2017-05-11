package net.myspring.cloud.modules.sys.web.controller;

import net.myspring.cloud.modules.sys.dto.AccountKingdeeBookDto;
import net.myspring.cloud.modules.sys.service.AccountKingdeeBookService;
import net.myspring.cloud.modules.sys.web.query.AccountKingdeeBookQuery;
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
}
