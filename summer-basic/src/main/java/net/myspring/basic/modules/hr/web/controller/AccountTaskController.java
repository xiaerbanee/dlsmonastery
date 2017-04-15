package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.AccountTask;
import net.myspring.basic.modules.hr.dto.AccountTaskDto;
import net.myspring.basic.modules.hr.service.AccountTaskService;
import net.myspring.basic.modules.hr.web.query.AccountTaskQuery;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "hr/accountTask")
public class AccountTaskController {

    @Autowired
    private AccountTaskService accountTaskService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AccountTaskDto> list(Pageable pageable, AccountTaskQuery accountTaskQuery) {
        Page<AccountTaskDto> page=accountTaskService.findPage(pageable, accountTaskQuery);
        return page;
    }
}
