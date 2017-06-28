package net.myspring.tool.common.client;

import net.myspring.tool.common.dto.CustomerDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by guolm on 2017/6/11.
 */

@FeignClient("ws-future")
public interface CustomerClient {

    @RequestMapping(method = RequestMethod.GET, value = "/basic/depot/findOppoCustomers")
    List<CustomerDto> findCustomerDtoList();


}
