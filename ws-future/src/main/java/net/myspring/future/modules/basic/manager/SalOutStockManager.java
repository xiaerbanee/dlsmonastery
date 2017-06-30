package net.myspring.future.modules.basic.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.modules.input.dto.SalOutStockDto;
import net.myspring.cloud.modules.input.dto.SalOutStockFEntityDto;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Client;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.ClientDto;
import net.myspring.future.modules.basic.repository.ClientRepository;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotStoreRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.repository.GoodsOrderDetailRepository;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;
import net.myspring.future.modules.layout.domain.ShopAllotDetail;
import net.myspring.future.modules.layout.dto.ShopAllotDto;
import net.myspring.future.modules.layout.repository.AdGoodsOrderDetailRepository;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
    private AdGoodsOrderDetailRepository adGoodsOrderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DepotStoreRepository depotStoreRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CloudClient cloudClient;

    public KingdeeSynReturnDto synForGoodsOrder(GoodsOrder goodsOrder){
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
            return cloudClient.synSalOutStock(salOutStockDtoList).get(0);
        }
        return null;
    }

    public List<KingdeeSynReturnDto> synForAdApply(List<AdGoodsOrder> adGoodsOrderList){
        List<SalOutStockDto> salOutStockDtoList = Lists.newArrayList();
        Map<String,Depot> depotMap = CollectionUtil.extractToMap(depotRepository.findAll(),"id");
        Map<String,Client> clientMap = CollectionUtil.extractToMap(clientRepository.findAll(),"id");
        Map<String,Product> productMap = CollectionUtil.extractToMap(productRepository.findAll(),"id");
        for (AdGoodsOrder adGoodsOrder : adGoodsOrderList){
            Depot outShop = depotMap.get(adGoodsOrder.getOutShopId());
            Client client = clientMap.get(outShop.getClientId());
            DepotStore depotStore = depotStoreRepository.findByEnabledIsTrueAndDepotId(adGoodsOrder.getStoreId());
            if(client == null || StringUtils.isBlank(client.getOutId())){
                throw new ServiceException(client.getName() + " 没有关联财务客户，不能申请");
            }
            if(depotStore == null || depotStore.getOutCode() == null){
                throw new ServiceException(client.getName() + " 没有关联财务仓库，不能申请");
            }
            SalOutStockDto salOutStockDto = new SalOutStockDto();
            salOutStockDto.setExtendId(adGoodsOrder.getId());
            salOutStockDto.setExtendType(ExtendTypeEnum.柜台订货.name());
            salOutStockDto.setDate(adGoodsOrder.getBillDate());
            salOutStockDto.setCustomerNumber(client.getOutCode());
            salOutStockDto.setNote(adGoodsOrder.getId()+ CharConstant.COMMA+outShop.getName()+CharConstant.COMMA+outShop.getContator()
                    +CharConstant.COMMA+outShop.getMobilePhone()+CharConstant.COMMA+outShop.getAddress()+CharConstant.COMMA+adGoodsOrder.getRemarks());
            List<SalOutStockFEntityDto> entityDtoList = Lists.newArrayList();
            List<AdGoodsOrderDetail> adGoodsOrderDetailLists = adGoodsOrderDetailRepository.findByAdGoodsOrderId(adGoodsOrder.getId());
            for(AdGoodsOrderDetail adGoodsOrderDetail:adGoodsOrderDetailLists){
                Product product = productMap.get(adGoodsOrderDetail.getProductId());
                SalOutStockFEntityDto entityDto = new SalOutStockFEntityDto();
                entityDto.setStockNumber(depotStore.getOutCode());
                if (product.getCode() != null){
                    entityDto.setMaterialNumber(product.getCode());
                }else{
                    throw new ServiceException(product.getName()+" 该货品没有编码，不能开单");
                }
                entityDto.setQty(adGoodsOrderDetail.getBillQty());
                // 是否赠品 赠品，电教，imoo 广告办事处的以原价出库
                if(BillTypeEnum.配件赠品.name().equals(adGoodsOrder.getBillType())){//或者是电教公司,而且的depot必须是金蝶里有的
                    entityDto.setPrice(product.getPrice2());
                }else{
                    entityDto.setPrice(BigDecimal.ZERO);
                }
                entityDto.setEntryNote(adGoodsOrder.getId()+CharConstant.COMMA+outShop.getName()+CharConstant.COMMA+outShop.getContator()
                        +CharConstant.COMMA+outShop.getMobilePhone()+CharConstant.COMMA+outShop.getAddress());
                entityDtoList.add(entityDto);
            }
            salOutStockDto.setSalOutStockFEntityDtoList(entityDtoList);
            salOutStockDtoList.add(salOutStockDto);
        }
        return cloudClient.synSalOutStock(salOutStockDtoList);
    }

    public List<KingdeeSynReturnDto> synForAdGoodsOrder(AdGoodsOrder adGoodsOrder, ExpressOrder expressOrder, List<AdGoodsOrderDetail> detailList,Map<String, Product> productMap){
        Depot depot = depotRepository.findOne(adGoodsOrder.getOutShopId());
        Client client = clientRepository.findOne(depot.getId());
        DepotStore depotStore = depotStoreRepository.findByEnabledIsTrueAndDepotId(adGoodsOrder.getStoreId());
        List<SalOutStockDto> salOutStockDtoList = Lists.newArrayList();
        SalOutStockDto salOutStockDto = new SalOutStockDto();
        salOutStockDto.setExtendId(adGoodsOrder.getId());
        salOutStockDto.setExtendType(ExtendTypeEnum.柜台订货.name());
        salOutStockDto.setDate(adGoodsOrder.getBillDate());
        salOutStockDto.setCustomerNumber(client.getOutCode());
        salOutStockDto.setNote(getFormatId(adGoodsOrder)+ CharConstant.COMMA+depot.getName()+CharConstant.COMMA+expressOrder.getContator()+CharConstant.COMMA+expressOrder.getMobilePhone()+CharConstant.COMMA+expressOrder.getAddress());

        List<SalOutStockFEntityDto> entityDtoList = Lists.newArrayList();
        for(AdGoodsOrderDetail adGoodsOrderDetail:detailList){
            SalOutStockFEntityDto entityDto = new SalOutStockFEntityDto();
            entityDto.setStockNumber(depotStore.getOutCode());
            Product product = productMap.get(adGoodsOrderDetail.getProductId());
            if (product.getCode() != null){
                entityDto.setMaterialNumber(product.getCode());
            }else{
                throw new ServiceException(product.getName()+" 该货品没有编码，不能开单");
            }
            entityDto.setQty(adGoodsOrderDetail.getBillQty());
            // 是否赠品 赠品，电教，imoo 广告办事处的以原价出库
            if(BillTypeEnum.配件赠品.name().equals(adGoodsOrder.getBillType())){//或者是电教公司,而且的depot必须是金蝶里有的
                entityDto.setPrice(productMap.get(adGoodsOrderDetail.getProductId()).getPrice2());//产品价格
            }else{
                entityDto.setPrice(BigDecimal.ZERO);
            }
            entityDto.setEntryNote(getFormatId(adGoodsOrder)+ CharConstant.COMMA+depot.getName()+CharConstant.COMMA+expressOrder.getContator()+CharConstant.COMMA+expressOrder.getMobilePhone()+CharConstant.COMMA+expressOrder.getAddress()+CharConstant.COMMA+adGoodsOrder.getRemarks());
            entityDtoList.add(entityDto);
        }
        salOutStockDto.setSalOutStockFEntityDtoList(entityDtoList);
        salOutStockDtoList.add(salOutStockDto);
        return cloudClient.synSalOutStock(salOutStockDtoList);

    }

    private String getFormatId(AdGoodsOrder adGoodsOrder) {
        if(StringUtils.isBlank(adGoodsOrder.getParentId()) || adGoodsOrder.getParentId().equals(adGoodsOrder.getId())){
            return RequestUtils.getCompanyName() + StringUtils.trimToEmpty(adGoodsOrder.getId());
        }
        return RequestUtils.getCompanyName() + StringUtils.trimToEmpty(adGoodsOrder.getParentId())+ CharConstant.UNDER_LINE + StringUtils.trimToEmpty(adGoodsOrder.getId());
    }

    private List<KingdeeSynReturnDto> synForShopAllot(ShopAllotDto shopAllotDto){
        List<SalOutStockDto> salOutStockDtoList = Lists.newArrayList();
        SalOutStockDto salOutStockDto = new SalOutStockDto();
        salOutStockDto.setExtendId("");
        salOutStockDto.setExtendType(ExtendTypeEnum.门店调拨.name());
        salOutStockDto.setDate(LocalDate.now());
        salOutStockDto.setCustomerNumber("00001");
        salOutStockDto.setNote("模拟测试");

        List<SalOutStockFEntityDto> entityDtoList = Lists.newArrayList();
        List<ShopAllotDetail> shopAllotDetailList = Lists.newArrayList();//开单关联的详细
        for (ShopAllotDetail shopAllotDetail : shopAllotDetailList) {
            if (shopAllotDetail.getQty() != null && shopAllotDetail.getQty() > 0) {
                SalOutStockFEntityDto entityDto = new SalOutStockFEntityDto();
                entityDto.setStockNumber("G00201");
                entityDto.setMaterialNumber("05YF");//其他收入费用类的物料
                entityDto.setQty(null);
                entityDto.setPrice(null);
                entityDto.setEntryNote("");
                entityDtoList.add(entityDto);
                salOutStockDto.setSalOutStockFEntityDtoList(entityDtoList);
                salOutStockDtoList.add(salOutStockDto);
            }
        }
        return cloudClient.synSalOutStock(salOutStockDtoList);

    }
}
