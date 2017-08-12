package net.myspring.cloud.modules.report.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import net.myspring.cloud.modules.kingdee.repository.BdCustomerRepository;
import net.myspring.cloud.modules.kingdee.repository.BdMaterialRepository;
import net.myspring.cloud.modules.report.dto.ConsignmentDto;
import net.myspring.cloud.modules.report.dto.CustomerInventoryDto;
import net.myspring.cloud.modules.report.repository.ConsignmentWZRepository;
import net.myspring.cloud.modules.report.web.query.ConsignmentWZQuery;
import net.myspring.util.excel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 温州-委托代销报表
 */
@Service
@KingdeeDataSource
public class ConsignmentWZService {
    @Autowired
    private ConsignmentWZRepository consignmentWZRepository;
    @Autowired
    private BdCustomerRepository bdCustomerRepository;
    @Autowired
    private BdMaterialRepository bdMaterialRepository;

    public List<ConsignmentDto> findConsignmentReport(ConsignmentWZQuery consignmentWZQuery) {
        LocalDate dateStart = consignmentWZQuery.getDateStart();
        LocalDate dateEnd = consignmentWZQuery.getDateEnd();
        List<ConsignmentDto> modelList = Lists.newArrayList();
        //期间
        List<CustomerInventoryDto> transferInList = consignmentWZRepository.findTransferInByPeriod(dateStart,dateEnd);
        List<CustomerInventoryDto> transferOutList = consignmentWZRepository.findTransferOutByPeriod(dateStart,dateEnd);
        List<CustomerInventoryDto> outStockList = consignmentWZRepository.findOutStockByPeriod(dateStart,dateEnd);
        List<CustomerInventoryDto> returnStockList = consignmentWZRepository.findReturnStockByPeriod(dateStart,dateEnd);
        //期初
        List<CustomerInventoryDto> initialItemList = consignmentWZRepository.findByEndDate(dateStart);
        //合成一个List,形成客户+物料+单价三个维度
        Map<String,CustomerInventoryDto> itemMap = Maps.newHashMap();
        for(CustomerInventoryDto initItem: initialItemList){
            String key = initItem.getCustomerId()+initItem.getMaterialId()+initItem.getPrice();
            if(!itemMap.containsKey(key)){
                itemMap.put(key,initItem);
            }
        }
        for(CustomerInventoryDto transferIn: transferInList){
            String key = transferIn.getCustomerId()+transferIn.getMaterialId()+transferIn.getPrice();
            if(!itemMap.containsKey(key)){
                itemMap.put(key,transferIn);
            }
        }
        for(CustomerInventoryDto transferOut: transferOutList){
            String key = transferOut.getCustomerId()+transferOut.getMaterialId()+transferOut.getPrice();
            if(!itemMap.containsKey(key)){
                itemMap.put(key,transferOut);
            }
        }
        for(CustomerInventoryDto outStock: outStockList){
            String key = outStock.getCustomerId()+outStock.getMaterialId()+outStock.getPrice();
            if(!itemMap.containsKey(key)){
                itemMap.put(key,outStock);
            }
        }
        for(CustomerInventoryDto returnStock: returnStockList){
            String key = returnStock.getCustomerId()+returnStock.getMaterialId()+returnStock.getPrice();
            if(!itemMap.containsKey(key)){
                itemMap.put(key,returnStock);
            }
        }
        //计算
        for(CustomerInventoryDto map : itemMap.values()){
            ConsignmentDto model = new ConsignmentDto();
            model.setCustomerCode(map.getCustomerId());
            model.setCustomerName(map.getCustomerName());
            model.setGoodsCode(map.getMaterialId());
            model.setGoodsName(map.getMaterialName());
            BigDecimal  quantityTransferIn = BigDecimal.ZERO;
            BigDecimal  amountTransferIn = BigDecimal.ZERO;
            BigDecimal  quantityTransferOut = BigDecimal.ZERO;
            BigDecimal  amountTransferOut = BigDecimal.ZERO;
            BigDecimal  quantityOutStock = BigDecimal.ZERO;
            BigDecimal  amountOutStock = BigDecimal.ZERO;
            BigDecimal  quantityReturnStock = BigDecimal.ZERO;
            BigDecimal  amountReturnStock = BigDecimal.ZERO;
            String initKey = map.getCustomerId()+map.getMaterialId()+map.getPrice();
            boolean flagInit = true;
            for(CustomerInventoryDto initialItem : initialItemList){
                String initialKey = initialItem.getCustomerId()+initialItem.getMaterialId()+initialItem.getPrice();
                if(initKey.equals(initialKey)){
                    flagInit = false;
                    model.setConsignmentInitialAmount(initialItem.getAmount());
                    if(initialItem.getQuantity().intValue() != 0){
                        model.setConsignmentInitialPrice(initialItem.getPrice());
                    }else{
                        model.setConsignmentInitialPrice(BigDecimal.ZERO);
                    }
                    model.setConsignmentInitialQuantity(initialItem.getQuantity());
                }
            }
            if(flagInit){
                model.setConsignmentInitialAmount(BigDecimal.ZERO);
                model.setConsignmentInitialPrice(BigDecimal.ZERO);
                model.setConsignmentInitialQuantity(BigDecimal.ZERO);
            }
            boolean flagTransfer = true;
            for(CustomerInventoryDto transferIn : transferInList){
                String transferInKey = transferIn.getCustomerId()+transferIn.getMaterialId()+transferIn.getPrice();
                if(initKey.equals(transferInKey)){
                    flagTransfer = false;
                    quantityTransferIn = quantityTransferIn.add(transferIn.getQuantity());
                    amountTransferIn = amountTransferIn.add(transferIn.getAmount());
                    model.setConsignmentSendPrice(transferIn.getPrice());
                }
            }
            for(CustomerInventoryDto transferOut : transferOutList){
                String transferOutKey = transferOut.getCustomerId()+transferOut.getMaterialId()+transferOut.getPrice();
                if(initKey.equals(transferOutKey)){
                    flagTransfer = false;
                    model.setConsignmentSendPrice(transferOut.getPrice());
                    quantityTransferOut = quantityTransferOut.add(transferOut.getQuantity());
                    amountTransferOut = amountTransferOut.add(transferOut.getAmount());
                }
            }
            if(flagTransfer){
                model.setConsignmentSendPrice(BigDecimal.ZERO);
            }

            model.setConsignmentSendAmount(amountTransferIn.subtract(amountTransferOut));
            model.setConsignmentSendQuantity(quantityTransferIn.subtract(quantityTransferOut));

            boolean flagStock = true;
            for(CustomerInventoryDto outStock : outStockList){
                String outStockKey = outStock.getCustomerId()+outStock.getMaterialId()+outStock.getPrice();
                if(initKey.equals(outStockKey)){
                    flagStock = false;
                    model.setConsignmentSettlementPrice(outStock.getPrice());
                    amountOutStock = amountOutStock.add(outStock.getAmount());
                    quantityOutStock = quantityOutStock.add(outStock.getQuantity());
                }
            }
            for(CustomerInventoryDto returnStock : returnStockList){
                String returnStockKey = returnStock.getCustomerId()+returnStock.getMaterialId()+returnStock.getPrice();
                if(initKey.equals(returnStockKey)){
                    flagStock = false;
                    model.setConsignmentSettlementPrice(returnStock.getPrice());
                    quantityReturnStock = quantityReturnStock.add(returnStock.getQuantity());
                    amountReturnStock = amountReturnStock.add(returnStock.getAmount());
                }
            }
            if(flagStock){
                model.setConsignmentSettlementPrice(BigDecimal.ZERO);
            }
            model.setConsignmentSettlementAmount(amountOutStock.subtract(amountReturnStock));
            model.setConsignmentSettlementQuantity(quantityOutStock.subtract(quantityReturnStock));

            model.setConsignmentNotSettledAmount(model.getConsignmentInitialAmount().add(model.getConsignmentSendAmount()).subtract(model.getConsignmentSettlementAmount()));
            model.setConsignmentNotSettledQuantity(model.getConsignmentInitialQuantity().add(model.getConsignmentSendQuantity().subtract(model.getConsignmentSettlementQuantity())));
            if(model.getConsignmentNotSettledQuantity().intValue() == 0){
                model.setConsignmentNotSettledPrice(BigDecimal.ZERO);
            }else{
                model.setConsignmentNotSettledPrice(map.getPrice());
            }
            modelList.add(model);
        }
        //小计+排序+客户id换成number
        List<ConsignmentDto> modelSortList = Lists.newLinkedList();
        List<BdCustomer> customers = bdCustomerRepository.findIncludeForbidByPrimaryGroup("636694");
        List<String> materialIdList = Lists.newArrayList();
        for(BdCustomer customer : customers){
            BigDecimal amountInit = BigDecimal.ZERO;
            BigDecimal quantityInit = BigDecimal.ZERO;
            BigDecimal amountSend = BigDecimal.ZERO;
            BigDecimal quantitySend = BigDecimal.ZERO;
            BigDecimal amountSettlement = BigDecimal.ZERO;
            BigDecimal quantitySettlement = BigDecimal.ZERO;
            BigDecimal amountNotSettled = BigDecimal.ZERO;
            BigDecimal quantityNotSettled = BigDecimal.ZERO;
            for(ConsignmentDto model : modelList){
                if(customer.getFCustId().equals(model.getCustomerCode())){
                    model.setCustomerCode(customer.getFNumber());
                    amountInit = amountInit.add(model.getConsignmentInitialAmount());
                    quantityInit = quantityInit.add(model.getConsignmentInitialQuantity());
                    amountSend = amountSend.add(model.getConsignmentSendAmount());
                    quantitySend = quantitySend.add(model.getConsignmentSendQuantity());
                    amountSettlement = amountSettlement.add(model.getConsignmentSettlementAmount());
                    quantitySettlement = quantitySettlement.add(model.getConsignmentSettlementQuantity());
                    amountNotSettled = amountNotSettled.add(model.getConsignmentNotSettledAmount());
                    quantityNotSettled = quantityNotSettled.add(model.getConsignmentNotSettledQuantity());
                    modelSortList.add(model);
                    materialIdList.add(model.getGoodsCode());
                }
            }
            ConsignmentDto model = new ConsignmentDto();
            model.setCustomerCode(customer.getFNumber());
            model.setCustomerName(customer.getFName());
            model.setGoodsCode("小计");
            model.setGoodsName("");
            model.setConsignmentInitialPrice(null);
            model.setConsignmentInitialAmount(amountInit);
            model.setConsignmentInitialQuantity(quantityInit);
            model.setConsignmentSendPrice(null);
            model.setConsignmentSendAmount(amountSend);
            model.setConsignmentSendQuantity(quantitySend);
            model.setConsignmentSettlementPrice(null);
            model.setConsignmentSettlementQuantity(quantitySettlement);
            model.setConsignmentSettlementAmount(amountSettlement);
            model.setConsignmentNotSettledPrice(null);
            model.setConsignmentNotSettledAmount(amountNotSettled);
            model.setConsignmentNotSettledQuantity(quantityNotSettled);
            modelSortList.add(model);
        }
        //物料id换成number
        List<BdMaterial> materials = bdMaterialRepository.findByMasterIdList(materialIdList);
        for(ConsignmentDto item : modelSortList){
            for(BdMaterial material : materials ){
                if (material.getFMasterId().equals(item.getGoodsCode())){
                    item.setGoodsCode(material.getFNumber());
                }
            }
        }
        return modelSortList;
    }

    public SimpleExcelBook export(LocalDate dateStart, LocalDate dateEnd ){
        ConsignmentWZQuery consignmentWZQuery = new ConsignmentWZQuery();
        consignmentWZQuery.setDateStart(dateStart);
        consignmentWZQuery.setDateEnd(dateEnd);
        List<ConsignmentDto> consignmentDtoList = findConsignmentReport(consignmentWZQuery);
        Workbook workbook = new SXSSFWorkbook(10000);
        List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "customerCode", "客户代码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "customerName", "客户名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "goodsCode", "商品代码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "goodsName", "商品名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "consignmentInitialQuantity", "寄售期初数量"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "consignmentInitialPrice", "寄售期初单价"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "consignmentInitialAmount", "寄售期初金额"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "consignmentSendQuantity", "寄售发出数量"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "consignmentSendPrice", "寄售发出单价"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "consignmentSendAmount", "寄售发出金额"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "consignmentSettlementQuantity", "寄售结算数量"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "consignmentSettlementPrice", "寄售结算单价"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "consignmentSettlementAmount", "寄售结算金额"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "consignmentNotSettledQuantity", "寄售未结算数量"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "consignmentNotSettledPrice", "寄售未结算单价"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "consignmentNotSettledAmount", "寄售未结算金额"));
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("委托代销",consignmentDtoList,simpleExcelColumnList);
        ExcelUtils.doWrite(workbook,simpleExcelSheet);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"委托代销报表"+"（"+dateStart+dateEnd+"）"+".xlsx",simpleExcelSheet);
        return simpleExcelBook;
    }

}
