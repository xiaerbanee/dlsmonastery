package net.myspring.future.modules.basic.client;

import com.sun.org.glassfish.gmbal.NameValue;
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
public interface DictMapClient {
    @RequestMapping(method = RequestMethod.GET, value = "/sys/dictMap/getDictMapByCategory")
    List<NameValueDto> findDictMapByCategory(@RequestParam(value = "category") String category);
}
