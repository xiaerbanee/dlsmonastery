package net.myspring.tool.modules.oppo.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by guolm on 2017/6/11.
 */

@FeignClient("ws-future")
public interface OppoClient {

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerAllots")
    String  findOppoCustomerAllots(String dateStart,String dateEnd,String companyId);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerStocks")
    String  findOppoCustomerStocks(String dateStart,String dateEnd,String companyId);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerImeiStocks")
    String  findOppoCustomerImeiStocks(String dateStart,String dateEnd,String companyId);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerSales")
    String  findOppoCustomerSales(String dateStart,String dateEnd,String companyId);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerSaleImes")
    String  findOppoCustomerSaleImes(String dateStart,String dateEnd,String companyId);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerSaleCounts")
    String  findOppoCustomerSaleCounts(String dateStart,String dateEnd,String companyId);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerAfterSaleImeis")
    String  findOppoCustomerAfterSaleImes(String dateStart,String dateEnd,String companyId);

    @RequestMapping(method = RequestMethod.GET, value = "third/factory/oppo/findOppoCustomerDemoPhones")
    String  findOppoCustomerDemoPhones(String dateStart,String dateEnd,String companyId);

}
