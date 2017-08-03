package net.myspring.task.tool.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "global-tool")
public interface ToolImooClient {

    @RequestMapping(method = RequestMethod.GET,value = "/factory/imoo/pullFactoryData")
    String pullFactoryDate(@RequestParam(value = "companyName")String companyName,@RequestParam(value = "date")String name);

}
