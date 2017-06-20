package net.myspring.tool.modules.oppo.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by guolm on 2017/6/11.
 */

@FeignClient("ws-future")
public interface CustomerClient {

    @RequestMapping(method = RequestMethod.GET, value = "/basic/depot/getCustomers")
    String  findCustomerDtoList();


}
