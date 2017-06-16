package net.myspring.future.modules.basic.client;

import net.myspring.basic.modules.sys.dto.AccountCommonDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("summer-basic")
public interface AccountClient {

    @RequestMapping(method = RequestMethod.GET, value = "/hr/account/findByLoginNameList")
    List<AccountCommonDto> findByLoginNameList(@RequestParam(value = "loginNameList") List<String> loginNameList);
}
