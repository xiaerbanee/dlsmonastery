package net.myspring.cloud.modules.report.web.controller;

import net.myspring.cloud.modules.report.dto.CustomerAccountDetailDto;
import net.myspring.cloud.modules.report.dto.CustomerAccountDto;
import net.myspring.cloud.modules.report.web.query.CustomerAccountQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by liuj on 2017/5/11.
 */
@RestController
@RequestMapping(value = "report/customerAccount")
public class CustomerAccountController {

    @RequestMapping(value = "list")
    public List<CustomerAccountDto> list(CustomerAccountQuery customerAccountQuery) {
        return null;
    }

    @RequestMapping(value = "detail")
    public List<CustomerAccountDetailDto> list(CustomerAccountDetailDto customerAccountDetailDto) {
        return null;
    }

}
