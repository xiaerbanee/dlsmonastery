package net.myspring.task.tool.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ws-future")
public interface FutureImooClient {

    @RequestMapping(method = RequestMethod.GET, value = "/third/factory/imoo/pullFactoryData")
    String pullFactoryData(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "date") String date);

}
