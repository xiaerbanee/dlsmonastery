package net.myspring.basic.modules.hr.client;

import net.myspring.general.modules.sys.dto.ProcessTypeDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by wangzm on 2017/4/25.
 */
@FeignClient("summer-general")
public interface ProcessTypeClient {

    @RequestMapping(value = "/sys/processType/findAll",method = RequestMethod.GET)
    List<ProcessTypeDto> findAll();
}
