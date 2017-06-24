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
@Transactional
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

    public void logicDelete(String id) {
        shopAllotRepository.logicDelete(id);
    }

    public ShopAllotDto findDto(String id) {
        return shopAllotRepository.findShopAllotDto(id);
    }

    public ShopAllot save(ShopAllotForm shopAllotForm) {
        ShopAllot shopAllot;
        if(shopAllotForm.isCreate()){
            shopAllot = new ShopAllot();
            shopAllot.setFromShopId(shopAllotForm.getFromShopId());
            shopAllot.setToShopId(shopAllotForm.getToShopId());
            shopAllot.setRemarks(shopAllotForm.getRemarks());
            shopAllot.setBusinessId(IdUtils.getNextBusinessId(shopAllotRepository.findMaxBusinessId(LocalDate.now().atStartOfDay())));
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

    //直接调拨单成功示例
    public KingdeeSynReturnDto synStkTransferDirectTest(StoreAllotDto storeAllotDto){
        StkTransferDirectDto transferDirectDto = new StkTransferDirectDto();
        transferDirectDto.setExtendId("2");
        transferDirectDto.setExtendType(ExtendTypeEnum.大库调拨.name());
        transferDirectDto.setNote("模拟测试");
        transferDirectDto.setDate(LocalDate.now());
        StkTransferDirectFBillEntryDto entryDto = new StkTransferDirectFBillEntryDto();
        entryDto.setQty(1);
        entryDto.setMaterialNumber("O4805804");//product.outCode
        entryDto.setSrcStockNumber("G001"); //调出仓库
        entryDto.setDestStockNumber("G020");//调入仓库
        transferDirectDto.getStkTransferDirectFBillEntryDtoList().add(entryDto);
        return cloudClient.synStkTransferDirect(transferDirectDto);
    }

    public KingdeeSynReturnDto synStkTransferDirect(ShopAllotDto shopAllotDto){
        StkTransferDirectDto transferDirectDto = new StkTransferDirectDto();
        transferDirectDto.setExtendId(shopAllotDto.getId());
        transferDirectDto.setExtendType(ExtendTypeEnum.门店调拨.name());
        transferDirectDto.setNote("");//getBusinessId()+"申："+getRemarks()+"审:"+getAuditRemarks()
        transferDirectDto.setDate(LocalDate.now());
        List<ShopAllotDetail> shopAllotDetailList = Lists.newArrayList();//开单关联的详细
        for (ShopAllotDetail shopAllotDetail : shopAllotDetailList) {
            if (shopAllotDetail.getQty() != null && shopAllotDetail.getQty() > 0) {
                StkTransferDirectFBillEntryDto entryDto = new StkTransferDirectFBillEntryDto();
                entryDto.setQty(shopAllotDetail.getQty());
                entryDto.setMaterialNumber("");
                entryDto.setSrcStockNumber(""); //调出仓库
                entryDto.setDestStockNumber("");//调入仓库
                entryDto.setNoteEntry("");//getBusinessId()+"申："+getRemarks()+"审:"+getAuditRemarks()
                transferDirectDto.getStkTransferDirectFBillEntryDtoList().add(entryDto);
            }
        }
        return cloudClient.synStkTransferDirect(transferDirectDto);
    }

    //销售出库单成功示例
    private List<KingdeeSynReturnDto> synSalOutStockTest(){
        List<SalOutStockDto> salOutStockDtoList = Lists.newArrayList();
        SalOutStockDto salOutStockDto = new SalOutStockDto();
        salOutStockDto.setExtendId("1");
        salOutStockDto.setExtendType(ExtendTypeEnum.门店调拨.name());
        salOutStockDto.setDate(LocalDate.now());
        salOutStockDto.setCustomerNumber("00001");
        salOutStockDto.setNote("模拟测试");

        List<SalOutStockFEntityDto> entityDtoList = Lists.newArrayList();
        SalOutStockFEntityDto entityDto = new SalOutStockFEntityDto();
        entityDto.setStockNumber("G00201");
        entityDto.setMaterialNumber("05YF");//其他收入费用类的物料
        entityDto.setQty(1);
        entityDto.setPrice(BigDecimal.TEN);
        entityDto.setEntryNote("模拟测试");
        entityDtoList.add(entityDto);
        salOutStockDto.setSalOutStockFEntityDtoList(entityDtoList);
        salOutStockDtoList.add(salOutStockDto);
        return cloudClient.synSalOutStock(salOutStockDtoList);

    }

    private List<KingdeeSynReturnDto> synSalOutStock(ShopAllotDto shopAllotDto){
        List<SalOutStockDto> salOutStockDtoList = Lists.newArrayList();
        SalOutStockDto salOutStockDto = new SalOutStockDto();
        salOutStockDto.setExtendId("");
        salOutStockDto.setExtendType(ExtendTypeEnum.门店调拨.name());
        salOutStockDto.setDate(LocalDate.now());
        salOutStockDto.setCustomerNumber("00001");
        salOutStockDto.setNote("模拟测试");

        List<SalOutStockFEntityDto> entityDtoList = Lists.newArrayList();
        List<ShopAllotDetail> shopAllotDetailList = Lists.newArrayList();//开单关联的详细
        for (ShopAllotDetail shopAllotDetail : shopAllotDetailList) {
            if (shopAllotDetail.getQty() != null && shopAllotDetail.getQty() > 0) {
                SalOutStockFEntityDto entityDto = new SalOutStockFEntityDto();
                entityDto.setStockNumber("G00201");
                entityDto.setMaterialNumber("05YF");//其他收入费用类的物料
                entityDto.setQty(null);
                entityDto.setPrice(null);
                entityDto.setEntryNote("");
                entityDtoList.add(entityDto);
                salOutStockDto.setSalOutStockFEntityDtoList(entityDtoList);
                salOutStockDtoList.add(salOutStockDto);
            }
        }
        return cloudClient.synSalOutStock(salOutStockDtoList);

    }

    //销售退货单成功示例
    private List<KingdeeSynReturnDto> synSalReturnStockTest(ShopAllotDto shopAllotDto){
        List<SalReturnStockDto> salReturnStockDtoList = Lists.newArrayList();
        SalReturnStockDto returnStockDto = new SalReturnStockDto();
        returnStockDto.setExtendId("1");
        returnStockDto.setExtendType(ExtendTypeEnum.门店调拨.name());
        returnStockDto.setDate(LocalDate.now());
        returnStockDto.setCustomerNumber("00001");
        returnStockDto.setNote("模拟测试");
        List<SalReturnStockFEntityDto> entityDtoList = Lists.newArrayList();
        SalReturnStockFEntityDto entityDto = new SalReturnStockFEntityDto();
        entityDto.setMaterialNumber("05YF");//其他收入费用类的物料
        entityDto.setQty(1);
        entityDto.setPrice(BigDecimal.TEN);
        entityDto.setEntryNote("模拟测试");
        entityDto.setStockNumber("CK002");
        entityDtoList.add(entityDto);
        returnStockDto.setSalReturnStockFEntityDtoList(entityDtoList);
        salReturnStockDtoList.add(returnStockDto);
        return cloudClient.synSalReturnStock(salReturnStockDtoList);
    }

    private List<KingdeeSynReturnDto> synSalReturnStock(ShopAllotDto shopAllotDto){
        List<SalReturnStockDto> salReturnStockDtoList = Lists.newArrayList();
        SalReturnStockDto returnStockDto = new SalReturnStockDto();
        returnStockDto.setExtendId(shopAllotDto.getId());
        returnStockDto.setExtendType(ExtendTypeEnum.门店调拨.name());
        returnStockDto.setDate(LocalDate.now());
        returnStockDto.setCustomerNumber("");//shopAllotDto.getFromShop().getRealCode()
        returnStockDto.setNote("");//shopAllotDto.getBusinessId()+"申："+shopAllotDto.getRemarks()+"审:"+shopAllotDto.getAuditRemarks()
        List<SalReturnStockFEntityDto> entityDtoList = Lists.newArrayList();
        SalReturnStockFEntityDto entityDto = new SalReturnStockFEntityDto();
        entityDto.setMaterialNumber("");// shopAllotDetail.getProduct().getCode()
        entityDto.setQty(1);//shopAllotDetail.getQty()
        entityDto.setPrice(null);//shopAllotDetail.getReturnPrice()
        entityDto.setEntryNote("");//shopAllotDto.getBusinessId()+"申："+shopAllotDto.getRemarks()+"审:"+shopAllotDto.getAuditRemarks()
        entityDto.setStockNumber("");//getToShop().getDelegateDepot()==null?getStore().getRealCode():getToShop().getDelegateDepot().getRealCode()
        entityDtoList.add(entityDto);
        returnStockDto.setSalReturnStockFEntityDtoList(entityDtoList);
        salReturnStockDtoList.add(returnStockDto);
        return cloudClient.synSalReturnStock(salReturnStockDtoList);
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
            customerReceiveQuery.setDateStart(LocalDate.now().toString());
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
