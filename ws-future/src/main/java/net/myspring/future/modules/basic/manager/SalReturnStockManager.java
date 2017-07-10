package net.myspring.future.modules.basic.manager;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.modules.input.dto.SalReturnStockDto;
import net.myspring.cloud.modules.input.dto.SalReturnStockFEntityDto;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.ClientDto;
import net.myspring.future.modules.basic.repository.ClientRepository;
import net.myspring.future.modules.basic.repository.DepotStoreRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.repository.GoodsOrderDetailRepository;
import net.myspring.future.modules.layout.dto.ShopAllotDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 标准销售退货单
 * Created by lihx on 2017/6/30.
 */
@Component
public class SalReturnStockManager {
    @Autowired
    private GoodsOrderDetailRepository goodsOrderDetailRepository;
    @Autowired
    private DepotStoreRepository depotStoreRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CloudClient cloudClient;

    public List<KingdeeSynReturnDto> synForGoodsOrderShip(GoodsOrder goodsOrder){
        List<GoodsOrderDetail> goodsOrderDetailList = goodsOrderDetailRepository.findByGoodsOrderId(goodsOrder.getId());
        DepotStore depotStore = depotStoreRepository.findByEnabledIsTrueAndDepotId(goodsOrder.getStoreId());
        ClientDto clientDto = clientRepository.findByDepotId(goodsOrder.getShopId());
        List<String> productIdList = goodsOrderDetailList.stream().map(GoodsOrderDetail::getProductId).collect(Collectors.toList());
        Map<String,Product> productIdToOutCodeMap = productRepository.findByEnabledIsTrueAndIdIn(productIdList).stream().collect(Collectors.toMap(Product::getId, Product->Product));
        List<SalReturnStockDto> salReturnStockDtoList = Lists.newArrayList();
        SalReturnStockDto returnStockDto = new SalReturnStockDto();
        returnStockDto.setExtendId(goodsOrder.getId());
        returnStockDto.setExtendType(ExtendTypeEnum.货品订货.name());
        returnStockDto.setDate(goodsOrder.getBillDate());
        if(clientDto.getOutCode() != null){
            returnStockDto.setCustomerNumber(clientDto.getOutCode());
        }else{
            throw new ServiceException(clientDto.getName()+",该客户没有编码，不能开单");
        }
        returnStockDto.setNote(goodsOrder.getRemarks());
        List<SalReturnStockFEntityDto> entityDtoList = Lists.newArrayList();
        for (GoodsOrderDetail detail : goodsOrderDetailList){
            if (detail.getBillQty() != null && detail.getBillQty() >0) {
                SalReturnStockFEntityDto entityDto = new SalReturnStockFEntityDto();
                Product product = productIdToOutCodeMap.get(detail.getProductId());
                if (product.getCode() != null) {
                    entityDto.setMaterialNumber(product.getCode());
                } else {
                    throw new ServiceException(product.getName() + " 该货品没有编码，不能开单");
                }
                entityDto.setQty(detail.getReturnQty());
                entityDto.setPrice(detail.getPrice());
                entityDto.setEntryNote(goodsOrder.getRemarks());
                if(depotStore.getOutCode() != null){
                    entityDto.setStockNumber(depotStore.getOutCode());
                }else{
                    throw new ServiceException(depotStore.getId()+",该门店没有编码，不能开单");
                }
                entityDtoList.add(entityDto);
            }
        }
        returnStockDto.setSalReturnStockFEntityDtoList(entityDtoList);
        salReturnStockDtoList.add(returnStockDto);
        return cloudClient.synSalReturnStock(salReturnStockDtoList);
    }

    private List<KingdeeSynReturnDto> synForShopAllot(ShopAllotDto shopAllotDto){
        List<SalReturnStockDto> salReturnStockDtoList = Lists.newArrayList();
        SalReturnStockDto returnStockDto = new SalReturnStockDto();
        returnStockDto.setExtendId(shopAllotDto.getId());
        returnStockDto.setExtendType(ExtendTypeEnum.门店调拨.name());
        returnStockDto.setDate(LocalDate.now());
        returnStockDto.setCustomerNumber("");//shopAllotDto.getFromShop().getRealCode()
        returnStockDto.setNote("");//shopAllotDto.getBusinessId()+"申："+shopAllotDto.getRemarks()+"审:"+shopAllotDto.getAuditRemarks()
        List<SalReturnStockFEntityDto> entityDtoList = Lists.newArrayList();
        SalReturnStockFEntityDto entityDto = new SalReturnStockFEntityDto();
        entityDto.setMaterialNumber("");// shopAllotDetail.getProduct().getCode()
        entityDto.setQty(1);//shopAllotDetail.getQty()
        entityDto.setPrice(null);//shopAllotDetail.getReturnPrice()
        entityDto.setEntryNote("");//shopAllotDto.getBusinessId()+"申："+shopAllotDto.getRemarks()+"审:"+shopAllotDto.getAuditRemarks()
        entityDto.setStockNumber("");//getToShop().getDelegateDepot()==null?getStore().getRealCode():getToShop().getDelegateDepot().getRealCode()
        entityDtoList.add(entityDto);
        returnStockDto.setSalReturnStockFEntityDtoList(entityDtoList);
        salReturnStockDtoList.add(returnStockDto);
        return cloudClient.synSalReturnStock(salReturnStockDtoList);
    }
}
