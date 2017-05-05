package net.myspring.future.modules.layout.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.PricesystemDetailMapper;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.layout.domain.ShopAllot;
import net.myspring.future.modules.layout.domain.ShopAllotDetail;
import net.myspring.future.modules.layout.dto.ShopAllotDto;
import net.myspring.future.modules.layout.mapper.ShopAllotDetailMapper;
import net.myspring.future.modules.layout.mapper.ShopAllotMapper;
import net.myspring.future.modules.layout.web.query.ShopAllotQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ShopAllotService {

    @Autowired
    private ShopAllotMapper shopAllotMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private PricesystemDetailMapper pricesystemDetailMapper;
    @Autowired
    private ShopAllotDetailMapper shopAllotDetailMapper;
    @Autowired
    private CacheUtils cacheUtils;


    public ShopAllot findOne(String id){
        ShopAllot shopAllot = shopAllotMapper.findOne(id);
        BigDecimal saleTotalPrice = BigDecimal.ZERO;
        for(ShopAllotDetail shopAllotDetail:shopAllot.getShopAllotDetailList()){
            saleTotalPrice = saleTotalPrice.add(shopAllotDetail.getSalePrice().multiply(new BigDecimal(shopAllotDetail.getQty())));
        }
        return shopAllot;
    }


    public Page<ShopAllotDto> findPage(Pageable pageable, ShopAllotQuery shopAllotQuery) {
        Page<ShopAllotDto> page = shopAllotMapper.findPage(pageable, shopAllotQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public String checkShop(String fromShopId,String toShopId){
        StringBuffer sb = new StringBuffer();
        Depot fromShop = depotMapper.findOne(fromShopId);
        Depot toShop = depotMapper.findOne(toShopId);
        if(fromShopId.equals(toShopId)){
            sb.append("调拨前后门店不能相同");
        }
        if(fromShop.getPricesystemId() ==null ||
                (fromShop.getParentId()==null && fromShop.getCode()==null) ||
                (fromShop.getParentId()!=null && depotMapper.findOne(fromShop.getParentId()).getCode()==null && fromShop.getCode()==null)){
            sb.append(fromShop.getName()+"没有价格体系或者没有和财务关联\n");
        }
        if(toShop.getPricesystemId() ==null ||
                (toShop.getParentId()==null && toShop.getCode()==null) ||
                (toShop.getParentId()!=null && depotMapper.findOne(toShop.getParentId()).getCode()==null && toShop.getCode()==null)){
            sb.append(toShop.getName()+"没有价格体系或者没有和财务关联 \n");
        }
        return sb.toString();
    }

    public List<ShopAllotDetail> getProducts(String fromShopId, String toShopId){
        return null;
    }

    public ShopAllot getEditFormData(ShopAllot shopAllot){
        return null;
    }

    @Transactional
    public void logicDeleteOne(String id) {
        shopAllotMapper.logicDeleteOne(id);
    }

    @Transactional
    public void save(ShopAllot shopAllot){
    }

    @Transactional
    public void audit(ShopAllot shopAllot){

    }
}
