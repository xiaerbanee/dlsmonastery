package net.myspring.future.modules.basic.client;

import net.myspring.cloud.modules.input.dto.CnJournalForBankDto;
import net.myspring.cloud.modules.kingdee.domain.*;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDetailDto;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveQuery;
import net.myspring.common.response.RestResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by liuj on 2017-03-08.
 */
@FeignClient(value = "global-cloud")
public interface CloudClient {

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdStock/findAll")
    List<BdStock> getSynStockData();

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdCustomer/findAll")
    List<BdCustomer> getSynCustomerData();

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdMaterial/findAll")
    List<BdMaterial> getSynProductData();

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/cnBankAcnt/findAll")
    List<CnBankAcnt> getSynBankData();

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/arReceivable/findTopOneBySourceBillNo")
    ArReceivable findReceivableBillNo( @RequestParam(value = "sourceBillNo") String outStockBillNo);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/hrEmpInfo/findByName")
    HrEmpInfo findHrEmpinfo(@RequestParam(value = "name") String name);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdAccount/findByName")
    BdAccount findAccountFNumber(@RequestParam(value = "name") String name);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/basicData/findAssistantCode")
    String findAssistantCode(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "lbName") String lbName, @RequestParam(value = "name") String name);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdDepartment/findCustId")
    BdDepartment findDepartByCustomer( @RequestParam(value = "custId") String outId);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdDepartment/findAll")
    List<BdDepartment> findAll();

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/stkInventory/findByStockIds")
    List<StkInventory> findBdInventorys(@RequestParam(value = "stockIds") List<String> stockIds);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/stkInventory/findByStockIdAndMaterialId")
    StkInventory findBdInventoryByDepotAndProduct(@RequestParam(value = "stockId") String depotId, @RequestParam(value = "materialId") String productId);


    @RequestMapping(method = RequestMethod.GET, value = "/report/customerReceive/detail")
    List<CustomerReceiveDetailDto> getCustomerReceiveDetailList(@RequestParam(value = "dateRange")String dateRange,@RequestParam(value = "customerId")String customerId);

    @RequestMapping(method = RequestMethod.POST, value = "/report/customerReceive/list")
    List<CustomerReceiveDto> getCustomerReceiveList(@RequestBody CustomerReceiveQuery customerReceiveQuery);

    @RequestMapping(method = RequestMethod.POST, value = "/input/cnJournalForBank/saveForDto")
    RestResponse synForJournalForBank(CnJournalForBankDto cnJournalForBankDto);

}
