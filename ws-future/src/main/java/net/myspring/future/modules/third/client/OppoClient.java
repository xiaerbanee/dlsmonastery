package net.myspring.future.modules.third.client;

import net.myspring.future.modules.third.domain.OppoPlantProductItemelectronSel;
import net.myspring.future.modules.third.domain.OppoPlantSendImeiPpsel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
@FeignClient("global-tool")
public interface OppoClient {

    @RequestMapping(method = RequestMethod.GET, value = "oppo/syn")
    String synFactoryOppo(@RequestParam(value="date") String date);

    @RequestMapping(method = RequestMethod.GET, value = "/oppo/synIme")
   List<OppoPlantSendImeiPpsel>  findSynImeList(@RequestParam(value = "date")String date, @RequestParam(value="agentCode")String agentCode);

    @RequestMapping(method = RequestMethod.GET, value = "/oppo/synProductItemelectronSel")
    List<OppoPlantProductItemelectronSel>  synProductItemelectronSel(@RequestParam(value = "date")String date, @RequestParam(value="agentCode")String agentCode);
}
