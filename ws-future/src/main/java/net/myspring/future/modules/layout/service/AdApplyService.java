package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.layout.domain.AdApply;
import net.myspring.future.modules.layout.dto.AdApplyDto;
import net.myspring.future.modules.layout.repository.AdApplyRepository;
import net.myspring.future.modules.layout.web.form.AdApplyBillForm;
import net.myspring.future.modules.layout.web.form.AdApplyForm;
import net.myspring.future.modules.layout.web.query.AdApplyQuery;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.text.IdUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ProductRepository productRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private RedisTemplate redisTemplate;

    public Page<AdApplyDto> findPage(Pageable pageable, AdApplyQuery adApplyQuery) {
        Page<AdApplyDto> page = adApplyRepository.findPage(pageable, adApplyQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public AdApply findOne(String id){
        AdApply adApply = adApplyRepository.findOne(id);
        return adApply;
    }

    public AdApplyForm getForm(AdApplyForm adApplyForm){
        List<String> billTypes = new ArrayList<>();
        billTypes.add(BillTypeEnum.POP.name());
        billTypes.add(BillTypeEnum.配件赠品.name());
        adApplyForm.setBillTypes(billTypes);
        adApplyForm.setProductDtos(findProductList(adApplyForm.getBillType()));
        return adApplyForm;
    }

    public AdApplyBillForm getBillForm(AdApplyBillForm adApplyBillForm){
        List<String> billTypes = new ArrayList<>();
        billTypes.add(BillTypeEnum.POP.name());
        billTypes.add(BillTypeEnum.配件赠品.name());
        adApplyBillForm.setBillTypes(billTypes);
        if(adApplyBillForm.getBillType()==null){
            adApplyBillForm.setBillType(BillTypeEnum.POP.name());
        }
        if(adApplyBillForm.getBillType().equalsIgnoreCase(BillTypeEnum.POP.name())){
            adApplyBillForm.setStoreId(CompanyConfigUtil.findByCode(redisTemplate,RequestUtils.getCompanyId(),CompanyConfigCodeEnum.AD_DEFAULT_STORE_ID.name()).getValue());
            List<String> outGroupIds = IdUtils.getIdList(CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.PRODUCT_POP_GROUP_IDS.name()).getValue());
            adApplyBillForm.setAdApplyDtos(findAdApplyList(outGroupIds));
        }
        if(adApplyBillForm.getBillType().equalsIgnoreCase(BillTypeEnum.配件赠品.name())){
            adApplyBillForm.setStoreId(CompanyConfigUtil.findByCode(redisTemplate,RequestUtils.getCompanyId(),CompanyConfigCodeEnum.DEFAULT_STORE_ID.name()).getValue());
            List<String> outGroupIds = IdUtils.getIdList(CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.PRODUCT_GOODS_POP_GROUP_IDS.name()).getValue());
            adApplyBillForm.setAdApplyDtos(findAdApplyList(outGroupIds));
        }
        return adApplyBillForm;
    }

    public List<AdApplyDto> findAdApplyList(List<String> outGroupIds){
        LocalDate dateStart = LocalDate.now().plusYears(-1);
        List<AdApplyDto> adApplyDtos = adApplyRepository.findByOutGroupIdAndDate(dateStart,outGroupIds);
        cacheUtils.initCacheInput(adApplyDtos);
        return adApplyDtos;
    }

    public List<ProductDto> findProductList(String billType){
        List<ProductDto> productDtos = new ArrayList<>();
        if(billType ==null){
            billType = BillTypeEnum.POP.name();
        }
        if(billType.equalsIgnoreCase(BillTypeEnum.POP.name())){
            List<String> outGroupIds = IdUtils.getIdList(CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.PRODUCT_POP_GROUP_IDS.name()).getValue());
            productDtos = productRepository.findByOutGroupIdInAndAllowOrder(outGroupIds,true);
        }
        if(billType.equalsIgnoreCase(BillTypeEnum.配件赠品.name())){
            List<String> outGroupIds = IdUtils.getIdList(CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.PRODUCT_GOODS_POP_GROUP_IDS.name()).getValue());
            productDtos  = productRepository.findByOutGroupIdInAndAllowOrder(outGroupIds,true);
        }
        return productDtos;
    }

    public List<AdApply> findAdApplyGoodsList(){
        Map<String,Object> filter = Maps.newHashMap();
        filter.put("adShop",true);
        List<String> adApplyIdList = adApplyRepository.findAllId();
        List<AdApply> adApplys = Lists.newArrayList();

        return adApplys;
    }

    public void save(AdApplyForm adApplyForm){
        if(adApplyForm.getApplyQty()==null){
            return;
        }
        if(adApplyForm.getApplyQty().size()!=adApplyForm.getProductDtos().size()){
            return;
        }
        for(int i = 0;i<adApplyForm.getApplyQty().size();i++){
            String productId = adApplyForm.getProductDtos().get(i).getId();
            String expiryDateRemarks = adApplyForm.getProductDtos().get(i).getExpiryDateRemarks();
            Integer applyQty = adApplyForm.getApplyQty().get(i);
            if(applyQty!=null&&applyQty>0){
                AdApply adApply = new AdApply();
                adApply.setApplyQty(applyQty);
                adApply.setConfirmQty(applyQty);
                adApply.setBilledQty(0);
                adApply.setLeftQty(applyQty);
                adApply.setShopId(adApplyForm.getShopId());
                adApply.setProductId(productId);
                adApply.setRemarks(adApplyForm.getRemarks());
                adApply.setExpiryDateRemarks(expiryDateRemarks);
                adApplyRepository.save(adApply);
            }
        }
    }

    public Map<String,Object> findBillAdApplyMap(String billType){
        return null;
    }

    public List<SimpleExcelSheet> findSimpleExcelSheets(Workbook workbook, Map<String, Object> map) {
        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<AdApply> adApplyList = adApplyRepository.findByFilter(map);
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "id", "编码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shop.name", "门店"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "product.code", "物料编码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "product.name", "物料名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "applyQty", "申请数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "confirmQty", "确认数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "leftQty", "待开单数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "created.loginName", "创建人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "expiryDateRemarks", "截止日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("征订明细", adApplyList, simpleExcelColumnList);
        simpleExcelSheetList.add(simpleExcelSheet);
        return simpleExcelSheetList;
    }
}
