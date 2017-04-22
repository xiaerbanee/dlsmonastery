package net.myspring.future.modules.basic.client;

import net.myspring.future.modules.basic.dto.BasicDistrictDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by wangzm on 2017/4/21.
 */
@FeignClient("summer-basic")
public interface DistrictClient {

    @RequestMapping(method = RequestMethod.GET, value = "/sys/companyConfig/getValueByCode")
    String getValueByCode(@RequestParam(value = "code") String code);

    @RequestMapping(method = RequestMethod.GET, value = "/sys/district/getByParentId")
    List<BasicDistrictDto> findByParentId(@RequestParam(value = "parentId") String parentId);
}
