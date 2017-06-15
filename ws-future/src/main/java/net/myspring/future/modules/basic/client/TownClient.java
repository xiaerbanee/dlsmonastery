package net.myspring.future.modules.basic.client;

import net.myspring.general.modules.sys.dto.TownFeignDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("summer-general")
public interface TownClient {

    @RequestMapping(method = RequestMethod.GET, value = "/sys/town/findOne")
    TownFeignDto findOne(@RequestParam(value = "id") String id);
}
