package net.myspring.future.modules.crm.service;

import com.google.common.collect.Maps;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.mapper.*;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.mapper.*;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery;
import net.myspring.future.modules.layout.mapper.ShopGoodsDepositMapper;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GoodsOrderService {

    @Autowired
    private GoodsOrderMapper goodsOrderMapper;
    @Autowired
    private GoodsOrderDetailMapper goodsOrderDetailMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductImeMapper productImeMapper;
    @Autowired
    private GoodsOrderImeMapper goodsOrderImeMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private PricesystemDetailMapper pricesystemDetailMapper;
    @Autowired
    private ExpressOrderMapper expressOrderMapper;
    @Autowired
    private ExpressCompanyMapper expressCompanyMapper;
    @Autowired
    private ExpressMapper expressMapper;
    @Autowired
    private ShopGoodsDepositMapper shopGoodsDepositMapper;
    @Autowired
    private BankMapper bankMapper;

    @Autowired

    private CacheUtils cacheUtils;
    @Autowired
    private GoodsOrderDetailService goodsOrderDetailService;


    public Page<GoodsOrderDto> findPage(Pageable pageable, GoodsOrderQuery goodsOrderQuery) {
        Page<GoodsOrderDto> page = goodsOrderMapper.findPage(pageable, goodsOrderQuery);

        for (GoodsOrderDto each : page.getContent()) {
            if (GoodsOrderStatusEnum.待开单.name().equals(each.getStatus())) {
//               TODO 设置应收 each.setShopShouldGet();

            }
        }

        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public  List<Depot> findStores(GoodsOrder goodsOrder){
        return null;
    }

    public GoodsOrder findOne(String id) {
        GoodsOrder goodsOrder = goodsOrderMapper.findOne(id);
        return goodsOrder;
    }

    public void sign(GoodsOrder goodsOrder) {
        goodsOrderMapper.update(goodsOrder);
    }

    public GoodsOrder shipBoxAndIme(GoodsOrder goodsOrder) {
        return goodsOrder;
    }

    public void print(GoodsOrder goodsOrder) {
        ExpressOrder expressOrder = goodsOrder.getExpressOrder();
        if (expressOrder != null) {
            if (expressOrder.getOutPrintDate() == null) {
                expressOrder.setOutPrintDate(LocalDateTime.now());
            }
            expressOrderMapper.update(expressOrder);
        }
    }

    public void shipPrint(GoodsOrder goodsOrder) {
        ExpressOrder expressOrder = goodsOrder.getExpressOrder();
        if (expressOrder != null) {
            if (expressOrder.getExpressPrintDate() == null) {
                expressOrder.setExpressPrintDate(LocalDateTime.now());
            }
            expressOrderMapper.update(expressOrder);
        }
    }


    public void shipBack(GoodsOrder goodsOrder) {
    }

    public void delete(GoodsOrder goodsOrder) {
    }

    public GoodsOrder save(GoodsOrder goodsOrder) {
        return null;
    }


    public GoodsOrder bill(GoodsOrder goodsOrder) throws Exception{
        return null;
    }

    private GoodsOrder syn(GoodsOrder goodsOrder) throws Exception{
        return goodsOrder;
    }

    public void sreturn(GoodsOrder goodsOrder) {
        goodsOrderMapper.update(goodsOrder);
    }

    public GoodsOrder getBillChange(GoodsOrder goodsOrder) {
        return goodsOrder;
    }

    public GoodsOrder getBillGoodsOrder(GoodsOrder goodsOrder) {
        return goodsOrder;
    }

    public GoodsOrder findGoodsOrderDetail(GoodsOrder goodsOrder) {
        return goodsOrder;
    }

    public Map<String, Object> findBtnCopy(GoodsOrder goodsOrder) {
        Map<String, Object> paramMap = Maps.newHashMap();
        return paramMap;
    }

    public void ship(GoodsOrder goodsOrder) {
        goodsOrderMapper.update(goodsOrder);
        //设置快递单
    }


    public GoodsOrder findByFormatId(String formatId) {
        return null;
    }

    private Map<String, Integer> getMap(List<GoodsOrderDetail> orderList, String columnName) {
        return null;
    }


    public List<GoodsOrder> findByStoreBillData(LocalDate billDate, String storeId, String status){
        return goodsOrderMapper.findByStoreBillData(billDate, storeId, status);
    }

    public GoodsOrderForm findForm(GoodsOrderForm goodsOrderForm) {


        GoodsOrderForm result = new GoodsOrderForm();


        result.setShipTypeList(ShipTypeEnum.getList());
//TODO 判斷公司類比額
//        if(CompanyNameEnum.JXOPPO.name().equals(RequestUtils.getCompanyId().getCompany().getName()) || Company.CompanyName.JXvivo.name().equals(AccountUtils.getCompany().getName()) ){
//            result.setNetTypeList(Arrays.asList(NetTypeEnum.移动.name(), NetTypeEnum.联信.name()));
//        }else{
//            result.setNetTypeList(Arrays.asList(NetTypeEnum.全网通.name()));
//        }

        if(goodsOrderForm.isCreate()){
            return result;
        }

        ReflectionUtil.copyProperties(goodsOrderMapper.findOne(goodsOrderForm.getId()), result);

        if (result.getShopId() == null) {
            return result;
        }

        Date date = new Date();

        result.setGoodsOrderDetailFormList(goodsOrderDetailService.getGoodsOrderDetailListForEdit(result.getId(), result.getShopId()));
        //TODO  需要判斷設置alreadyQty
        if (result.getShopId() != null) {
            // 检查是否有权限
            //DepotDto shop = depotService.findOne(result.getShopId());
            //TODO 檢查是否有權限
//            if (!DepotUtils.isAccess(shop, false)) {
//                throw new ServiceException("exception_goods_order_no_order_permission");
//            }
//TODO 判斷公司類別
//            if(Company.CompanyName.WZOPPO.name().equals(AccountUtils.getCompany().getName())){
//                if(StringUtils.isEmpty(goodsOrder.getShipType())){
//                    result.setShipType(ShipTypeEnum.总部发货.name());
//                }
//            }

//
//            Map<Long, GoodsOrderDetail> goodsOrderDetailMap = Maps.newHashMap();
//            if (Collections3.isNotEmpty(goodsOrder.getGoodsOrderDetails())) {
//                for (GoodsOrderDetail goodsOrderDetail : goodsOrder.getGoodsOrderDetails()) {
//                    goodsOrderDetailMap.put(goodsOrderDetail.getProduct().getId(), goodsOrderDetail);
//                }
//            }
//            if(StringUtils.isNotBlank(goodsOrder.getNetType()) && shop.getPricesystemId()!=null){
//                Pricesystem pricesystem=pricesystemService.findOne(shop.getPricesystemId(),goodsOrder.getNetType());
//                model.addAttribute("pricesystem", pricesystem);
//            }else{
//                model.addAttribute("pricesystem", shop.getPricesystem());
//            }
//            // 办事处已订货数
//            Map<Long, Integer> orderMap = goodsOrderDetailService.getOrderMap(shop.getOffice().getParentId(), date,date);
//            model.addAttribute("orderMap", orderMap);
//            model.addAttribute("shop", shop);
//            model.addAttribute("carrierShops", shop.getCarrierShops());
//            model.addAttribute("goodsOrderDetailMap", goodsOrderDetailMap);

        }


        return result;
    }
}
