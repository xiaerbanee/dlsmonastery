package net.myspring.future.modules.basic.client;

import net.myspring.future.modules.basic.dto.BasicOfficeDto;
import net.myspring.future.modules.basic.dto.OfficeBasicDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import redis.clients.jedis.BinaryClient;

import java.util.List;

/**
 * Created by wangzm on 2017/4/21.
 */
@FeignClient("summer-basic")
public interface OfficeClient {

    @RequestMapping(method = RequestMethod.GET, value = "/hr/office/getOfficeFilterIds")
    List<String> getOfficeFilterIds(@RequestParam(value = "accountId") String accountId);

    @RequestMapping(method = RequestMethod.GET, value = "/hr/office/findAreaByType")
    List<String> findAreaByType(@RequestParam(value = "type") String type);

    @RequestMapping(method = RequestMethod.GET, value = "/hr/office/getOfficeIdsByAccountId")
    List<String> getOfficeIdsByAccountId(@RequestParam(value = "accountId") String accountId);

    @RequestMapping(method = RequestMethod.GET, value = "/hr/office/getOfficeIdByOfficeType")
    String getOfficeIdByOfficeType(@RequestParam(value = "officeId") String officeId,@RequestParam(value = "officeType")String officeType);

    @RequestMapping(method = RequestMethod.GET, value = "/hr/office/findByAreaIds")
    List<BasicOfficeDto> findByAreaIds(@RequestParam(value = "areaIds") List<String> areaIds);

    @RequestMapping(method = RequestMethod.GET,value = "/hr/office/findByType")
    List<OfficeBasicDto> findByType(@RequestParam(value = "type")String type);

}
