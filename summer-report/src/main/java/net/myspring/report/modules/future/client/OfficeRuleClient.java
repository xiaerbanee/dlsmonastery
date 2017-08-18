package net.myspring.report.modules.future.client;

import net.myspring.basic.modules.sys.dto.OfficeRuleDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@FeignClient("summer-basic")
public interface OfficeRuleClient {

    @RequestMapping(method = RequestMethod.GET, value = "/sys/officeRule/findAll")
    List<OfficeRuleDto> findAll();

    @RequestMapping(method = RequestMethod.GET, value = "/sys/officeRule/getOfficeRuleMap")
    Map<String,Map<String,String>> getOfficeRuleMap();

}
