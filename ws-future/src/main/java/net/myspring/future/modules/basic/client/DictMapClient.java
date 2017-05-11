package net.myspring.future.modules.basic.client;

import net.myspring.basic.modules.sys.dto.DictEnumDto;
import net.myspring.basic.modules.sys.dto.DictMapDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by wangzm on 2017/5/11.
 */
@FeignClient("summer-basic")
public interface DictMapClient {

    @RequestMapping(method = RequestMethod.GET, value = "/sys/dictMap/findByCategory")
    List<DictMapDto> findByCategory(@RequestParam(value = "category") String category);
}
