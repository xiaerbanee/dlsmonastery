package net.myspring.future.modules.layout.service;

import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.service.PricesystemDetailService;
import net.myspring.future.modules.layout.domain.ShopAllot;
import net.myspring.future.modules.layout.domain.ShopAllotDetail;
import net.myspring.future.modules.layout.dto.ShopAllotDto;
import net.myspring.future.modules.layout.mapper.ShopAllotMapper;
import net.myspring.future.modules.layout.web.form.ShopAllotAuditForm;
import net.myspring.future.modules.layout.web.form.ShopAllotDetailForm;
import net.myspring.future.modules.layout.web.form.ShopAllotForm;
import net.myspring.future.modules.layout.web.query.ShopAllotQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ShopAllotService {
    @Autowired
    private ShopAllotMapper shopAllotMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ShopAllotDetailService shopAllotDetailService;
    @Autowired
    private PricesystemDetailService pricesystemDetailService;


    public ShopAllot findOne(String id){

        return shopAllotMapper.findOne(id);
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
        return sb.toString();
    }

    public void logicDeleteOne(String id) {
        shopAllotMapper.logicDeleteOne(id);
    }


    public ShopAllotForm findForm(ShopAllotForm shopAllotForm) {

        if(shopAllotForm.isCreate()){
            return new ShopAllotForm();
        }

        ShopAllotDto shopAllotDto = findShopAllotDto(shopAllotForm.getId());
        ShopAllotForm result = BeanUtil.map(shopAllotDto, ShopAllotForm.class);
        result.setShopAllotDetailFormList(shopAllotDetailService.getShopAllotDetailListForEdit(shopAllotDto.getId(), shopAllotDto.getFromShopId(), shopAllotDto.getToShopId()));

        return result;
    }

    private ShopAllotDto findShopAllotDto(String id) {
        return shopAllotMapper.findShopAllotDto(id);
    }

    public ShopAllot saveOrUpdate(ShopAllotForm shopAllotForm) {
        ShopAllot shopAllot = null;
        if(shopAllotForm.isCreate()){
            shopAllot = new ShopAllot();
            ReflectionUtil.copyProperties(shopAllotForm, shopAllot);
            String maxBusinessId = shopAllotMapper.findMaxBusinessId(LocalDate.now());
            shopAllot.setBusinessId(StringUtils.getNextBusinessId(maxBusinessId));
            shopAllot.setStatus(AuditTypeEnum.APPLYING.name());
            shopAllot.setEnabled(Boolean.TRUE);
            shopAllotMapper.save(shopAllot);

            batchSaveShopAllotDetails(shopAllotForm.getShopAllotDetailFormList(), shopAllot);
        }else{
            shopAllot = shopAllotMapper.findOne(shopAllotForm.getId());
            shopAllot.setRemarks( shopAllotForm.getRemarks());
            shopAllotMapper.update(shopAllot);


            batchSaveShopAllotDetails(shopAllotForm.getShopAllotDetailFormList(), shopAllot);
        }

        return shopAllot;
    }

    /**
     * 该方法会首先清空ShopAllot已经关联的ShopAllotDetail记录。之后将传入的门店调拨明细插入ShopAllotDetail表中，并关联至传入的ShopAllot记录
     */
    private void batchSaveShopAllotDetails(List<ShopAllotDetailForm> shopAllotDetailFormList, ShopAllot shopAllot) {
        shopAllotDetailService.deleteByShopAllotId(shopAllot.getId());
        if(shopAllotDetailFormList == null || shopAllotDetailFormList.isEmpty()){
            return ;
        }
        Map<String, PricesystemDetail> fromPricesystemMap = pricesystemDetailService.findProductPricesystemDetailMap(shopAllot.getFromShopId());
        Map<String, PricesystemDetail> toPricesystemMap = pricesystemDetailService.findProductPricesystemDetailMap(shopAllot.getToShopId());

        List<ShopAllotDetail> shopAllotDetailsToBeSaved = new ArrayList<>();
        for(ShopAllotDetailForm each : shopAllotDetailFormList){
            ShopAllotDetail shopAllotDetail = new ShopAllotDetail();
            ReflectionUtil.copyProperties(each, shopAllotDetail);
            shopAllotDetail.setId(null);
            shopAllotDetail.setShopAllotId(shopAllot.getId());
            shopAllotDetail.setReturnPrice(fromPricesystemMap.get(shopAllotDetail.getProductId()).getPrice());
            shopAllotDetail.setSalePrice(toPricesystemMap.get(shopAllotDetail.getProductId()).getPrice());
            shopAllotDetailsToBeSaved.add(shopAllotDetail);
        }


        shopAllotDetailService.batchSave(shopAllotDetailsToBeSaved);


    }

    public void delete(ShopAllotForm shopAllotForm) {
        shopAllotMapper.logicDeleteOne(shopAllotForm.getId());
    }

    public ShopAllotDto findForViewOrAudit(String shopAllotId) {
        ShopAllotDto result =  shopAllotMapper.findShopAllotDto(shopAllotId);
        result.setShopAllotDetailList(shopAllotDetailService.getShopAllotDetailListForViewOrAudit(shopAllotId));

//TODO 如果是申请状态，需要看到两个门店的应收
//        if(AuditType.APPLY.toString().equals(shopAllot.getStatus())) {
//        result.setFromShopShouldGet();
//        result.setToShopShouldGet();
//            shopAllot.getFromShop().setShouldGet(k3cloudService.findShouldGet(company.getOutDbname(),shopAllot.getFromShop().getOutId()));
//            shopAllot.getToShop().setShouldGet(k3cloudService.findShouldGet(company.getOutDbname(),shopAllot.getToShop().getOutId()));
//        }

        cacheUtils.initCacheInput(result);
        return result;
    }

    public void audit(ShopAllotAuditForm shopAllotAuditForm) {
//        Account account = AccountUtils.getAccount();
//        if(account.getOutId()==null){
//            return new Message("message_shop_allot_no_finance_message_account",Message.Type.danger);
//        }

        ShopAllot shopAllot = findOne(shopAllotAuditForm.getId());
        shopAllot.setStatus(shopAllotAuditForm.getPass() ? AuditTypeEnum.PASSED.name() : AuditTypeEnum.NOT_PASS.name());
        shopAllot.setAuditRemarks(shopAllotAuditForm.getAuditRemarks());
        shopAllot.setAuditBy(RequestUtils.getAccountId());
        shopAllot.setAuditDate(LocalDateTime.now());
        shopAllotMapper.update(shopAllot);

        if(!shopAllotAuditForm.getPass()) {
            return;
        }

        if(shopAllotAuditForm.getShopAllotDetailList()!=null){
            for(ShopAllotDetailForm each : shopAllotAuditForm.getShopAllotDetailList()){
                ShopAllotDetail  shopAllotDetail = shopAllotDetailService.findOne(each.getId());
                shopAllotDetail.setReturnPrice(each.getReturnPrice());
                shopAllotDetail.setSalePrice(each.getSalePrice());
                shopAllotDetailService.update(shopAllotDetail);
            }
        }

        if(shopAllotAuditForm.getSyn()) {
//                Long defaultStoreId = Long.valueOf(Global.getCompanyConfig(shopAllot.getCompany().getId(), CompanyConfigCode.DEFAULT_STORE_ID.getCode()));
//                shopAllot.setStore(depotDao.findOne(defaultStoreId));
//                //都不是寄售门店  一出一退
//                if(shopAllot.getFromShop().getDelegateDepot()==null && shopAllot.getToShop().getDelegateDepot()==null){
//
//                    K3CloudSynEntity outReturnEntity =new K3CloudSynEntity(K3CloudSaveExtend.K3CloudFormId.SAL_RETURNSTOCK.name(),K3CloudSave.K3CloudFormId.AR_receivable.name(), shopAllot.getReturnStock(),shopAllot.getId(),shopAllot.getFormatId(), K3CloudSynEntity.ExtendType.门店调拨.name());
//                    k3cloudSynDao.save(outReturnEntity);
//                    shopAllot.setReturnCloudEntity(outReturnEntity);
//                    //销售开单
//                    K3CloudSynEntity outSaleEntity =new K3CloudSynEntity(K3CloudSaveExtend.K3CloudFormId.SAL_OUTSTOCK.name(),K3CloudSave.K3CloudFormId.AR_receivable.name(), shopAllot.getSaleOutstock(),shopAllot.getId(),shopAllot.getFormatId(), K3CloudSynEntity.ExtendType.门店调拨.name());
//                    k3cloudSynDao.save(outSaleEntity);
//                    shopAllot.setSaleCloudEntity(outSaleEntity);
//                }else{
//                    //都是寄售(直接调拨单)
//                    if(shopAllot.getFromShop().getDelegateDepot()!=null && shopAllot.getToShop().getDelegateDepot() !=null){
//                        K3CloudSynEntity outReturnEntity=new K3CloudSynEntity(K3CloudSave.K3CloudFormId.STK_TransferDirect.name(),shopAllot.getTransferDirect(),shopAllot.getId(),shopAllot.getFormatId(), K3CloudSynEntity.ExtendType.门店调拨.name());
//                        k3cloudSynDao.save(outReturnEntity);
//                        shopAllot.setReturnCloudEntity(outReturnEntity);
//                    }else{
//                        //调前为寄售 出库单
//                        if(shopAllot.getFromShop().getDelegateDepot()!=null){
//                            K3CloudSynEntity outSaleEntity =new K3CloudSynEntity(K3CloudSaveExtend.K3CloudFormId.SAL_OUTSTOCK.name(),K3CloudSave.K3CloudFormId.AR_receivable.name(), shopAllot.getSaleOutstock(),shopAllot.getId(),shopAllot.getFormatId(), K3CloudSynEntity.ExtendType.门店调拨.name());
//                            k3cloudSynDao.save(outSaleEntity);
//                            shopAllot.setSaleCloudEntity(outSaleEntity);
//                        }else{
//                            //调后为寄售  退货单
//                            K3CloudSynEntity outReturnEntity =new K3CloudSynEntity(K3CloudSaveExtend.K3CloudFormId.SAL_RETURNSTOCK.name(),K3CloudSave.K3CloudFormId.AR_receivable.name(), shopAllot.getReturnStock(),shopAllot.getId(),shopAllot.getFormatId(), K3CloudSynEntity.ExtendType.门店调拨.name());
//                            k3cloudSynDao.save(outReturnEntity);
//                            shopAllot.setReturnCloudEntity(outReturnEntity);
//                        }
//                    }
//                }
//                shopAllotDao.save(shopAllot);

//            shopAllotDao.save(shopAllot);
        }

// if(shopAllotAuditForm.getSyn()){
////            k3cloudSynService.syn(shopAllot.getId(), K3CloudSynEntity.ExtendType.门店调拨.name());
//    }

    }
}
