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
    List<String> getOfficeFilterIds(@RequestParam(value = "accountId") String accountId);

    @RequestMapping(method = RequestMethod.GET, value = "/sys/office/findAreaByType")
    List<String> findAreaByType(@RequestParam(value = "type") String type);

    @RequestMapping(method = RequestMethod.GET, value = "/sys/office/getOfficeIdsByAccountId")
    List<String> getOfficeIdsByAccountId(@RequestParam(value = "accountId") String accountId);

    @RequestMapping(method = RequestMethod.GET, value = "/sys/office/getOfficeIdByOfficeType")
    String getOfficeIdByOfficeType(@RequestParam(value = "officeId") String officeId,@RequestParam(value = "officeType")String officeType);

    @RequestMapping(method = RequestMethod.GET, value = "/sys/office/findByAreaIds")
    List<OfficeDto> findByAreaIds(@RequestParam(value = "areaIds") List<String> areaIds);

    @RequestMapping(method = RequestMethod.GET,value = "/sys/office/findByOfficeRuleName")
    List<OfficeDto> findByOfficeRuleName(@RequestParam(value = "officeRuleName")String officeRuleName);

    @RequestMapping(method = RequestMethod.GET,value = "/sys/office/findSortList")
    List<OfficeDto> findSortList();

}
