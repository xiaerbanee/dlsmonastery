package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.dto.AccountTaskDto;
import net.myspring.basic.modules.hr.service.AccountTaskService;
import net.myspring.common.domain.SearchEntity;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "hr/accountTask")
public class AccountTaskController {

    @Autowired
    private AccountTaskService accountTaskService;
    @Autowired
    private SecurityUtils securityUtils;


    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().put("positionId",securityUtils.getPositionId());
        Page<AccountTaskDto> page=accountTaskService.findPage(searchEntity.getPageable(), searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(page);
    }
}
