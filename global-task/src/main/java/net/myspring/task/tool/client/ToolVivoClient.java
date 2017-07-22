package net.myspring.task.tool.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by guolm on 2017/6/11.
 */

@FeignClient("global-tool")
public interface ToolVivoClient {

    @RequestMapping(method = RequestMethod.GET, value = "/factory/vivo/pullFactoryData")
    String pullFactoryData(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "date") String date);

    @RequestMapping(method = RequestMethod.GET, value = "/factory/vivo/pushToLocal")
    String pushToLocal(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "date") String date);


}
