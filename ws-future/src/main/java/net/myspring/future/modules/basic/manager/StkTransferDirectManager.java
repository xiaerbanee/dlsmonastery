package net.myspring.future.modules.basic.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.modules.input.dto.StkTransferDirectDto;
import net.myspring.cloud.modules.input.dto.StkTransferDirectFBillEntryDto;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.repository.DepotStoreRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.*;
import net.myspring.future.modules.crm.dto.StoreAllotDto;
import net.myspring.future.modules.crm.repository.GoodsOrderDetailRepository;
import net.myspring.future.modules.crm.web.form.AfterSaleStoreAllotForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
import net.myspring.future.modules.layout.domain.ShopAllotDetail;
import net.myspring.future.modules.layout.dto.ShopAllotDto;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.elasticsearch.xpack.monitoring.collector.Collector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 直接调拨单
 * Created by lihx on 2017/6/26.
 */
@Component
public class StkTransferDirectManager {
    @Autowired
    private GoodsOrderDetailRepository goodsOrderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DepotStoreRepository depotStoreRepository;
    @Autowired
    private CloudClient cloudClient;

    public KingdeeSynReturnDto synForGoodsOrder(GoodsOrder goodsOrder, String allotFormStockCode,String allotToStokeCode){
        if (StringUtils.isNotBlank(goodsOrder.getId())) {
            StkTransferDirectDto transferDirectDto = new StkTransferDirectDto();
            transferDirectDto.setExtendId(goodsOrder.getId());
            transferDirectDto.setExtendType(ExtendTypeEnum.货品订货.name());
            transferDirectDto.setNote(goodsOrder.getRemarks());
            transferDirectDto.setDate(goodsOrder.getBillDate());
            List<GoodsOrderDetail> goodsOrderDetailList = goodsOrderDetailRepository.findByGoodsOrderId(goodsOrder.getId());
            List<String> productIdList = goodsOrderDetailList.stream().map(GoodsOrderDetail::getProductId).collect(Collectors.toList());
            Map<String, Product> productIdToOutCodeMap = productRepository.findByEnabledIsTrueAndIdIn(productIdList).stream().collect(Collectors.toMap(Product::getId, Product->Product));
            for (GoodsOrderDetail detail : goodsOrderDetailList) {
                if (detail.getBillQty() != null && detail.getBillQty() >0) {
                    StkTransferDirectFBillEntryDto entryDto = new StkTransferDirectFBillEntryDto();
                    entryDto.setQty(detail.getBillQty());
                    Product product = productIdToOutCodeMap.get(detail.getProductId());
                    if (product.getCode() != null) {
                        entryDto.setMaterialNumber(product.getCode());
                    } else {
                        throw new ServiceException(product.getName() + " 该货品没有编码，不能开单");
                    }
                    entryDto.setSrcStockNumber(allotFormStockCode);
                    entryDto.setDestStockNumber(allotToStokeCode);
                    transferDirectDto.getStkTransferDirectFBillEntryDtoList().add(entryDto);
                }
            }
            return cloudClient.synStkTransferDirect(transferDirectDto);
        }
        return null;
    }

    public KingdeeSynReturnDto synForGoodsOrderReturn(GoodsOrder goodsOrder, String allotFormStockCode,String allotToStokeCode){
        if (StringUtils.isNotBlank(goodsOrder.getId())) {
            StkTransferDirectDto transferDirectDto = new StkTransferDirectDto();
            transferDirectDto.setExtendId(goodsOrder.getId());
            transferDirectDto.setExtendType(ExtendTypeEnum.货品订货.name());
            transferDirectDto.setNote(null);
            transferDirectDto.setDate(null);
            List<GoodsOrderDetail> goodsOrderDetailList = goodsOrderDetailRepository.findByGoodsOrderId(goodsOrder.getId());
            List<String> productIdList = goodsOrderDetailList.stream().map(GoodsOrderDetail::getProductId).collect(Collectors.toList());
            Map<String, Product> productIdToOutCodeMap = productRepository.findByEnabledIsTrueAndIdIn(productIdList).stream().collect(Collectors.toMap(Product::getId, Product->Product));
            for (GoodsOrderDetail detail : goodsOrderDetailList) {
                if (detail.getReturnQty() != null && detail.getReturnQty() >0) {
                    StkTransferDirectFBillEntryDto entryDto = new StkTransferDirectFBillEntryDto();
                    entryDto.setQty(detail.getReturnQty());
                    Product product = productIdToOutCodeMap.get(detail.getProductId());
                    if (product.getCode() != null) {
                        entryDto.setMaterialNumber(product.getCode());
                    } else {
                        throw new ServiceException(product.getName() + " 该货品没有编码，不能开单");
                    }
                    entryDto.setSrcStockNumber(allotFormStockCode);
                    entryDto.setDestStockNumber(allotToStokeCode);
                    transferDirectDto.getStkTransferDirectFBillEntryDtoList().add(entryDto);
                }
            }
            return cloudClient.synStkTransferDirect(transferDirectDto);
        }
        return null;
    }

    public String getOutCode(String extendId,String extendType){
        List<KingdeeSynReturnDto> kingdeeSynReturnDtos = cloudClient.findByExtendIdAndExtendType(extendId, extendType);
        String result="";
        if(CollectionUtil.isNotEmpty(kingdeeSynReturnDtos)){
            List<String> billNoList=kingdeeSynReturnDtos.stream().map(KingdeeSynReturnDto::getBillNo).collect(Collectors.toList());
            result=StringUtils.join(billNoList, CharConstant.COMMA);
        }
        return result;
    }

    public KingdeeSynReturnDto synForStoreAllot(StoreAllot storeAllot, List<StoreAllotDetail> detailList, Map<String, Product> productMap){
        DepotStore fromDepotStore = depotStoreRepository.findByEnabledIsTrueAndDepotId(storeAllot.getFromStoreId());
        DepotStore toDepotStore = depotStoreRepository.findByEnabledIsTrueAndDepotId(storeAllot.getToStoreId());
        StkTransferDirectDto transferDirectDto = new StkTransferDirectDto();
        transferDirectDto.setExtendId(storeAllot.getId());
        transferDirectDto.setExtendType(ExtendTypeEnum.大库调拨.name());
        transferDirectDto.setNote(storeAllot.getRemarks());
        transferDirectDto.setDate(storeAllot.getBillDate());
        for (StoreAllotDetail detail : detailList){
            if (detail.getBillQty() != null && detail.getBillQty() >0) {
                StkTransferDirectFBillEntryDto entryDto = new StkTransferDirectFBillEntryDto();
                entryDto.setQty(detail.getQty());
                Product product = productMap.get(detail.getProductId());
                if (product.getCode() != null) {
                    entryDto.setMaterialNumber(product.getCode());
                } else {
                    throw new ServiceException(product.getName() + " 该货品没有编码，不能开单");
                }
                entryDto.setSrcStockNumber(fromDepotStore.getOutCode());
                entryDto.setDestStockNumber(toDepotStore.getOutCode());
                transferDirectDto.getStkTransferDirectFBillEntryDtoList().add(entryDto);
            }
        }
        return cloudClient.synStkTransferDirect(transferDirectDto);

    }

    public KingdeeSynReturnDto synForAfterSale(List<AfterSaleStoreAllotForm> afterSaleStoreAllotFormList){
        StkTransferDirectDto transferDirectDto = new StkTransferDirectDto();
        transferDirectDto.setExtendId("--");
        transferDirectDto.setExtendType(ExtendTypeEnum.售后调拨.name());
        transferDirectDto.setNote("");
        transferDirectDto.setDate(LocalDate.now());
        List<String> materialIdList = afterSaleStoreAllotFormList.stream().map(AfterSaleStoreAllotForm::getProductId).collect(Collectors.toList());
        Map<String, Product> productIdToOutCodeMap = productRepository.findByEnabledIsTrueAndIdIn(materialIdList).stream().collect(Collectors.toMap(Product::getId, Product->Product));
        List<String> fromStoreIdList = afterSaleStoreAllotFormList.stream().map(AfterSaleStoreAllotForm::getFromStoreId).collect(Collectors.toList());
        Map<String, DepotStore> fromDepotStoreIdMap = depotStoreRepository.findByIds(fromStoreIdList).stream().collect(Collectors.toMap(DepotStore::getId, DepotStore->DepotStore));
        List<String> toStoreIdList = afterSaleStoreAllotFormList.stream().map(AfterSaleStoreAllotForm::getToStoreId).collect(Collectors.toList());
        Map<String, DepotStore> toDepotStoreMap = depotStoreRepository.findByIds(toStoreIdList).stream().collect(Collectors.toMap(DepotStore::getId, DepotStore->DepotStore));
        for (AfterSaleStoreAllotForm afterSaleStoreAllot : afterSaleStoreAllotFormList) {
            if (afterSaleStoreAllot.getQty() != null && afterSaleStoreAllot.getQty() > 0) {
                StkTransferDirectFBillEntryDto entryDto = new StkTransferDirectFBillEntryDto();
                entryDto.setQty(afterSaleStoreAllot.getQty());
                Product product = productIdToOutCodeMap.get(afterSaleStoreAllot.getProductId());
                if (product.getCode() != null) {
                    entryDto.setMaterialNumber(product.getCode());
                } else {
                    throw new ServiceException(product.getName() + " 该货品没有编码，不能开单");
                }
                DepotStore fromDepotStore = fromDepotStoreIdMap.get(afterSaleStoreAllot.getFromStoreId());
                if (fromDepotStore.getOutCode() != null) {
                    entryDto.setSrcStockNumber(fromDepotStore.getOutCode());//调出仓库
                } else {
                    throw new ServiceException(fromDepotStore.getId() + " 该仓库（ID）没有编码，不能开单");
                }
                DepotStore toDepotStore = toDepotStoreMap.get(afterSaleStoreAllot.getToStoreId());
                if (toDepotStore.getOutCode() != null) {
                    entryDto.setDestStockNumber(toDepotStore.getOutCode());//调入仓库
                } else {
                    throw new ServiceException(toDepotStore.getId() + " 该仓库（ID）没有编码，不能开单");
                }
                entryDto.setNoteEntry(afterSaleStoreAllot.getAfterSaleId() + CharConstant.COMMA + afterSaleStoreAllot.getRemarks());
                transferDirectDto.getStkTransferDirectFBillEntryDtoList().add(entryDto);
            }
        }
        return cloudClient.synStkTransferDirect(transferDirectDto);
    }

    public KingdeeSynReturnDto synForShopAllot(ShopAllotDto shopAllotDto){
        StkTransferDirectDto transferDirectDto = new StkTransferDirectDto();
        transferDirectDto.setExtendId(shopAllotDto.getId());
        transferDirectDto.setExtendType(ExtendTypeEnum.门店调拨.name());
        transferDirectDto.setNote("");//getBusinessId()+"申："+getRemarks()+"审:"+getAuditRemarks()
        transferDirectDto.setDate(LocalDate.now());
        List<ShopAllotDetail> shopAllotDetailList = Lists.newArrayList();//开单关联的详细
        for (ShopAllotDetail shopAllotDetail : shopAllotDetailList) {
            if (shopAllotDetail.getQty() != null && shopAllotDetail.getQty() > 0) {
                StkTransferDirectFBillEntryDto entryDto = new StkTransferDirectFBillEntryDto();
                entryDto.setQty(shopAllotDetail.getQty());
                entryDto.setMaterialNumber("");
                entryDto.setSrcStockNumber(""); //调出仓库
                entryDto.setDestStockNumber("");//调入仓库
                entryDto.setNoteEntry("");//getBusinessId()+"申："+getRemarks()+"审:"+getAuditRemarks()
                transferDirectDto.getStkTransferDirectFBillEntryDtoList().add(entryDto);
            }
        }
        return cloudClient.synStkTransferDirect(transferDirectDto);
    }

}
