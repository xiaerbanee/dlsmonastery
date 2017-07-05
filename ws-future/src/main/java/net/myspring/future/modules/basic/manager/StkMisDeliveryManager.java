package net.myspring.future.modules.basic.manager;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.common.enums.StkMisDeliveryTypeEnum;
import net.myspring.cloud.modules.input.dto.StkMisDeliveryDto;
import net.myspring.cloud.modules.input.dto.StkMisDeliveryFEntityDto;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.repository.DepotStoreRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.AfterSaleProductAllot;
import net.myspring.future.modules.crm.web.form.AfterSaleProductAllotForm;
import net.myspring.future.modules.crm.web.form.AfterSaleStoreAllotForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 其他出库单
 * Created by lihx on 2017/7/4.
 */
@Component
public class StkMisDeliveryManager {
    @Autowired
    private CloudClient cloudClient;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DepotStoreRepository depotStoreRepository;

    public KingdeeSynReturnDto synTHForAfterSale(List<AfterSaleProductAllotForm> afterSaleProductAllotFormList){
        StkMisDeliveryDto stkMisDeliveryDto = new StkMisDeliveryDto();
        stkMisDeliveryDto.setExtendId("--");
        stkMisDeliveryDto.setExtendType(ExtendTypeEnum.售后调拨.name());
        stkMisDeliveryDto.setDate(LocalDate.now());
        stkMisDeliveryDto.setDepartmentNumber("0001");//待定
        stkMisDeliveryDto.setMisDeliveryType(StkMisDeliveryTypeEnum.退货.name());
        List<StkMisDeliveryFEntityDto> entityDtoList = Lists.newArrayList();
        List<String> materialIdList = afterSaleProductAllotFormList.stream().map(AfterSaleProductAllotForm::getToProductId).collect(Collectors.toList());
        Map<String, Product> productIdToOutCodeMap = productRepository.findByEnabledIsTrueAndIdIn(materialIdList).stream().collect(Collectors.toMap(Product::getId, Product->Product));
        List<String> storeIdList = afterSaleProductAllotFormList.stream().map(AfterSaleProductAllotForm::getStoreId).collect(Collectors.toList());
        Map<String, DepotStore> depotStoreIdMap = depotStoreRepository.findByIds(storeIdList).stream().collect(Collectors.toMap(DepotStore::getId, DepotStore->DepotStore));
        for (AfterSaleProductAllotForm detail : afterSaleProductAllotFormList){
            if (detail.getToQty() != null && detail.getToQty() >0) {
                StkMisDeliveryFEntityDto entityDto = new StkMisDeliveryFEntityDto();
                Product product = productIdToOutCodeMap.get(detail.getToProductId());
                if (product.getCode() != null) {
                    entityDto.setMaterialNumber(product.getCode());
                } else {
                    throw new ServiceException(product.getName() + " 该货品没有编码，不能开单");
                }
                entityDto.setQty(detail.getToQty());
                DepotStore toDepotStore = depotStoreIdMap.get(detail.getStoreId());
                if (toDepotStore.getOutCode() != null) {
                    entityDto.setStockNumber(toDepotStore.getOutCode());//调入仓库
                } else {
                    throw new ServiceException(toDepotStore.getId() + " 该仓库（ID）没有编码，不能开单");
                }
                entityDto.setFEntryNote( detail.getAfterSaleId() + CharConstant.COMMA + detail.getRemarks());
                entityDtoList.add(entityDto);
            }
        }
        stkMisDeliveryDto.setStkMisDeliveryFEntityDtoList(entityDtoList);
        return cloudClient.synStkMisDelivery(stkMisDeliveryDto);
    }

    public KingdeeSynReturnDto synCKForAfterSale(List<AfterSaleProductAllotForm> afterSaleProductAllotFormList){
        StkMisDeliveryDto stkMisDeliveryDto = new StkMisDeliveryDto();
        stkMisDeliveryDto.setExtendId("--");
        stkMisDeliveryDto.setExtendType(ExtendTypeEnum.售后调拨.name());
        stkMisDeliveryDto.setDate(LocalDate.now());
        stkMisDeliveryDto.setDepartmentNumber("0001");//待定
        stkMisDeliveryDto.setMisDeliveryType(StkMisDeliveryTypeEnum.出库.name());
        List<StkMisDeliveryFEntityDto> entityDtoList = Lists.newArrayList();
        List<String> materialIdList = afterSaleProductAllotFormList.stream().map(AfterSaleProductAllotForm::getFromProductId).collect(Collectors.toList());
        Map<String, Product> productIdToOutCodeMap = productRepository.findByEnabledIsTrueAndIdIn(materialIdList).stream().collect(Collectors.toMap(Product::getId, Product->Product));
        List<String> storeIdList = afterSaleProductAllotFormList.stream().map(AfterSaleProductAllotForm::getStoreId).collect(Collectors.toList());
        Map<String, DepotStore> depotStoreIdMap = depotStoreRepository.findByIds(storeIdList).stream().collect(Collectors.toMap(DepotStore::getId, DepotStore->DepotStore));
        for (AfterSaleProductAllotForm detail : afterSaleProductAllotFormList){
            if (detail.getFromQty() != null && detail.getFromQty() >0) {
                StkMisDeliveryFEntityDto entityDto = new StkMisDeliveryFEntityDto();
                Product product = productIdToOutCodeMap.get(detail.getFromProductId());
                if (product.getCode() != null) {
                    entityDto.setMaterialNumber(product.getCode());
                } else {
                    throw new ServiceException(product.getName() + " 该货品没有编码，不能开单");
                }
                entityDto.setQty(detail.getFromQty());
                DepotStore toDepotStore = depotStoreIdMap.get(detail.getStoreId());
                if (toDepotStore.getOutCode() != null) {
                    entityDto.setStockNumber(toDepotStore.getOutCode());//调入仓库
                } else {
                    throw new ServiceException(toDepotStore.getId() + " 该仓库（ID）没有编码，不能开单");
                }
                entityDto.setFEntryNote( detail.getAfterSaleId() + CharConstant.COMMA + detail.getRemarks());
                entityDtoList.add(entityDto);
            }
        }
        stkMisDeliveryDto.setStkMisDeliveryFEntityDtoList(entityDtoList);
        return cloudClient.synStkMisDelivery(stkMisDeliveryDto);
    }
    
}
