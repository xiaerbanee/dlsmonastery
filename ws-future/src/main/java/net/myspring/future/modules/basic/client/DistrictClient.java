package net.myspring.future.modules.basic.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wangzm on 2017/4/21.
 */
@FeignClient("summer-basic")
public interface DistrictClient {

    @RequestMapping(method = RequestMethod.GET, value = "/sys/companyConfig/getValueByCode")
    String getValueByCode(@RequestParam(value = "code") String code);
}
