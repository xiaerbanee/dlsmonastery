package net.myspring.future.modules.layout.service;

import net.myspring.future.common.enums.OutBillTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.layout.domain.ShopGoodsDeposit;
import net.myspring.future.modules.layout.dto.ShopGoodsDepositDto;
import net.myspring.future.modules.layout.mapper.ShopGoodsDepositMapper;
import net.myspring.future.modules.layout.web.form.ShopGoodsDepositForm;
import net.myspring.future.modules.layout.web.query.ShopGoodsDepositQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ShopGoodsDepositService {

    @Autowired
    private ShopGoodsDepositMapper shopGoodsDepositMapper;
    @Autowired
    private CacheUtils cacheUtils;

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

    public Page<ShopGoodsDepositDto> findPage(Pageable pageable, ShopGoodsDepositQuery shopGoodsDepositQuery) {

        Page<ShopGoodsDepositDto> page = shopGoodsDepositMapper.findPage(pageable, shopGoodsDepositQuery);

        cacheUtils.initCacheInput(page.getContent());
        return page;
    }



    public void audit(String[] ids){
    }

    public ShopGoodsDepositForm getForm(ShopGoodsDepositForm shopGoodsDepositForm) {
        ShopGoodsDepositForm result;

        if(shopGoodsDepositForm.isCreate()){
            result = new ShopGoodsDepositForm();
            if(shopGoodsDepositForm.getShopId()!=null){
                result.setShopId(shopGoodsDepositForm.getShopId());
                result.setDepartMent("");//TODO 需要设置department
            }

        }else{
            ShopGoodsDeposit sgd = shopGoodsDepositMapper.findOne(shopGoodsDepositForm.getId());
            result = BeanUtil.map(sgd, ShopGoodsDepositForm.class);

        }

        return result;

    }

    public void save(ShopGoodsDepositForm shopGoodsDepositForm) {

        if(shopGoodsDepositForm.isCreate()){
            ShopGoodsDeposit shopGoodsDeposit = new ShopGoodsDeposit();
            ReflectionUtil.copyProperties(shopGoodsDepositForm, shopGoodsDeposit);
            shopGoodsDeposit.setStatus("省公司审核");
            shopGoodsDeposit.setOutBillType(OutBillTypeEnum.手工日记账.name());
            shopGoodsDepositMapper.save(shopGoodsDeposit);

        }else{

            ShopGoodsDeposit shopGoodsDeposit = shopGoodsDepositMapper.findOne(shopGoodsDepositForm.getId());
            shopGoodsDeposit.setBankId(shopGoodsDepositForm.getBankId());
            shopGoodsDeposit.setRemarks(shopGoodsDepositForm.getRemarks());
            shopGoodsDeposit.setAmount(shopGoodsDepositForm.getAmount());
            shopGoodsDepositMapper.update(shopGoodsDeposit);
        }


    }

    public void batchAudit(String[] ids) {
        if(ids==null){
            return;
        }

//                TODO 要檢查確認批量審核的所有記錄對應的門店都綁定了財務
//                if(StringUtils.isBlank(shopGoodsDeposit.getShop().getRealOutId())){
//                    throw new ServiceException("审核失败,"+shopGoodsDeposit.getShopName()+"没有绑定财务门店；");
//                }
            for(String eachId : ids){
                if(eachId == null){
                    continue;
                }
                ShopGoodsDeposit shopGoodsDeposit=shopGoodsDepositMapper.findOne(eachId);
                if(shopGoodsDeposit == null || !"省公司审核".equals(shopGoodsDeposit.getStatus()) || StringUtils.isNotBlank(shopGoodsDeposit.getOutCode())){
                    continue;
                }

//                    String otherTypes="其他应付款-订货会订金";
                if(OutBillTypeEnum.手工日记账.name().equals(shopGoodsDeposit.getOutBillType())){
                        //TODO 同步金蝶
//                        K3CloudSynEntity k3CloudSynEntity = new K3CloudSynEntity(K3CloudSave.K3CloudFormId.CN_JOURNAL.name(),shopGoodsDeposit.getBankJournal(otherTypes),shopGoodsDeposit.getId(),shopGoodsDeposit.getFormatId(), K3CloudSynEntity.ExtendType.定金收款.name());
//                        k3cloudSynDao.save(k3CloudSynEntity);
//                        shopGoodsDeposit.setK3CloudSynEntity(k3CloudSynEntity);
                }
                if(OutBillTypeEnum.其他应收单.name().equals(shopGoodsDeposit.getOutBillType())){
                        //TODO 同步金蝶
//                        K3CloudSynEntity k3CloudSynEntity = new K3CloudSynEntity(K3CloudSave.K3CloudFormId.AR_OtherRecAble.name(),shopGoodsDeposit.getAROtherRecAbleImage(otherTypes),shopGoodsDeposit.getId(),shopGoodsDeposit.getFormatId(), K3CloudSynEntity.ExtendType.定金收款.name());
//                        k3cloudSynDao.save(k3CloudSynEntity);
//                        shopGoodsDeposit.setK3CloudSynEntity(k3CloudSynEntity);
                }
                shopGoodsDeposit.setStatus("已通过");
                shopGoodsDeposit.setLocked(true);
                shopGoodsDeposit.setBillDate(LocalDateTime.now());
                shopGoodsDepositMapper.update(shopGoodsDeposit);
            }

    }

    public void delete(ShopGoodsDepositForm shopGoodsDepositForm) {
        shopGoodsDepositMapper.logicDeleteOne(shopGoodsDepositForm.getId());
    }

}
