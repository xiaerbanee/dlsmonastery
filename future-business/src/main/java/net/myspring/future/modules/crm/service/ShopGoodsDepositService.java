package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.ShopGoodsDeposit;
import net.myspring.future.modules.crm.mapper.ShopGoodsDepositMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ShopGoodsDepositService {

    @Autowired
    private ShopGoodsDepositMapper shopGoodsDepositMapper;

    public BigDecimal getTotalAmount(String shopId) {
        return null;
    }

    public List<ShopGoodsDeposit> findByIds(List<String> ids){
        List<ShopGoodsDeposit> shopGoodsDepositList = shopGoodsDepositMapper.findByIds(ids);
        return shopGoodsDepositList;
    }

    public ShopGoodsDeposit findOne(String id){
        ShopGoodsDeposit shopGoodsDeposit=shopGoodsDepositMapper.findOne(id);
        return shopGoodsDeposit;
    }

    public Page<ShopGoodsDeposit> findPage(Pageable pageable, Map<String, Object> map) {
        Page<ShopGoodsDeposit> page = shopGoodsDepositMapper.findPage(pageable, map);
        return page;
    }

    @Transactional
    public void delete(ShopGoodsDeposit shopGoodsDeposit) {
        shopGoodsDeposit.setEnabled(false);
        shopGoodsDepositMapper.update(shopGoodsDeposit);
    }

    @Transactional
    public void save(ShopGoodsDeposit shopGoodsDeposit){

    }

    @Transactional
    public void audit(String[] ids){
    }
}
