package net.myspring.future.modules.layout.service;

import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.OutBillTypeEnum;
import net.myspring.future.common.enums.ShopDepositTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.layout.domain.ShopDeposit;
import net.myspring.future.modules.layout.dto.ShopDepositDto;
import net.myspring.future.modules.layout.repository.ShopDepositRepository;
import net.myspring.future.modules.layout.web.form.ShopDepositForm;
import net.myspring.future.modules.layout.web.query.ShopDepositQuery;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class ShopDepositService {

    @Autowired
    private ShopDepositRepository shopDepositRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<ShopDepositDto> findPage(Pageable pageable, ShopDepositQuery shopDepositQuery) {
        Page<ShopDepositDto> page = shopDepositRepository.findPage(pageable, shopDepositQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void save(ShopDepositForm shopDepositForm) {

        if(!shopDepositForm.isCreate()){
            throw new ServiceException();
        }

        if(shopDepositForm.isImageAmountValid()){
            saveShopDeposit(shopDepositForm, ShopDepositTypeEnum.形象保证金, shopDepositForm.getImageAmount());
        }
        if(shopDepositForm.isMarketAmountValid()){
            saveShopDeposit(shopDepositForm, ShopDepositTypeEnum.市场保证金, shopDepositForm.getMarketAmount());
        }
        if(shopDepositForm.isDemoPhoneAmountValid()){
            saveShopDeposit(shopDepositForm, ShopDepositTypeEnum.演示机押金, shopDepositForm.getDemoPhoneAmount());
        }

        if(!OutBillTypeEnum.不同步到金蝶.name().equals(shopDepositForm.getOutBillType())){
            //        TODO 同步到金蝶
//            k3cloudSynService.syn(idList, K3CloudSynEntity.ExtendType.押金列表.name());
       }

    }

    private void saveShopDeposit(ShopDepositForm shopDepositForm, ShopDepositTypeEnum type, BigDecimal amount) {
        ShopDeposit shopDeposit  = new ShopDeposit();
        shopDeposit.setShopId(shopDepositForm.getShopId());
        shopDeposit.setBankId(shopDepositForm.getBankId());
        shopDeposit.setBillDate(shopDepositForm.getBillDate());
        shopDeposit.setAmount(amount);
        shopDeposit.setType(type.name());
        shopDeposit.setRemarks(type.name()+"  "+ StringUtils.trimToEmpty(shopDepositForm.getRemarks()));

        ShopDeposit latest = shopDepositRepository.findLatest(type.name(), shopDepositForm.getShopId());

        BigDecimal leftAmount = (latest == null ? BigDecimal.ZERO : latest.getLeftAmount()).add(amount);
        shopDeposit.setLeftAmount(leftAmount);

        //TODO 保存之前調用金蝶

        shopDepositRepository.save(shopDeposit);

        if(latest != null){
            latest.setLocked(true);
            shopDepositRepository.save(latest);
        }

    }

    public ShopDepositDto findDto(String id) {

        ShopDepositDto shopDepositDto = shopDepositRepository.findDto(id);
        cacheUtils.initCacheInput(shopDepositDto);
        return shopDepositDto;
    }

    public BigDecimal findLeftAmount(String type, String depotId) {
        ShopDeposit latest = shopDepositRepository.findLatest(type, depotId);
        return (latest == null ? BigDecimal.ZERO : latest.getLeftAmount());
    }
}
