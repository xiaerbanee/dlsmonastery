package net.myspring.tool.modules.oppo.client;

import feign.Param;
import net.myspring.tool.modules.oppo.domain.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by guolm on 2017/6/11.
 */

@FeignClient("ws-future")
public interface OppoClient {

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerAllots")
    List<OppoCustomerAllot> findOppoCustomerAllots(@RequestParam(value = "dateStart")String dateStart, @RequestParam(value = "dateEnd") String dateEnd);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerStocks")
    List<OppoCustomerStock> findOppoCustomerStocks(@RequestParam(value = "dateStart")String dateStart, @RequestParam(value = "dateEnd") String dateEnd);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerImeiStocks")
    List<OppoCustomerImeiStock> findOppoCustomerImeiStocks(@RequestParam(value = "dateStart")String dateStart, @RequestParam(value = "dateEnd") String dateEnd);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerSales")
    List<OppoCustomerSale> findOppoCustomerSales(@RequestParam(value = "dateStart")String dateStart, @RequestParam(value = "dateEnd") String dateEnd);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerSaleImes")
    List<OppoCustomerSaleImei> findOppoCustomerSaleImes(@RequestParam(value = "dateStart")String dateStart, @RequestParam(value = "dateEnd") String dateEnd);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerSaleCounts")
    List<OppoCustomerSaleCount> findOppoCustomerSaleCounts(@RequestParam(value = "dateStart")String dateStart, @RequestParam(value = "dateEnd") String dateEnd);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerAfterSaleImeis")
    List<OppoCustomerAfterSaleImei> findOppoCustomerAfterSaleImes(@RequestParam(value = "dateStart")String dateStart, @RequestParam(value = "dateEnd") String dateEnd);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerDemoPhones")
    List<OppoCustomerDemoPhone>  findOppoCustomerDemoPhones(@RequestParam(value = "dateStart")String dateStart, @RequestParam(value = "dateEnd") String dateEnd);

}
