package net.myspring.future.modules.basic.client;

import net.myspring.common.dto.NameValueDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by lihx on 2017/4/21.
 */
@FeignClient("summer-basic")
public interface DictEnumClient {
    @RequestMapping(method = RequestMethod.GET, value = "/sys/dictEnum/getDictEnumByCategory")
    List<NameValueDto> findDictEnumByCategory(@RequestParam(value = "category") String category);
}
