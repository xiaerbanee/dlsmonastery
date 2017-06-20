package net.myspring.tool.modules.oppo.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by guolm on 2017/6/11.
 */

@FeignClient("ws-future")
public interface OppoCustomerAllotClient {

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/getOppoCustomerAllots")
    String  findOppoCustomerAllots(String dateStart,String dateEnd,String companyId);
}
