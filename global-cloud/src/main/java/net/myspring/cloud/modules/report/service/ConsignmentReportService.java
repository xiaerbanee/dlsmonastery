package net.myspring.cloud.modules.report.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.CustomerGroupEnum;
import net.myspring.cloud.modules.input.domain.BdCustomer;
import net.myspring.cloud.modules.input.domain.BdMaterial;
import net.myspring.cloud.modules.input.mapper.BdCustomerMapper;
import net.myspring.cloud.modules.input.mapper.BdMaterialMapper;
import net.myspring.cloud.modules.report.dto.ConsignmentForUnitDto;
import net.myspring.cloud.modules.report.dto.ConsignmentForShowDto;
import net.myspring.cloud.modules.report.mapper.ConsignmentReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@KingdeeDataSource
public class ConsignmentReportService {

    @Autowired
    private ConsignmentReportMapper consignmentReportMapper;
    @Autowired
    private BdCustomerMapper bdCustomerMapper;
    @Autowired
    private BdMaterialMapper bdMaterialMapper;

    public List<ConsignmentForShowDto> findConsignmentReport(LocalDate dateStart, LocalDate dateEnd) {
        List<ConsignmentForShowDto> modelList = Lists.newArrayList();
        //期间
        List<ConsignmentForUnitDto> transferInList = consignmentReportMapper.findTransferInByPeriod(dateStart,dateEnd);
        List<ConsignmentForUnitDto> transferOutList = consignmentReportMapper.findTransferOutByPeriod(dateStart,dateEnd);
        List<ConsignmentForUnitDto> outStockList = consignmentReportMapper.findOutStockByPeriod(dateStart,dateEnd);
        List<ConsignmentForUnitDto> returnStockList = consignmentReportMapper.findReturnStockByPeriod(dateStart,dateEnd);
        //期初
        List<ConsignmentForUnitDto> initialItemList = getInitialItem(dateStart);
        //合成一个List,形成客户+物料+单价三个维度
        Map<String,ConsignmentForUnitDto> itemMap = Maps.newHashMap();
        for(ConsignmentForUnitDto initItem: initialItemList){
            String key = initItem.getCustomerId()+initItem.getMaterialId()+initItem.getPrice();
            if(!itemMap.containsKey(key)){
                itemMap.put(key,initItem);
            }
        }
        for(ConsignmentForUnitDto transferIn: transferInList){
            String key = transferIn.getCustomerId()+transferIn.getMaterialId()+transferIn.getPrice();
            if(!itemMap.containsKey(key)){
                itemMap.put(key,transferIn);
            }
        }
        for(ConsignmentForUnitDto transferOut: transferOutList){
            String key = transferOut.getCustomerId()+transferOut.getMaterialId()+transferOut.getPrice();
            if(!itemMap.containsKey(key)){
                itemMap.put(key,transferOut);
            }
        }
        for(ConsignmentForUnitDto outStock: outStockList){
            String key = outStock.getCustomerId()+outStock.getMaterialId()+outStock.getPrice();
            if(!itemMap.containsKey(key)){
                itemMap.put(key,outStock);
            }
        }
        for(ConsignmentForUnitDto returnStock: returnStockList){
            String key = returnStock.getCustomerId()+returnStock.getMaterialId()+returnStock.getPrice();
            if(!itemMap.containsKey(key)){
                itemMap.put(key,returnStock);
            }
        }
        //计算
        for(ConsignmentForUnitDto map : itemMap.values()){
            ConsignmentForShowDto model = new ConsignmentForShowDto();
            model.setCustomerCode(map.getCustomerId());
            model.setCustomerName(map.getCustomerName());
            model.setGoodsCode(map.getMaterialId());
            model.setGoodsName(map.getMaterialName());
            BigDecimal quantityTransferIn = BigDecimal.ZERO;
            BigDecimal amountTransferIn = BigDecimal.ZERO;
            BigDecimal quantityTransferOut = BigDecimal.ZERO;
            BigDecimal amountTransferOut = BigDecimal.ZERO;
            BigDecimal quantityOutStock = BigDecimal.ZERO;
            BigDecimal amountOutStock = BigDecimal.ZERO;
            BigDecimal quantityReturnStock = BigDecimal.ZERO;
            BigDecimal amountReturnStock = BigDecimal.ZERO;
            String initKey = map.getCustomerId()+map.getMaterialId()+map.getPrice();
            boolean flagInit = true;
            for(ConsignmentForUnitDto initialItem : initialItemList){
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
            for(ConsignmentForUnitDto transferIn : transferInList){
                String transferInKey = transferIn.getCustomerId()+transferIn.getMaterialId()+transferIn.getPrice();
                if(initKey.equals(transferInKey)){
                    flagTransfer = false;
                    quantityTransferIn = quantityTransferIn.add(transferIn.getQuantity());
                    amountTransferIn = amountTransferIn.add(transferIn.getAmount());
                    model.setConsignmentSendPrice(transferIn.getPrice());
                }
            }
            for(ConsignmentForUnitDto transferOut : transferOutList){
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
            for(ConsignmentForUnitDto outStock : outStockList){
                String outStockKey = outStock.getCustomerId()+outStock.getMaterialId()+outStock.getPrice();
                if(initKey.equals(outStockKey)){
                    flagStock = false;
                    model.setConsignmentSettlementPrice(outStock.getPrice());
                    amountOutStock = amountOutStock.add(outStock.getAmount());
                    quantityOutStock = quantityOutStock.add(outStock.getQuantity());
                }
            }
            for(ConsignmentForUnitDto returnStock : returnStockList){
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
        //小计+排序+根据客户id设置number
        List<ConsignmentForShowDto> modelSortList = Lists.newLinkedList();
        List<BdCustomer> customers = bdCustomerMapper.findByPrimaryGroup(CustomerGroupEnum.成品寄售客户.getPrimaryGroup());
        for(BdCustomer customer : customers){
            BigDecimal amountInit = BigDecimal.ZERO;
            BigDecimal quantityInit = BigDecimal.ZERO;
            BigDecimal amountSend = BigDecimal.ZERO;
            BigDecimal quantitySend = BigDecimal.ZERO;
            BigDecimal amountSettlement = BigDecimal.ZERO;
            BigDecimal quantitySettlement = BigDecimal.ZERO;
            BigDecimal amountNotSettled = BigDecimal.ZERO;
            BigDecimal quantityNotSettled = BigDecimal.ZERO;
            for(ConsignmentForShowDto model : modelList){
                if(customer.getfCustId().equals(model.getCustomerCode())){
                    model.setCustomerCode(customer.getfNumber());
                    amountInit = amountInit.add(model.getConsignmentInitialAmount());
                    quantityInit = quantityInit.add(model.getConsignmentInitialQuantity());
                    amountSend = amountSend.add(model.getConsignmentSendAmount());
                    quantitySend = quantitySend.add(model.getConsignmentSendQuantity());
                    amountSettlement = amountSettlement.add(model.getConsignmentSettlementAmount());
                    quantitySettlement = quantitySettlement.add(model.getConsignmentSettlementQuantity());
                    amountNotSettled = amountNotSettled.add(model.getConsignmentNotSettledAmount());
                    quantityNotSettled = quantityNotSettled.add(model.getConsignmentNotSettledQuantity());
                    modelSortList.add(model);
                }
            }
            ConsignmentForShowDto model = new ConsignmentForShowDto();
            model.setCustomerCode(customer.getfNumber());
            model.setCustomerName(customer.getfName());
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
        List<BdMaterial> materials = bdMaterialMapper.findAll();
        for(ConsignmentForShowDto item : modelSortList){
            for(BdMaterial material : materials ){
                if (material.getfMasterId().equals(item.getGoodsCode())){
                    item.setGoodsCode(material.getfNumber());
                }
            }
        }
        return modelSortList;
    }

    private List<ConsignmentForUnitDto> getInitialItem(LocalDate dateStart) {
        List<ConsignmentForUnitDto> itemList = Lists.newArrayList();
        List<ConsignmentForUnitDto> initItemList = consignmentReportMapper.findInitialization();
        List<ConsignmentForUnitDto> transferInByEndDateList = consignmentReportMapper.findTransferInByEndDate(dateStart);
        List<ConsignmentForUnitDto> transferOutByEndDateList = consignmentReportMapper.findTransferOutByEndDate(dateStart);
        List<ConsignmentForUnitDto> outStockByEndDateList = consignmentReportMapper.findOutStockByEndDate(dateStart);
        List<ConsignmentForUnitDto> returnStockByEndDateList = consignmentReportMapper.findReturnStockByEndDate(dateStart);
        Map<String, ConsignmentForUnitDto> itemMap = Maps.newHashMap();
        for (ConsignmentForUnitDto initItem : initItemList) {
            String key = initItem.getCustomerId() + initItem.getMaterialId() + initItem.getPrice();
            if (!itemMap.containsKey(key)) {
                itemMap.put(key, initItem);
            }
        }
        for (ConsignmentForUnitDto transferIn : transferInByEndDateList) {
            String key = transferIn.getCustomerId() + transferIn.getMaterialId() + transferIn.getPrice();
            if (!itemMap.containsKey(key)) {
                itemMap.put(key, transferIn);
            }
        }
        for (ConsignmentForUnitDto transferOut : transferOutByEndDateList) {
            String key = transferOut.getCustomerId() + transferOut.getMaterialId() + transferOut.getPrice();
            if (!itemMap.containsKey(key)) {
                itemMap.put(key, transferOut);
            }
        }
        for (ConsignmentForUnitDto outStock : outStockByEndDateList) {
            String key = outStock.getCustomerId() + outStock.getMaterialId() + outStock.getPrice();
            if (!itemMap.containsKey(key)) {
                itemMap.put(key, outStock);
            }
        }
        for (ConsignmentForUnitDto returnStock : returnStockByEndDateList) {
            String key = returnStock.getCustomerId() + returnStock.getMaterialId() + returnStock.getPrice();
            if (!itemMap.containsKey(key)) {
                itemMap.put(key, returnStock);
            }
        }
        for (ConsignmentForUnitDto map : itemMap.values()) {
            BigDecimal  quantity = BigDecimal.ZERO;
            BigDecimal  amount = BigDecimal.ZERO;
            String initKey = map.getCustomerId() + map.getMaterialId() + map.getPrice();
            for(ConsignmentForUnitDto initItem : initItemList){
                String initItemKey = initItem.getCustomerId()+initItem.getMaterialId()+initItem.getPrice();
                if (initKey.equals(initItemKey)) {
                    quantity = quantity.add(initItem.getQuantity());
                    amount = amount.add(initItem.getAmount());
                }
            }
            for (ConsignmentForUnitDto transferIn : transferInByEndDateList) {
                String transferInKey = transferIn.getCustomerId() + transferIn.getMaterialId() + transferIn.getPrice();
                if (initKey.equals(transferInKey)) {
                    quantity = quantity.add(transferIn.getQuantity());
                    amount = amount.add(transferIn.getAmount());
                }
            }
            for (ConsignmentForUnitDto transferOut : transferOutByEndDateList) {
                String transferOutKey = transferOut.getCustomerId() + transferOut.getMaterialId() + transferOut.getPrice();
                if (initKey.equals(transferOutKey)) {
                    quantity = quantity.subtract(transferOut.getQuantity());
                    amount = amount.subtract(transferOut.getAmount());
                }
            }
            for (ConsignmentForUnitDto outStock : outStockByEndDateList) {
                String outStockKey = outStock.getCustomerId() + outStock.getMaterialId() + outStock.getPrice();
                if (initKey.equals(outStockKey)) {
                    quantity = quantity.subtract(outStock.getQuantity());
                    amount = amount.subtract(outStock.getAmount());
                }
            }
            for (ConsignmentForUnitDto returnStock : returnStockByEndDateList) {
                String returnStockKey = returnStock.getCustomerId() + returnStock.getMaterialId() + returnStock.getPrice();
                if (initKey.equals(returnStockKey)) {
                    amount = amount.add(returnStock.getAmount());
                    quantity = quantity.add(returnStock.getQuantity());
                }
            }
            map.setAmount(amount);
            map.setQuantity(quantity);
            itemList.add(map);
        }
        return itemList;
    }
}
