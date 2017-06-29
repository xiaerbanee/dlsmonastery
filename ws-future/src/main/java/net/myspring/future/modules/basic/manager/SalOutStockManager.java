package net.myspring.future.modules.basic.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.modules.input.dto.SalOutStockDto;
import net.myspring.cloud.modules.input.dto.SalOutStockFEntityDto;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.ClientDto;
import net.myspring.future.modules.basic.repository.ClientRepository;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotStoreRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.GoodsOrder;
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
 * 销售出库单
 * Created by lihx on 2017/6/26.
 */
@Component
public class SalOutStockManager {
    @Autowired
    private GoodsOrderDetailRepository goodsOrderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DepotStoreRepository depotStoreRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CloudClient cloudClient;

    public List<KingdeeSynReturnDto> synForGoodsOrder(GoodsOrder goodsOrder){
        if (StringUtils.isNotBlank(goodsOrder.getId())){
            List<GoodsOrderDetail> goodsOrderDetailList = goodsOrderDetailRepository.findByGoodsOrderId(goodsOrder.getId());
            DepotStore depotStore = depotStoreRepository.findByEnabledIsTrueAndDepotId(goodsOrder.getStoreId());
            ClientDto clientDto = clientRepository.findByDepotId(goodsOrder.getShopId());
            List<String> productIdList = goodsOrderDetailList.stream().map(GoodsOrderDetail::getProductId).collect(Collectors.toList());
            Map<String,Product> productIdToOutCodeMap = productRepository.findByEnabledIsTrueAndIdIn(productIdList).stream().collect(Collectors.toMap(Product::getId,Product->Product));
            List<SalOutStockDto> salOutStockDtoList = Lists.newArrayList();
            SalOutStockDto salOutStockDto = new SalOutStockDto();
            salOutStockDto.setExtendId(goodsOrder.getId());
            salOutStockDto.setExtendType(ExtendTypeEnum.货品订货.name());
            salOutStockDto.setDate(goodsOrder.getBillDate());
            salOutStockDto.setCustomerNumber(clientDto.getOutCode());
            salOutStockDto.setNote(goodsOrder.getRemarks());
            List<SalOutStockFEntityDto> entityDtoList = Lists.newArrayList();
            for (GoodsOrderDetail detail : goodsOrderDetailList) {
                if (detail.getBillQty() != null && detail.getBillQty() > 0) {
                    SalOutStockFEntityDto entityDto = new SalOutStockFEntityDto();
                    entityDto.setStockNumber(depotStore.getOutCode());
                    Product product = productIdToOutCodeMap.get(detail.getProductId());
                    if (product.getCode() != null){
                        entityDto.setMaterialNumber(product.getCode());
                    }else{
                        throw new ServiceException(product.getName()+" 该货品没有编码，不能开单");
                    }
                    entityDto.setQty(detail.getBillQty());
                    entityDto.setPrice(detail.getPrice());
                    entityDto.setEntryNote(goodsOrder.getRemarks());
                    entityDtoList.add(entityDto);
                    salOutStockDto.setSalOutStockFEntityDtoList(entityDtoList);
                    salOutStockDtoList.add(salOutStockDto);
                }
            }
            return cloudClient.synSalOutStock(salOutStockDtoList);
        }
        return null;
    }
}
