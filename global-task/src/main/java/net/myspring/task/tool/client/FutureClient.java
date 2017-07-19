package net.myspring.task.tool.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by guolm on 2017/6/11.
 */

@FeignClient("ws-future")
public interface FutureClient {

    @RequestMapping(method = RequestMethod.GET, value = "/third/factory/oppo/synIme")
    String synOppoIme(@RequestParam(value = "date") String date);

    @RequestMapping(method = RequestMethod.GET, value = "/third/factory/vivo/synIme")
    String synVivoIme(@RequestParam(value = "date") String date);

}
