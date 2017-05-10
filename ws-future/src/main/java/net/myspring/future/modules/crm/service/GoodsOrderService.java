package net.myspring.future.modules.crm.service;

import com.google.common.collect.Maps;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.mapper.*;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.mapper.*;
import net.myspring.future.modules.layout.mapper.ShopGoodsDepositMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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


    public Page<GoodsOrder> findPage(Pageable pageable, Map<String, Object> param) {
        Page<GoodsOrder> page = goodsOrderMapper.findPage(pageable, param);
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

    @Transactional(readOnly = false)
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

}
