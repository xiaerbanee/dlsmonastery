package net.myspring.task.tool.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by guolm on 2017/6/11.
 */

@FeignClient("global-tool")
public interface FactoryClient {

    @RequestMapping(method = RequestMethod.GET, value = "/factory/oppo/pullFactoryData")
    String pullOppoFactoryData(@RequestParam(value="date") String date);

}
