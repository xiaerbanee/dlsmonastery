package net.myspring.cloud.modules.report.web.controller;

import net.myspring.cloud.modules.report.dto.CustomerReceiveDetailDto;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import net.myspring.cloud.modules.report.service.CustomerReceiveService;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveDetailQuery;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by liuj on 2017/5/11.
 */
@RestController
@RequestMapping(value = "report/customerReceive")
public class CustomerReceiveController {
    @Autowired
    private CustomerReceiveService customerReceiveService;

    @RequestMapping(value = "page")
    public List<CustomerReceiveDto> page(CustomerReceiveQuery customerReceiveQuery) {
        List<CustomerReceiveDto> customerReceiveDtoList =  customerReceiveService.findCustomerReceiveDtoList(customerReceiveQuery);
        return customerReceiveDtoList;
    }

    @RequestMapping(value = "list",method = RequestMethod.POST)
    public List<CustomerReceiveDto> list(@RequestBody CustomerReceiveQuery customerReceiveQuery) {
        List<CustomerReceiveDto> customerReceiveDtoList =  customerReceiveService.findCustomerReceiveDtoList(customerReceiveQuery);
        return customerReceiveDtoList;
    }

    @RequestMapping(value = "detail")
    public List<CustomerReceiveDetailDto> detail(CustomerReceiveDetailQuery customerReceiveDetailQuery) {
        return customerReceiveService.findCustomerReceiveDetailDtoList(customerReceiveDetailQuery);
    }

    @RequestMapping(value = "getQuery")
    public CustomerReceiveQuery getQuery() {
        return customerReceiveService.getQuery();
    }

}
