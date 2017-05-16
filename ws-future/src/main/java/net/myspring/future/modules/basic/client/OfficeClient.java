package net.myspring.future.modules.basic.client;

import net.myspring.basic.modules.sys.dto.OfficeDto;
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

    @RequestMapping(method = RequestMethod.GET, value = "/sys/office/getOfficeFilterIds")
    List<String> getOfficeFilterIds(@RequestParam(value = "officeId") String officeId);

    @RequestMapping(method = RequestMethod.GET,value = "/sys/office/findByOfficeRuleName")
    List<OfficeDto> findByOfficeRuleName(@RequestParam(value = "officeRuleName")String officeRuleName);

    @RequestMapping(method = RequestMethod.GET, value = "/sys/office/getOfficeOfSameArea")
    List<String> getOfficeOfSameArea(@RequestParam(value = "officeId") String officeId);

}
