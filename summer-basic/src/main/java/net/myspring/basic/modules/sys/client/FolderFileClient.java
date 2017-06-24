package net.myspring.basic.modules.sys.client;

import net.myspring.general.modules.sys.dto.ActivitiStartDto;
import net.myspring.general.modules.sys.dto.FolderFileFeignDto;
import net.myspring.general.modules.sys.form.ActivitiStartForm;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("summer-general")
public interface FolderFileClient {

    @RequestMapping(method = RequestMethod.GET, value = "/sys/folderFile/findById")
    FolderFileFeignDto findById(@RequestParam(value = "id") String id);

}
