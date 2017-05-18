package net.myspring.future.modules.layout.service;

import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.OutBillTypeEnum;
import net.myspring.future.common.enums.ShopDepositTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.mapper.BankMapper;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.layout.domain.ShopDeposit;
import net.myspring.future.modules.layout.dto.ShopDepositDto;
import net.myspring.future.modules.layout.mapper.ShopDepositMapper;
import net.myspring.future.modules.layout.web.form.ShopDepositForm;
import net.myspring.future.modules.layout.web.query.ShopDepositQuery;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ShopDepositService {

    @Autowired
    private ShopDepositMapper shopDepositMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private BankMapper bankMapper;

    @Transactional(readOnly = true)
    public ShopDeposit findOne(String id){
        ShopDeposit shopDeposit = shopDepositMapper.findOne(id);
        return shopDeposit;
    }

    public Page<ShopDepositDto> findPage(Pageable pageable, ShopDepositQuery shopDepositQuery) {
        Page<ShopDepositDto> page = shopDepositMapper.findPage(pageable, shopDepositQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

	public ShopDeposit findLatest(String shopId, String type) {
	    List<ShopDeposit> shopDeposits= shopDepositMapper.findByTypeAndShopId(type,shopId,1);
		return null;
	}

	public List<String> getDepartments(){
        return null;
    }

    public int save(ShopDeposit shopDeposit){
	    //TODO 調用金蝶
	    return shopDepositMapper.save(shopDeposit);
    }

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

    public List<String> findShopDepositTypeList() {
        return ShopDepositTypeEnum.getList();
    }

    @Autowired
    private CacheUtils cacheUtils;

    public ShopDepositForm findForm(ShopDepositForm shopDepositForm) {
        if(!shopDepositForm.isCreate()){
            ShopDeposit shopDeposit=shopDepositMapper.findOne(shopDepositForm.getId());
            shopDepositForm= BeanUtil.map(shopDeposit, ShopDepositForm.class);
            cacheUtils.initCacheInput(shopDepositForm);
        }
        return shopDepositForm;
    }

    public void save(ShopDepositForm shopDepositForm) {

        if(!shopDepositForm.isCreate()){
            throw new ServiceException();
        }

        if(shopDepositForm.isImageAmountPositive()){
            save(genShopDepositFromForm(shopDepositForm, ShopDepositTypeEnum.形象保证金, shopDepositForm.getImageAmount()));
        }
        if(shopDepositForm.isMarketAmountPositive()){
            save(genShopDepositFromForm(shopDepositForm, ShopDepositTypeEnum.市场保证金, shopDepositForm.getMarketAmount()));
        }
        if(shopDepositForm.isDemoPhoneAmountPositive()){
            save(genShopDepositFromForm(shopDepositForm, ShopDepositTypeEnum.演示机押金, shopDepositForm.getDemoPhoneAmount()));
        }

        if(!OutBillTypeEnum.不同步到金蝶.name().equals(shopDepositForm.getOutBillType())){
            //        TODO 同步到金蝶
//            k3cloudSynService.syn(idList, K3CloudSynEntity.ExtendType.押金列表.name());
       }

    }

    private ShopDeposit genShopDepositFromForm(ShopDepositForm shopDepositForm, ShopDepositTypeEnum type, BigDecimal amount) {
        ShopDeposit shopDeposit  = new ShopDeposit();
        shopDeposit.setShopId(shopDepositForm.getShopId());
        shopDeposit.setBankId(shopDepositForm.getBankId());
        shopDeposit.setBillDate(shopDepositForm.getBillDate());
        shopDeposit.setAmount(amount);
        shopDeposit.setType(type.name());
        shopDeposit.setLocked(Boolean.FALSE);
        shopDeposit.setEnabled(Boolean.TRUE);
        shopDeposit.setRemarks(type.name()+"  "+shopDepositForm.getRemarks());

        shopDeposit.setLeftAmount(getLeftAmout(shopDepositForm.getShopId(), type.name()).add(amount));
        return shopDeposit;
    }

    private BigDecimal getLeftAmout(String shopId, String type) {
        List<ShopDeposit> sdList = shopDepositMapper.findByTypeAndShopId(shopId, type, 1);
        BigDecimal result = null;
        if(sdList!=null && sdList.size()>0){
            result = sdList.get(0).getLeftAmount();
        }
        if(result == null){
            result = BigDecimal.ZERO;
        }
        return result;
    }

    public void logicDelete(ShopDepositForm shopDepositForm) {
        shopDepositMapper.logicDeleteOne(shopDepositForm.getId());
    }
}
