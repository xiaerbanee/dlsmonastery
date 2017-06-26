package net.myspring.future.modules.basic.manager;

import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.modules.input.dto.StkTransferDirectDto;
import net.myspring.cloud.modules.input.dto.StkTransferDirectFBillEntryDto;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.repository.GoodsOrderDetailRepository;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
import net.myspring.util.text.StringUtils;
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
    private CloudClient cloudClient;

    public KingdeeSynReturnDto synForGoodsOrder(GoodsOrderForm goodsOrderForm){
        if (StringUtils.isNotBlank(goodsOrderForm.getId())) {
            StkTransferDirectDto transferDirectDto = new StkTransferDirectDto();
            transferDirectDto.setExtendId(goodsOrderForm.getId());
            transferDirectDto.setExtendType(ExtendTypeEnum.货品订货.name());
            transferDirectDto.setNote(goodsOrderForm.getRemarks());
            transferDirectDto.setDate(LocalDate.now());
            List<GoodsOrderDetail> goodsOrderDetailList = goodsOrderDetailRepository.findByGoodsOrderId(goodsOrderForm.getId());
            List<String> productIdList = goodsOrderDetailList.stream().map(GoodsOrderDetail::getProductId).collect(Collectors.toList());
            Map<String, String> productIdToOutCodeMap = productRepository.findByEnabledIsTrueAndIdIn(productIdList).stream().collect(Collectors.toMap(Product::getId, Product::getCode));
            for (GoodsOrderDetail detail : goodsOrderDetailList) {
                StkTransferDirectFBillEntryDto entryDto = new StkTransferDirectFBillEntryDto();
                entryDto.setQty(detail.getBillQty());
                entryDto.setMaterialNumber(productIdToOutCodeMap.get(detail.getProductId()));
                entryDto.setSrcStockNumber(goodsOrderForm.getAllotFromStockCode());
                entryDto.setDestStockNumber(goodsOrderForm.getAllotToStockCode());
                transferDirectDto.getStkTransferDirectFBillEntryDtoList().add(entryDto);
            }
            return cloudClient.synStkTransferDirect(transferDirectDto);
        }
        return null;
    }
}
