package net.myspring.future.modules.third.client;

import net.myspring.future.modules.third.dto.VivoPlantElectronicsn;
import net.myspring.future.modules.third.dto.VivoPlantSendimei;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("global-tool")
public interface VivoClient {

    @RequestMapping(method = RequestMethod.GET, value = "/factory/vivo/getSendImeList")
    List<VivoPlantSendimei> getSendImeList(@RequestParam(value = "companyName")String companyName, @RequestParam(value = "date") String date, @RequestParam(value = "agentCode") String agentCode);

    @RequestMapping(method = RequestMethod.GET, value = "/factory/vivo/getItemelectronSelList")
    List<VivoPlantElectronicsn>  getItemelectronSelList(@RequestParam(value = "companyName")String companyName, @RequestParam(value = "date") String date, @RequestParam(value = "agentCode") String agentCode);
}
