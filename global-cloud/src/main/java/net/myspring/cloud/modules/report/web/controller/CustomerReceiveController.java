package net.myspring.cloud.modules.report.web.controller;

import net.myspring.cloud.modules.report.dto.CustomerReceiveDetailDto;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import net.myspring.cloud.modules.report.service.CustomerReceiveService;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by liuj on 2017/5/11.
 */
@RestController
@RequestMapping(value = "report/customerReceive")
public class CustomerReceiveController {
    @Autowired
    private CustomerReceiveService customerReceiveService;

    @RequestMapping(value = "list")
    public Map<String,CustomerReceiveDto> list(CustomerReceiveQuery customerReceiveQuery) {
        List<CustomerReceiveDto> customerReceiveDtoList =  customerReceiveService.findCustomerReceiveDtoList(customerReceiveQuery);
        return customerReceiveDtoList.stream().collect(Collectors.toMap(CustomerReceiveDto::getCustomerId,customerReceiveDto -> customerReceiveDto));
    }

    @RequestMapping(value = "detail")
    public List<CustomerReceiveDetailDto> detail(String dateRange,String customerId) {
        return customerReceiveService.findCustomerReceiveDetailDtoList(dateRange,customerId);
    }

}
