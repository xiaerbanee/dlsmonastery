package net.myspring.tool.common.client;

import net.myspring.tool.common.domain.DistrictEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("summer-general")
public interface DistrictClient {

    @RequestMapping(method = RequestMethod.GET, value = "/sys/district/findAll")
    List<DistrictEntity> findDistrictList();
}
