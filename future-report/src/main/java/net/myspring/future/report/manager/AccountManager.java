package net.myspring.future.report.manager;

import net.myspring.hr.domain.Account;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by liuj on 2017/3/28.
 */
@FeignClient("summer-hr")
public interface AccountManager {

    @RequestMapping(method = RequestMethod.GET, value = "/account/findOne")
    String findOne(@RequestParam(value = "id") String id);

}
