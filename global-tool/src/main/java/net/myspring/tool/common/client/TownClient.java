package net.myspring.tool.common.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by guolm on 2017/6/11.
 */

@FeignClient("summer-general")
public interface TownClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/general/sys/district/findAll")
    String  findDistrictList();
}
