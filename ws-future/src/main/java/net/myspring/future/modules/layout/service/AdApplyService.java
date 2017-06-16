package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.DictEnumDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.basic.web.form.DepotAdApplyForm;
import net.myspring.future.modules.basic.web.form.ProductAdForm;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.repository.ExpressOrderRepository;
import net.myspring.future.modules.layout.domain.AdApply;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;
import net.myspring.future.modules.layout.dto.AdApplyDto;
import net.myspring.future.modules.layout.repository.AdApplyRepository;
import net.myspring.future.modules.layout.repository.AdGoodsOrderDetailRepository;
import net.myspring.future.modules.layout.repository.AdGoodsOrderRepository;
import net.myspring.future.modules.layout.web.form.AdApplyBillForm;
import net.myspring.future.modules.layout.web.form.AdApplyDetailForm;
import net.myspring.future.modules.layout.web.form.AdApplyForm;
import net.myspring.future.modules.layout.web.form.AdApplyGoodsForm;
import net.myspring.future.modules.layout.web.query.AdApplyQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.IdUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AdApplyService {
    @Autowired
    private AdApplyRepository adApplyRepository;
    @Autowired
    private AdGoodsOrderRepository adGoodsOrderRepository;
    @Autowired
    private AdGoodsOrderDetailRepository adGoodsOrderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ExpressOrderRepository expressOrderRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private RedisTemplate redisTemplate;

    public Page<AdApplyDto> findPage(Pageable pageable, AdApplyQuery adApplyQuery) {
        Page<AdApplyDto> page = adApplyRepository.findPage(pageable, adApplyQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public AdApplyDto findOne(String id){
        AdApplyDto adApplyDto;
        if(StringUtils.isBlank(id)){
            adApplyDto = new AdApplyDto();
        } else {
            AdApply adApply= adApplyRepository.findOne(id);
            adApplyDto = BeanUtil.map(adApply,AdApplyDto.class);
            cacheUtils.initCacheInput(adApplyDto);
        }
        return adApplyDto;
    }

    public AdApplyForm getForm(AdApplyForm adApplyForm){
        List<String> billTypes = new ArrayList<>();
        billTypes.add(BillTypeEnum.POP.name());
        billTypes.add(BillTypeEnum.配件赠品.name());
        adApplyForm.getExtra().put("billTypes",billTypes);
        return adApplyForm;
    }

    public AdApplyBillForm getBillForm(AdApplyBillForm adApplyBillForm){
        List<String> billTypes = new ArrayList<>();
        billTypes.add(BillTypeEnum.POP.name());
        billTypes.add(BillTypeEnum.配件赠品.name());
        adApplyBillForm.getExtra().put("billTypes",billTypes);
        if(adApplyBillForm.getBillType().equalsIgnoreCase(BillTypeEnum.POP.name())){
            adApplyBillForm.setStoreId(CompanyConfigUtil.findByCode(redisTemplate,RequestUtils.getCompanyId(),CompanyConfigCodeEnum.AD_DEFAULT_STORE_ID.name()).getValue());
        }
        if(adApplyBillForm.getBillType().equalsIgnoreCase(BillTypeEnum.配件赠品.name())){
            adApplyBillForm.setStoreId(CompanyConfigUtil.findByCode(redisTemplate,RequestUtils.getCompanyId(),CompanyConfigCodeEnum.DEFAULT_STORE_ID.name()).getValue());
        }
        return adApplyBillForm;
    }

    public List<AdApplyDto> findAdApplyList(String billType){
        List<String> outGroupIds = Lists.newArrayList();
        if(BillTypeEnum.POP.name().equalsIgnoreCase(billType)){
            outGroupIds = IdUtils.getIdList(CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.PRODUCT_POP_GROUP_IDS.name()).getValue());
        }
        if(BillTypeEnum.配件赠品.name().equalsIgnoreCase(billType)){
            outGroupIds = IdUtils.getIdList(CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.PRODUCT_GOODS_POP_GROUP_IDS.name()).getValue());
        }
        LocalDate dateStart = LocalDate.now().plusYears(-1);
        List<AdApplyDto> adApplyDtos = adApplyRepository.findByOutGroupIdAndDate(dateStart,outGroupIds);
        cacheUtils.initCacheInput(adApplyDtos);
        return adApplyDtos;
    }

    public AdApplyGoodsForm getAdApplyGoodsList(AdApplyGoodsForm adApplyGoodsForm){
        List<DepotAdApplyForm> depotAdApplyForms = BeanUtil.map(depotRepository.findByEnabledIsTrueAndAdShopIsTrueAndIsHiddenIsFalse(),DepotAdApplyForm.class);
        adApplyGoodsForm.setDepotAdApplyForms(depotAdApplyForms);
        return adApplyGoodsForm;
    }

    public void save(AdApplyForm adApplyForm){
        if(CollectionUtil.isEmpty(adApplyForm.getProductAdForms())){
            return;
        }
        List<AdApply> adApplyList = Lists.newArrayList();
        for(ProductAdForm productAdForm:adApplyForm.getProductAdForms()){
            Integer applyQty = productAdForm.getApplyQty();
            if(applyQty!=null&&applyQty>0){
                AdApply adApply = new AdApply();
                adApply.setApplyQty(applyQty);
                adApply.setConfirmQty(applyQty);
                adApply.setBilledQty(0);
                adApply.setLeftQty(applyQty);
                adApply.setShopId(adApplyForm.getShopId());
                adApply.setProductId(productAdForm.getId());
                adApply.setRemarks(adApplyForm.getRemarks());
                adApply.setExpiryDateRemarks(productAdForm.getExpiryDateRemarks());
                adApplyList.add(adApply);
            }
        }
        adApplyRepository.save(adApplyList);
    }

    public void billSave(AdApplyBillForm adApplyBillForm){

        List<String> adApplyId  =CollectionUtil.extractToList(adApplyBillForm.getAdApplyDetailForms(),"id");
        Map<String,AdApplyDetailForm> adApplyDetailFormMap = CollectionUtil.extractToMap(adApplyBillForm.getAdApplyDetailForms(),"id");
        List<AdApply> adApplyList = adApplyRepository.findAll(adApplyId);
        Map<String,AdGoodsOrder> adGoodsOrderMap = Maps.newHashMap();
        List<AdGoodsOrderDetail> adGoodsOrderDetails = Lists.newArrayList();
        List<AdGoodsOrder> adGoodsOrders = Lists.newArrayList();
        if(adApplyList == null){
            return;
        }

        //生成AdGoodsOrder
        for(AdApply adApply:adApplyList){
            if(!adGoodsOrderMap.containsKey(adApply.getShopId())){
                AdGoodsOrder adGoodsOrder = new AdGoodsOrder();
                adGoodsOrder.setBillType(adApplyBillForm.getBillType());
                adGoodsOrder.setStoreId(adApplyBillForm.getStoreId());
                adGoodsOrder.setOutShopId(adApply.getShopId());
                adGoodsOrder.setShopId(adApply.getShopId());
                adGoodsOrder.setBillDate(adApplyBillForm.getBillDate());
                adGoodsOrder.setLocked(true);
                adGoodsOrder.setBillRemarks(adApplyBillForm.getRemarks());
                adGoodsOrderMap.put(adApply.getShopId(), adGoodsOrder);
                adGoodsOrderRepository.save(adGoodsOrder);
            }
            AdGoodsOrder adGoodsOrder = adGoodsOrderMap.get(adApply.getShopId());
            adGoodsOrders.add(adGoodsOrder);
            Product product = productRepository.findOne(adApply.getProductId());
            AdGoodsOrderDetail adGoodsOrderDetail = new AdGoodsOrderDetail();
            adGoodsOrderDetail.setAdGoodsOrderId(adGoodsOrder.getId());
            adGoodsOrderDetail.setProductId(adApply.getProductId());
            adGoodsOrderDetail.setPrice(product.getPrice2());
            adGoodsOrderDetail.setShouldGet(product.getShouldGet());
            adGoodsOrderDetail.setQty(adApply.getApplyQty());
            adGoodsOrderDetail.setConfirmQty(adApply.getConfirmQty());
            adGoodsOrderDetail.setBillQty(adApplyDetailFormMap.get(adApply.getId()).getNowBilledQty());
            adGoodsOrderDetails.add(adGoodsOrderDetail);
        }
        adGoodsOrderDetailRepository.save(adGoodsOrderDetails);

        //自动开单
        String maxBusinessId = adGoodsOrderRepository.findMaxBusinessId(adApplyBillForm.getBillDate());
        for(AdGoodsOrder adGoodsOrder:adGoodsOrders){
            adGoodsOrder.setBusinessId(IdUtils.getNextBusinessId(maxBusinessId));
            BigDecimal amount = BigDecimal.ZERO;
            List<AdGoodsOrderDetail> adGoodsOrderDetailLists = adGoodsOrderDetailRepository.findByAdGoodsOrderId(adGoodsOrder.getId());
            for(AdGoodsOrderDetail adGoodsOrderDetail:adGoodsOrderDetailLists){
                amount = amount.add(adGoodsOrderDetail.getPrice().multiply(new BigDecimal(adGoodsOrderDetail.getBillQty())));
            }
            adGoodsOrder.setAmount(amount);
            adGoodsOrder.setProcessStatus(GoodsOrderStatusEnum.待发货.name());

            Depot depot = depotRepository.findOne(adGoodsOrder.getShopId());
            ExpressOrder expressOrder = new ExpressOrder();
            expressOrder.setExtendBusinessId(adGoodsOrder.getBusinessId());
            expressOrder.setExtendType(ExpressOrderTypeEnum.物料订单.name());
            expressOrder.setExtendId(adGoodsOrder.getId());
            expressOrder.setFromDepotId(adGoodsOrder.getStoreId());
            expressOrder.setExpressCompanyId(adApplyBillForm.getExpressCompanyId());
            expressOrder.setToDepotId(adGoodsOrder.getShopId());
            expressOrder.setContator(depot.getContator());
            expressOrder.setAddress(depot.getAddress());
            expressOrder.setMobilePhone(depot.getMobilePhone());
            expressOrder.setPrintDate(adApplyBillForm.getBillDate());
            if(BillTypeEnum.POP.name().equalsIgnoreCase(adApplyBillForm.getBillType())){
                expressOrder.setExpressPrintQty(0);
            }else{
                expressOrder.setExpressPrintQty(1);
            }

            expressOrderRepository.save(expressOrder);
            adGoodsOrder.setExpressOrderId(expressOrder.getId());
            adGoodsOrderRepository.save(adGoodsOrder);
        }

        //保存adApply
        List<AdApply> newAdApplys = Lists.newArrayList();
        for(AdApply adApply:adApplyList){
            AdGoodsOrder adGoodsOrder = adGoodsOrderMap.get(adApply.getShopId());
            AdApplyDetailForm adApplyDetailForm = adApplyDetailFormMap.get(adApply.getId());
            adApply.setBilledQty(adApply.getBilledQty()+adApplyDetailForm.getNowBilledQty());
            if(StringUtils.isBlank(adApply.getOrderId())){
                adApply.setOrderId(adGoodsOrder.getId());
            }else{
                adApply.setOrderId(adApply.getOrderId()+ CharConstant.COMMA+adGoodsOrder.getId());
            }
            adApply.setLeftQty(adApply.getConfirmQty()-adApply.getBilledQty());
            newAdApplys.add(adApply);
        }
        adApplyRepository.save(newAdApplys);

        //TODO 调用金蝶接口
    }
}
