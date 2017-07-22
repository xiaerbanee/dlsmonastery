package net.myspring.tool.common.client;

import net.myspring.tool.common.dto.OfficeDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by wangzm on 2017/4/21.
 */
@FeignClient("summer-basic")
public interface OfficeClient {

    @RequestMapping(method = RequestMethod.GET,value = "/sys/office/findAllChildCount")
    List<OfficeDto> findAll();
}
