package net.myspring.tool.common.client;

import net.myspring.tool.modules.future.domain.Office;
import net.myspring.tool.modules.future.dto.OfficeDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by wangzm on 2017/4/21.
 */
@FeignClient("summer-basic")
public interface OfficeClient {
    @RequestMapping(method = RequestMethod.GET,value = "/sys/office/findAll")
    List<Office> findAll(@RequestParam(value = "companyName")String companyName);

    @RequestMapping(method = RequestMethod.GET,value = "/sys/office/findAllChildCount")
    List<OfficeDto> findAllChildCount(@RequestParam(value = "companyName")String companyName);

    @RequestMapping(method = RequestMethod.GET,value = "/sys/office/findDistinctAgentCode")
    List<String> findDistinctAgentCode(@RequestParam(value = "companyName")String companyName);
}
