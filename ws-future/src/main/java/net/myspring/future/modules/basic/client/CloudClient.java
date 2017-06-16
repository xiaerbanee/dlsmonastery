package net.myspring.future.modules.basic.client;

import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by liuj on 2017-03-08.
 */
@FeignClient("global-cloud")
public interface CloudClient {

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdStock/findAll")
    String getSynStockData();

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdCustomer/findAll")
    String getSynCustomerData();

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdMaterial/findAll")
    String getSynProductData(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "maxOutDate") String maxOutDate);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/basicData/getSynBankData")
    String getSynBankData(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "maxOutDate") String maxOutDate);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/basicData/findReceivableBillNo")
    String findReceivableBillNo(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "outStockBillNo") String outStockBillNo);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/basicData/findHrEmpinfo")
    String findHrEmpinfo(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "name") String name);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/basicData/findAccountFNumber")
    String findAccountFNumber(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "bankName") String bankName);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/basicData/findAssistantCode")
    String findAssistantCode(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "lbName") String lbName, @RequestParam(value = "name") String name);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/basicData/findDepartByCustomer")
    String findDepartByCustomer(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "outId") String outId);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdDepartment/findAll")
    List<BdDepartment> findAll();

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/basicData/findBdInventorys")
    String findBdInventorys(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "stockIds") List<String> stockIds);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/basicData/findBdInventoryByDepotAndProduct")
    String findBdInventoryByDepotAndProduct(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "depotId") String depotId, @RequestParam(value = "productId") String productId);


    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/basicData/receivableReportForSummaryList")
    String receivableReportForSummaryList(@RequestParam(value = "dateRange") String dateRange, @RequestParam(value = "companyName") String companyName);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/basicData/receivableReportForDetailList")
    String receivableReportForDetailList(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "customerId") String customerId, @RequestParam(value = "dateRange") String dateRange);

    @RequestMapping(method = RequestMethod.POST, value = "/kingdee/basicData/shouldGet")
    String findShouldGet(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "customerIds") List<String> customerIds, @RequestParam(value = "dateEndStr") String dateEndStr);
}
