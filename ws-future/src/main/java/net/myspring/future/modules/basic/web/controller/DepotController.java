package net.myspring.future.modules.basic.web.controller;

import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDetailDto;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveDetailQuery;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.dto.CustomerDto;
import net.myspring.future.modules.basic.dto.DepotAccountDto;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.basic.service.DepotShopService;
import net.myspring.future.modules.basic.web.query.DepotAccountQuery;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by liuj on 2017/5/15.
 */
@RestController
@RequestMapping(value = "basic/depot")
public class DepotController {

    @Autowired
    private DepotService depotService;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private CloudClient cloudClient;
    @Autowired
    private DepotShopService depotShopService;

    //直营门店查询(POP申请开单类型为配件赠品用这个)
    @RequestMapping(value = "directShop")
    public List<DepotDto>  directShop(DepotQuery depotQuery) {
        depotQuery.setClientIsNull(false);
        List<DepotDto> result = depotService.findShopList(depotQuery);
        return result;
    }

    //代理门店查询
    @RequestMapping(value = "delegateShop")
    public List<DepotDto>  delegateShop(DepotQuery depotQuery) {
        depotQuery.setClientIsNull(true);
        return depotService.findShopList(depotQuery);
    }

    //门店查询(物料订单的代理门店也用这个)
    @RequestMapping(value = "shop")
    public List<DepotDto>  shop(DepotQuery depotQuery) {
        return depotService.findShopList(depotQuery);
    }

    //物料订单门店
    @RequestMapping(value = "adShop")
    public List<DepotDto>  adShop(DepotQuery depotQuery) {
        depotQuery.setAdShop(true);
        return depotService.findShopList(depotQuery);
    }

    //POP门店
    @RequestMapping(value = "popShop")
    public List<DepotDto>  popShop(DepotQuery depotQuery) {
        depotQuery.setPopShop(true);
        return depotService.findShopList(depotQuery);
    }

    //直营仓库查询
    @RequestMapping(value = "directStore")
    public List<DepotDto>  directStore(DepotQuery depotQuery) {
        depotQuery.setOutIdIsNull(false);
        return depotService.findStoreList(depotQuery);
    }

    //代理仓库查询
    @RequestMapping(value = "delegateStore")
    public List<DepotDto>  delegateStore(DepotQuery depotQuery) {
        depotQuery.setOutIdIsNull(true);
        return depotService.findStoreList(depotQuery);
    }

    //仓库查询
    @RequestMapping(value = "store")
    public List<DepotDto>  store(DepotQuery depotQuery) {
        return depotService.findStoreList(depotQuery);
    }

    @RequestMapping(value = "findByIds")
    public List<DepotDto> findByListIds(@RequestParam("idStr") List<String> ids) {
        List<DepotDto> depotDtoList =depotService.findByIds(ids);
        return depotDtoList;
    }

    @RequestMapping(value = "getDepotAccountQuery")
    public DepotAccountQuery getDepotAccountQuery(DepotAccountQuery depotAccountQuery) {
        LocalDate now = LocalDate.now();
        LocalDate dutyDateStart = now.minusDays(30);
        depotAccountQuery.setDutyDateRange(LocalDateUtils.format(dutyDateStart) + " - "+LocalDateUtils.format(now));

        return depotAccountQuery;
    }

    @RequestMapping(value = "findDepotAccountList")
    public Page<DepotAccountDto> findDepotAccountList(Pageable pageable, DepotAccountQuery depotAccountQuery) {
        return depotService.findDepotAccountList(pageable, depotAccountQuery);
    }

    @RequestMapping(value = "findDepotAccountDetailList")
    public List<CustomerReceiveDetailDto> findDepotAccountDetailList(String clientOutId, String dateRange) {
        CustomerReceiveDetailQuery customerReceiveDetailQuery = new CustomerReceiveDetailQuery();
        customerReceiveDetailQuery.setCustomerIdList(Collections.singletonList(clientOutId));
        if(StringUtils.isNotBlank(dateRange)){
            String[] tempParamValues = dateRange.split(" - ");
            customerReceiveDetailQuery.setDateStart(LocalDate.parse(tempParamValues[0]));
            customerReceiveDetailQuery.setDateEnd(LocalDate.parse(tempParamValues[1]));
        }

        return cloudClient.getCustomerReceiveDetailList(customerReceiveDetailQuery);
    }

    @RequestMapping(value="depotAccountExportDetail")
    public String depotAccountExportDetail(DepotAccountQuery depotAccountQuery) {
        return depotService.depotAccountExportDetail(depotAccountQuery);
    }

    @RequestMapping(value="depotAccountExportConfirmation")
    public String depotAccountExportConfirmation(DepotAccountQuery depotAccountQuery) {
        return depotService.depotAccountExportConfirmation(depotAccountQuery);
    }

    @RequestMapping(value="depotAccountExportAllDepots")
    public String depotAccountExportAllDepots(DepotAccountQuery depotAccountQuery) {
        return depotService.depotAccountExportAllDepots(depotAccountQuery);
    }

    @RequestMapping(value = "findOne")
    public DepotDto findOne(String id) {
        return depotService.findOne(id);
    }

    @RequestMapping(value = "scheduleSynArea")
    public RestResponse scheduleSynArea(String accountId) {
        depotService.scheduleSynArea();
        return new RestResponse("同步成功",null);
    }

    @RequestMapping(value = "synArea")
    public RestResponse synArea(DepotQuery depotQuery) {
        if(StringUtils.isBlank(depotQuery.getName())&&StringUtils.isBlank(depotQuery.getAreaId())){
            return new RestResponse("请设置过滤条件",null);
        }
        depotService.synArea(depotQuery);
        return new RestResponse("同步成功",null);
    }


    @RequestMapping(value = "getDefaultDepartMent")
    public String getDefaultDepartMent(String depotId) {
        if(StringUtils.isBlank(depotId)){
            return null;
        }
        BdDepartment bdDepartment=depotService.getDefaultDepartment(depotId);
        return bdDepartment.getFNumber();
    }

    @RequestMapping(value = "getRecentMonthSaleAmount")
    public Map<String, Long>  getRecentMonthSaleAmount(String depotId, int monthQty) {
        if(monthQty <= 0){
            throw new ServiceException("monthQty 必须大于0");
        }
        if(StringUtils.isBlank(depotId)){
            throw new ServiceException("depotId不能为空");
        }
        return depotService.getRecentMonthSaleAmount(depotId, monthQty);
    }

    @RequestMapping(value = "searchDepartment")
    public String searchDepartment(String depotName){
        if(StringUtils.isNotBlank(depotName)){
            Depot depot=depotService.findByName(depotName);
            BdDepartment bdDepartment=depotService.getDefaultDepartment(depot.getId());
            return bdDepartment.getFFullName();
        }
        return null;
    }

    @RequestMapping(value = "getCloudQtyMap")
    public Map<String, Integer> getCloudQtyMap(String storeId) {
        if(StringUtils.isBlank(storeId)){
            return new HashMap<>();
        }
        return depotService.getCloudQtyMap(storeId);
    }

    @RequestMapping(value="findOppoCustomers")
    public List<CustomerDto> findOppoCustomers(){
        List<CustomerDto> oppoCustomers=depotService.findOppoCustomers();
        return oppoCustomers;
    }

}
