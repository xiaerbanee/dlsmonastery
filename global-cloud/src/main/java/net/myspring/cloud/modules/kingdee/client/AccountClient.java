package net.myspring.cloud.modules.kingdee.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by lihx on 2017/4/25.
 */
@FeignClient("summer-basic")
public interface AccountClient {

    @RequestMapping(method = RequestMethod.GET, value = "/hr/account/getName")
    String getName(@RequestParam(value = "accountId") String accountId);

}
