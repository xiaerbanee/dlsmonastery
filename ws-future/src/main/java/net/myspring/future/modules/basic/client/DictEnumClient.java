package net.myspring.future.modules.basic.client;

import net.myspring.basic.modules.sys.dto.DictEnumDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by wangzm on 2017/5/11.
 */
@FeignClient("summer-basic")
public interface DictEnumClient {

    @RequestMapping(method = RequestMethod.GET, value = "/sys/dictEnum/findByCategory")
    List<DictEnumDto> findByCategory(@RequestParam(value = "category") String category);
}
