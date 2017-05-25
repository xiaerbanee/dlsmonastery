package net.myspring.future.modules.third.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
@FeignClient("global-tool")
public interface OppoClient {

    @RequestMapping(method = RequestMethod.GET, value = "/oppo/synIme")
   String  findSynImeList(@RequestParam(value = "date")String date);

}
