package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.modules.input.dto.*;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveQuery;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.repository.ClientRepository;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.PricesystemDetailRepository;
import net.myspring.future.modules.crm.dto.StoreAllotDto;
import net.myspring.future.modules.layout.domain.ShopAllot;
import net.myspring.future.modules.layout.domain.ShopAllotDetail;
import net.myspring.future.modules.layout.dto.ShopAllotDetailDto;
import net.myspring.future.modules.layout.dto.ShopAllotDto;
import net.myspring.future.modules.layout.repository.ShopAllotDetailRepository;
import net.myspring.future.modules.layout.repository.ShopAllotRepository;
import net.myspring.future.modules.layout.web.form.ShopAllotAuditForm;
import net.myspring.future.modules.layout.web.form.ShopAllotDetailAuditForm;
import net.myspring.future.modules.layout.web.form.ShopAllotDetailForm;
import net.myspring.future.modules.layout.web.form.ShopAllotForm;
import net.myspring.future.modules.layout.web.query.ShopAllotQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ShopAllotService {
    @Autowired
    private ShopAllotRepository shopAllotRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private CloudClient cloudClient;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ShopAllotDetailRepository shopAllotDetailRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PricesystemDetailRepository pricesystemDetailRepository;

    public Page<ShopAllotDto> findPage(Pageable pageable, ShopAllotQuery shopAllotQuery) {
        Page<ShopAllotDto> page = shopAllotRepository.findPage(pageable, shopAllotQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public String checkShop(String fromShopId,String toShopId){

        StringBuilder sb = new StringBuilder();

        for(String shopId : Lists.newArrayList(fromShopId, toShopId)){
            if(StringUtils.isNotBlank(shopId)){
                Depot shop = depotRepository.findOne(shopId);
                if(StringUtils.isBlank(shop.getPricesystemId())) {
                    sb.append(shop.getName()).append("没有绑定价格体系；");
                }
                if(StringUtils.isBlank(shop.getClientId()) || StringUtils.isBlank(clientRepository.findOne(shop.getClientId()).getOutCode()) ){
                    sb.append(shop.getName()).append("没有和财务关联；");
                }
            }
        }

        if(StringUtils.isNotBlank(fromShopId) && StringUtils.isNotBlank(toShopId) && fromShopId.equals(toShopId)){
            sb.append("调拨前后门店不能相同；");
        }
        return sb.toString();
    }

    @Transactional
    public void logicDelete(String id) {
        shopAllotRepository.logicDelete(id);
    }

    public ShopAllotDto findDto(String id) {
        return shopAllotRepository.findShopAllotDto(id);
    }

    @Transactional
    public ShopAllot save(ShopAllotForm shopAllotForm) {
        ShopAllot shopAllot;
        if(shopAllotForm.isCreate()){
            shopAllot = new ShopAllot();
            shopAllot.setFromShopId(shopAllotForm.getFromShopId());
            shopAllot.setToShopId(shopAllotForm.getToShopId());
            shopAllot.setRemarks(shopAllotForm.getRemarks());
            LocalDate now = LocalDate.now();
            shopAllot.setBusinessId(IdUtils.getNextBusinessId(shopAllotRepository.findMaxBusinessId(now.atStartOfDay()), now));
            shopAllot.setStatus(AuditStatusEnum.申请中.name());

            shopAllotRepository.save(shopAllot);

            batchSaveShopAllotDetails(shopAllotForm.getShopAllotDetailList(), shopAllot);
        }else{
            shopAllot = shopAllotRepository.findOne(shopAllotForm.getId());
            shopAllot.setRemarks( shopAllotForm.getRemarks());
            shopAllotRepository.save(shopAllot);


            batchSaveShopAllotDetails(shopAllotForm.getShopAllotDetailList(), shopAllot);
        }

        return shopAllot;
    }

private void batchSaveShopAllotDetails(List<ShopAllotDetailForm> shopAllotDetailFormList, ShopAllot shopAllot) {

        Map<String, PricesystemDetail> fromPricesystemMap = CollectionUtil.extractToMap(pricesystemDetailRepository.findByDepotId(shopAllot.getFromShopId()),"productId");
        Map<String, PricesystemDetail> toPricesystemMap = CollectionUtil.extractToMap(pricesystemDetailRepository.findByDepotId(shopAllot.getToShopId()),"productId");

        List<ShopAllotDetail> shopAllotDetailsToBeSaved = new ArrayList<>();
        for(ShopAllotDetailForm shopAllotDetailForm : shopAllotDetailFormList){
            ShopAllotDetail shopAllotDetail;
            if(StringUtils.isBlank(shopAllotDetailForm.getId())){
                shopAllotDetail = new ShopAllotDetail();
                shopAllotDetail.setProductId(shopAllotDetailForm.getProductId());
                shopAllotDetail.setShopAllotId(shopAllot.getId());
                shopAllotDetail.setReturnPrice(fromPricesystemMap.get(shopAllotDetail.getProductId()).getPrice());
                shopAllotDetail.setSalePrice(toPricesystemMap.get(shopAllotDetail.getProductId()).getPrice());
            }else{
                shopAllotDetail = shopAllotDetailRepository.findOne(shopAllotDetailForm.getId());
            }
            shopAllotDetail.setQty(shopAllotDetailForm.getQty()==null?0:shopAllotDetailForm.getQty());
            shopAllotDetailsToBeSaved.add(shopAllotDetail);
        }

        shopAllotDetailRepository.save(shopAllotDetailsToBeSaved);

    }

    @Transactional
    public void audit(ShopAllotAuditForm shopAllotAuditForm) {
//        Account account = AccountUtils.getAccount();
//        if(account.getOutId()==null){
//            return new Message("message_shop_allot_no_finance_message_account",Message.Type.danger);
//        }

        ShopAllot shopAllot = shopAllotRepository.findOne(shopAllotAuditForm.getId());
        shopAllot.setStatus(shopAllotAuditForm.getPass() ? AuditStatusEnum.已通过.name() : AuditStatusEnum.未通过.name());
        shopAllot.setAuditRemarks(shopAllotAuditForm.getAuditRemarks());
        shopAllot.setAuditBy(RequestUtils.getAccountId());
        shopAllot.setAuditDate(LocalDateTime.now());
        shopAllotRepository.save(shopAllot);

        if( !shopAllotAuditForm.getPass()) {
            return;
        }

        if(shopAllotAuditForm.getShopAllotDetailList()!=null){
            for(ShopAllotDetailAuditForm each : shopAllotAuditForm.getShopAllotDetailList()){
                ShopAllotDetail  shopAllotDetail = shopAllotDetailRepository.findOne(each.getId());
                shopAllotDetail.setReturnPrice(each.getReturnPrice());
                shopAllotDetail.setSalePrice(each.getSalePrice());
                shopAllotDetailRepository.save(shopAllotDetail);
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

    public List<ShopAllotDetailDto> findDetailListForViewOrAudit(String id) {

        List<ShopAllotDetailDto> result = shopAllotDetailRepository.getShopAllotDetailListForViewOrAudit(id);
        cacheUtils.initCacheInput(result);

        return result;
    }

    public ShopAllotDto findDtoForViewOrAudit(String id) {
        ShopAllotDto shopAllotDto = findDto(id);

        if(AuditStatusEnum.申请中.name().equals(shopAllotDto.getStatus())) {
            List<String> customerIdList = new ArrayList<>();
            if(StringUtils.isNotBlank(shopAllotDto.getFromShopClientOutId())){
                customerIdList.add(shopAllotDto.getFromShopClientOutId());
            }
            if(StringUtils.isNotBlank(shopAllotDto.getToShopClientOutId())){
                customerIdList.add(shopAllotDto.getToShopClientOutId());
            }
            if(CollectionUtil.isEmpty(customerIdList)){
                return shopAllotDto ;
            }

            CustomerReceiveQuery customerReceiveQuery = new CustomerReceiveQuery();
            customerReceiveQuery.setDateStart(LocalDate.now());
            customerReceiveQuery.setDateEnd(customerReceiveQuery.getDateStart());
            customerReceiveQuery.setCustomerIdList(customerIdList);

            List<CustomerReceiveDto> customerReceiveDtoList = cloudClient.getCustomerReceiveList(customerReceiveQuery);
            Map<String, CustomerReceiveDto> customerReceiveDtoMap = CollectionUtil.extractToMap(customerReceiveDtoList,"customerId");

            if(StringUtils.isNotBlank(shopAllotDto.getFromShopClientOutId())){
                shopAllotDto.setFromShopShouldGet(customerReceiveDtoMap.get(shopAllotDto.getFromShopClientOutId()).getEndShouldGet());
            }

            if(StringUtils.isNotBlank(shopAllotDto.getToShopClientOutId())){
                shopAllotDto.setToShopShouldGet(customerReceiveDtoMap.get(shopAllotDto.getToShopClientOutId()).getEndShouldGet());
            }
        }
        return shopAllotDto;
    }

    public Map<String,Object> findTotalPrice(String id) {

        BigDecimal returnTotalPrice = BigDecimal.ZERO;
        BigDecimal saleTotalPrice = BigDecimal.ZERO;
        List<ShopAllotDetailDto> shopAllotDetailDtoList = shopAllotDetailRepository.getShopAllotDetailListForViewOrAudit(id);
        for(ShopAllotDetailDto shopAllotDetailDto : shopAllotDetailDtoList){
            returnTotalPrice = returnTotalPrice.add(shopAllotDetailDto.getReturnAmount());
            saleTotalPrice = saleTotalPrice.add(shopAllotDetailDto.getSaleAmount());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("returnTotalPrice",  returnTotalPrice);
        result.put("saleTotalPrice",  saleTotalPrice);
        result.put("chineseReturnTotalPrice",  StringUtils.getChineseMoney(returnTotalPrice));
        result.put("chineseSaleTotalPrice",  StringUtils.getChineseMoney(saleTotalPrice));

        return result;

    }
}
