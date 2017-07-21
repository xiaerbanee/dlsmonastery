package net.myspring.future.modules.third.client;

import net.myspring.future.modules.third.dto.OppoPlantProductItemelectronSel;
import net.myspring.future.modules.third.dto.OppoPlantSendImeiPpsel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("global-tool")
public interface OppoClient {

    @RequestMapping(method = RequestMethod.GET, value = "/factory/oppo/getSendImeList")
    List<OppoPlantSendImeiPpsel> getSendImeList(@RequestParam(value = "date")String date, @RequestParam(value="agentCode")String agentCode);

    @RequestMapping(method = RequestMethod.GET, value = "/factory/oppo/getItemelectronSelList")
    List<OppoPlantProductItemelectronSel>  getItemelectronSelList(@RequestParam(value = "date")String date, @RequestParam(value="agentCode")String agentCode);
}
