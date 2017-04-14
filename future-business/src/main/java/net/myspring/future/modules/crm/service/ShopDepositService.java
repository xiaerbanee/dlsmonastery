package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.ShopDeposit;
import net.myspring.future.modules.crm.mapper.BankMapper;
import net.myspring.future.modules.crm.mapper.DepotMapper;
import net.myspring.future.modules.crm.mapper.ShopDepositMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ShopDepositService {

    @Autowired
    private ShopDepositMapper shopDepositMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private BankMapper bankMapper;

    public ShopDeposit findOne(String id){
        ShopDeposit shopDeposit = shopDepositMapper.findOne(id);
        return shopDeposit;
    }

    public Page<ShopDeposit> findPage(Pageable pageable, Map<String, Object> map) {
        Page<ShopDeposit> page = shopDepositMapper.findPage(pageable, map);
        return page;
    }


	public ShopDeposit findLatest(String shopId, String type) {
	    List<ShopDeposit> shopDeposits= shopDepositMapper.findByTypeAndShopId(type,shopId,1);
		return null;
	}

	public List<String> getDepartments(){
        return null;
    }

    @Transactional
    public List<String> save(ShopDeposit shopDeposit){
	    return null;
    }

    @Transactional
    public void update(ShopDeposit shopDeposit){
    }

    @Transactional
    public void delete(ShopDeposit shopDeposit){
        List<ShopDeposit> shopDeposits = shopDepositMapper.findByTypeAndShopId(shopDeposit.getShopId(), shopDeposit.getType(), 2);
        shopDeposit.setEnabled(false);
        shopDepositMapper.update(shopDeposit);
        if(shopDeposits.size()==2) {
            ShopDeposit latest = shopDeposits.get(1);
            latest.setLocked(false);
            shopDepositMapper.update(shopDeposit);
        }
    }
}
