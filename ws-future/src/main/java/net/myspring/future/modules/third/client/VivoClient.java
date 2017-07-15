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

    @RequestMapping(method = RequestMethod.GET, value = "/factory/vivo/synIme")
    List<VivoPlantSendimei> findSynImeList(@RequestParam(value = "date") String date, @RequestParam(value = "agentCode") String agentCode);

    @RequestMapping(method = RequestMethod.GET, value = "/factory/vivo/synPlantElectronicsn")
    List<VivoPlantElectronicsn>  synProductItemelectronSel(@RequestParam(value = "date") String date, @RequestParam(value = "agentCode") String agentCode);
}
