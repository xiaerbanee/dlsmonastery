package net.myspring.future.modules.basic.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("summer-general")
public interface SequenceClient {

    @RequestMapping(value = "/sys/sequence/nextVal")
    Long nextVal(String key);

}
