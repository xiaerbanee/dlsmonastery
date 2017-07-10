package net.myspring.basic.modules.sys.client;

import net.myspring.general.modules.sys.dto.FolderFileFeignDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by wangzm on 2017/7/10.
 */
@FeignClient("summer-general")
public interface ProcessTypeClient {

    @RequestMapping(method = RequestMethod.GET, value = "/sys/processType/findIdByViewPositionId")
    List<String> findByViewPositionId(@RequestParam(value = "positionId") String positionId);

}
