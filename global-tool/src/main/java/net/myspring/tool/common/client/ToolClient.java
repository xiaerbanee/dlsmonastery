package net.myspring.tool.common.client;

import net.myspring.tool.common.domain.ProductEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("global-tool")
public interface ToolClient {

    @RequestMapping(method = RequestMethod.GET, value = "oppo/syn")
    String synFactoryOppo(@RequestParam(value="date") String date);
}
