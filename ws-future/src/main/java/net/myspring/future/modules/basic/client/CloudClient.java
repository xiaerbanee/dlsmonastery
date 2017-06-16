package net.myspring.future.modules.basic.client;

import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.kingdee.domain.BdStock;
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
    List<BdStock> getSynStockData();

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdCustomer/findAll")
    List<BdCustomer> getSynCustomerData();

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdMaterial/findAll")
    List<BdMaterial> getSynProductData();

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/cnBankAcnt/findAll")
    String getSynBankData();

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/basicData/findReceivableBillNo")
    String findReceivableBillNo(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "outStockBillNo") String outStockBillNo);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/hrEmpInfo/findByName")
    String findHrEmpinfo(@RequestParam(value = "name") String name);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdAccount/findByName")
    String findAccountFNumber(@RequestParam(value = "name") String name);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/basicData/findAssistantCode")
    String findAssistantCode(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "lbName") String lbName, @RequestParam(value = "name") String name);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdDepartment/findCustId")
    BdDepartment findDepartByCustomer( @RequestParam(value = "custId") String custId);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdDepartment/findAll")
    List<BdDepartment> findAll();

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/stkInventory/findByStockIds")
    String findBdInventorys(@RequestParam(value = "stockIds") List<String> stockIds);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/stkInventory/findBdInventoryByDepotAndProduct")
    String findBdInventoryByDepotAndProduct(@RequestParam(value = "depotId") String depotId, @RequestParam(value = "productId") String productId);


    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/basicData/receivableReportForSummaryList")
    String receivableReportForSummaryList(@RequestParam(value = "dateRange") String dateRange, @RequestParam(value = "companyName") String companyName);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/basicData/receivableReportForDetailList")
    String receivableReportForDetailList(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "customerId") String customerId, @RequestParam(value = "dateRange") String dateRange);

    @RequestMapping(method = RequestMethod.POST, value = "/kingdee/basicData/shouldGet")
    String findShouldGet(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "customerIds") List<String> customerIds, @RequestParam(value = "dateEndStr") String dateEndStr);
}
