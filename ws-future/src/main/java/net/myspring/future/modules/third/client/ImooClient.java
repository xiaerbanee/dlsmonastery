package net.myspring.future.modules.third.client;

import net.myspring.future.modules.third.dto.ImooPrdocutImeiDeliver;
import net.myspring.future.modules.third.dto.ImooProductDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@FeignClient("global-tool")
public interface ImooClient {

    @RequestMapping(method = RequestMethod.GET,value = "/factory/imoo/getImooProductDtoMap")
    Map<String,ImooProductDto> getImooProductDtoMap();

    @RequestMapping(method = RequestMethod.GET,value = "/factory/imoo/getSendImeList")
    List<ImooPrdocutImeiDeliver> getSendImeList(@RequestParam(value = "dateStart")LocalDate dateStart, @RequestParam(value = "dateStart")LocalDate dateEnd, @RequestParam(value = "agentCodeList")List<String> agentCodeList);

}
