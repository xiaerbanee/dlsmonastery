package net.myspring.future.modules.basic.client;

import net.myspring.cloud.modules.input.dto.*;
import net.myspring.cloud.modules.kingdee.domain.*;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDetailDto;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveDetailQuery;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveQuery;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by liuj on 2017-03-08.
 */
@FeignClient(value = "global-cloud")
public interface CloudClient {

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdStock/findAll")
    List<BdStock> getAllStock();

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdStock/findByMaxModifyDate")
    List<BdStock> findStockByMaxModifyDate(@RequestParam(value = "maxModifyDate") LocalDateTime maxModifyDate);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdCustomer/findAll")
    List<BdCustomer> getAllCustomer();

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdCustomer/findByMaxModifyDate")
    List<BdCustomer> findCustomerByMaxModifyDate(@RequestParam(value = "maxModifyDate") LocalDateTime maxModifyDate);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdMaterial/findAll")
    List<BdMaterial> getAllProduct();

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdMaterial/findByMaxModifyDate")
    List<BdMaterial> findMaterialByMaxModifyDate(@RequestParam(value = "maxModifyDate") LocalDateTime maxModifyDate);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/cnBankAcnt/findAll")
    List<CnBankAcnt> getAllBank();

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/cnBankAcnt/findByMaxModifyDate")
    List<CnBankAcnt> findBankAcntByMaxModifyDate(@RequestParam(value = "maxModifyDate") LocalDateTime maxModifyDate);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/arReceivable/findTopOneBySourceBillNo")
    ArReceivable findReceivableByBillNo( @RequestParam(value = "sourceBillNo") String outStockBillNo);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/hrEmpInfo/findByName")
    HrEmpInfo findEmpInfoByName(@RequestParam(value = "name") String name);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdAccount/findByName")
    BdAccount findAccountByName(@RequestParam(value = "name") String name);

//    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/basicData/findAssistantCode")
//    String findAssistantCode(@RequestParam(value = "companyName") String companyName, @RequestParam(value = "lbName") String lbName, @RequestParam(value = "name") String name);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdDepartment/findCustId")
    BdDepartment findDepartmentByOutId( @RequestParam(value = "custId") String outId);

    @RequestMapping(method = RequestMethod.GET, value = "/kingdee/bdDepartment/findAll")
    List<BdDepartment> findAllDepartment();
    //其他出库单
    @RequestMapping(method = RequestMethod.POST, value = "/kingdee/stkInventory/findByStockIds")
    List<StkInventory> findInventorysByDepotStoreOutIds(List<String> depotStoreOutIds);
    //其他出库单
    @RequestMapping(method = RequestMethod.POST, value = "/kingdee/stkInventory/findByMaterialIds")
    List<StkInventory> findInventorysByProductOutIds(List<String> productOutIds);
    //应收
    @RequestMapping(method = RequestMethod.POST, value = "/report/customerReceive/detail")
    List<CustomerReceiveDetailDto> getCustomerReceiveDetailList(CustomerReceiveDetailQuery customerReceiveDetailQuery);
    //应收
    @RequestMapping(method = RequestMethod.POST, value = "/report/customerReceive/list")
    List<CustomerReceiveDto> getCustomerReceiveList(CustomerReceiveQuery customerReceiveQuery);
    //银行存款日记账
    @RequestMapping(method = RequestMethod.POST, value = "/input/cnJournalForBank/saveForEmployeePhoneDeposit")
    List<KingdeeSynReturnDto> synJournalBankForEmployeePhoneDeposit(List<CnJournalForBankDto> cnJournalForBankDtoList);
    //银行存款日记账
    @RequestMapping(method = RequestMethod.POST, value = "/input/cnJournalForBank/saveForShopDeposit")
    List<KingdeeSynReturnDto> synJournalBankForShopDeposit(List<CnJournalForBankDto> cnJournalForBankDtoList);
    //标准销售出库单
    @RequestMapping(method = RequestMethod.POST, value = "/input/salOutStock/saveForXSCKD")
    List<KingdeeSynReturnDto> synSalOutStock(List<SalOutStockDto> salOutStockDtoList);
    //直接调拨单
    @RequestMapping(method = RequestMethod.POST, value = "/input/stkTransferDirect/saveForStoreAllot")
    KingdeeSynReturnDto synStkTransferDirect(StkTransferDirectDto stkTransferDirectDto);
    //其他应收单
    @RequestMapping(method = RequestMethod.POST, value = "/input/arOtherRecAble/saveForShopDeposit")
    List<KingdeeSynReturnDto> synOtherRecAble(List<ArOtherRecAbleDto> arOtherRecAbleDtoList);
}
