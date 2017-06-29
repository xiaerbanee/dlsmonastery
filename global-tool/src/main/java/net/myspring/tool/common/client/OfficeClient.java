package net.myspring.tool.common.client;

import net.myspring.basic.modules.sys.dto.OfficeDto;
import net.myspring.tool.common.domain.OfficeEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/4/21.
 */
@FeignClient("summer-basic")
public interface OfficeClient {

    @RequestMapping(method = RequestMethod.GET,value = "/sys/office/findAll")
    List<OfficeEntity> findAll();
}
