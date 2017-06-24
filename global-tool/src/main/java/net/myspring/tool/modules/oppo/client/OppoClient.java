package net.myspring.tool.modules.oppo.client;

import feign.Param;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by guolm on 2017/6/11.
 */

@FeignClient("ws-future")
public interface OppoClient {

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerAllots")
    String  findOppoCustomerAllots(@RequestParam(value = "dateStart")String dateStart, @RequestParam(value = "dateEnd") String dateEnd, @RequestParam(value = "companyId") String companyId);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerStocks")
    String  findOppoCustomerStocks(@RequestParam(value = "dateStart")String dateStart, @RequestParam(value = "dateEnd") String dateEnd, @RequestParam(value = "companyId") String companyId);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerImeiStocks")
    String  findOppoCustomerImeiStocks(@RequestParam(value = "dateStart")String dateStart, @RequestParam(value = "dateEnd") String dateEnd, @RequestParam(value = "companyId") String companyId);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerSales")
    String  findOppoCustomerSales(@RequestParam(value = "dateStart")String dateStart, @RequestParam(value = "dateEnd") String dateEnd, @RequestParam(value = "companyId") String companyId);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerSaleImes")
    String  findOppoCustomerSaleImes(@RequestParam(value = "dateStart")String dateStart, @RequestParam(value = "dateEnd") String dateEnd, @RequestParam(value = "companyId") String companyId);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerSaleCounts")
    String  findOppoCustomerSaleCounts(@RequestParam(value = "dateStart")String dateStart, @RequestParam(value = "dateEnd") String dateEnd, @RequestParam(value = "companyId") String companyId);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerAfterSaleImeis")
    String  findOppoCustomerAfterSaleImes(@RequestParam(value = "dateStart")String dateStart, @RequestParam(value = "dateEnd") String dateEnd, @RequestParam(value = "companyId") String companyId);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerDemoPhones")
    String  findOppoCustomerDemoPhones(@RequestParam(value = "dateStart")String dateStart, @RequestParam(value = "dateEnd") String dateEnd, @RequestParam(value = "companyId") String companyId);

}
