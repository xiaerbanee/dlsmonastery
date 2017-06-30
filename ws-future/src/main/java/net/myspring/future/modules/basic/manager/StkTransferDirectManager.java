package net.myspring.future.modules.basic.manager;

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
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.domain.StoreAllot;
import net.myspring.future.modules.crm.domain.StoreAllotDetail;
import net.myspring.future.modules.crm.repository.GoodsOrderDetailRepository;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
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
}
