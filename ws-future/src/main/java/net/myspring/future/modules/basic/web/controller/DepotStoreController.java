package net.myspring.future.modules.basic.web.controller;

import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.common.enums.*;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.DepotStoreTypeEnum;
import net.myspring.future.common.enums.OutTypeEnum;
import net.myspring.future.common.enums.ProductImeStockReportOutTypeEnum;
import net.myspring.future.common.enums.ReportTypeEnum;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.DictEnumClient;
import net.myspring.future.modules.basic.client.DictMapClient;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.dto.DepotStoreDto;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.basic.service.DepotStoreService;
import net.myspring.future.modules.basic.web.form.DepotStoreForm;
import net.myspring.future.modules.basic.web.query.DepotStoreQuery;
import net.myspring.future.modules.crm.web.query.ReportQuery;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by liuj on 2017/5/12.
 */
@RestController
@RequestMapping(value = "basic/depotStore")
public class DepotStoreController {

    @Autowired
    private DepotStoreService depotStoreService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private DepotService depotService;
    @Autowired
    private DictMapClient dictMapClient;
    @Autowired
    private DictEnumClient dictEnumClient;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DepotStoreDto> list(Pageable pageable, DepotStoreQuery depotStoreQuery){
        Page<DepotStoreDto> page = depotStoreService.findPage(pageable,depotStoreQuery);
        return page;
    }

    @RequestMapping(value = "findOne")
    public DepotStoreDto findOne(DepotStoreDto depotStoreDto){
        depotStoreDto=depotStoreService.findOne(depotStoreDto);
        return depotStoreDto;
    }

    @RequestMapping(value = "getForm")
    public DepotStoreForm getForm(DepotStoreForm depotStoreForm){
        depotStoreForm.getExtra().put("depotStoreTypeList",DepotStoreTypeEnum.getList());
        depotStoreForm.getExtra().put("jointLevelList", JointLevelEnum.getList());
        depotStoreForm.getExtra().put("areaList", dictMapClient.findByCategory(DictMapCategoryEnum.门店_地区属性.name()));
        depotStoreForm.getExtra().put("channelList",dictMapClient.findByCategory((DictMapCategoryEnum.门店_渠道类型.name())));
        depotStoreForm.getExtra().put("chainList",dictMapClient.findByCategory((DictMapCategoryEnum.门店_连锁属性.name())));
        depotStoreForm.getExtra().put("salePointList",dictMapClient.findByCategory((DictMapCategoryEnum.门店_售点类型.name())));
        depotStoreForm.getExtra().put("turnoverList",dictMapClient.findByCategory((DictMapCategoryEnum.门店_营业额分类.name())));
        depotStoreForm.getExtra().put("shopAreaList",dictMapClient.findByCategory((DictMapCategoryEnum.门店_店面尺寸.name())));
        depotStoreForm.getExtra().put("carrierList",dictMapClient.findByCategory((DictMapCategoryEnum.门店_运营商属性.name())));
        depotStoreForm.getExtra().put("businessCenterList",dictMapClient.findByCategory((DictMapCategoryEnum.门店_核心商圈.name())));
        depotStoreForm.getExtra().put("specialityStoreList",dictMapClient.findByCategory((DictMapCategoryEnum.门店_体验店类型.name())));
        depotStoreForm.getExtra().put("shopMonthTotalList",dictEnumClient.findByCategory((DictEnumCategoryEnum.SHOP_MONTH_TOTAL.name())));
        return depotStoreForm;
    }

    @RequestMapping(value = "save")
    public RestResponse save(DepotStoreForm depotStoreForm){
        depotStoreService.save(depotStoreForm);
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(DepotStoreForm depotStoreForm){
        depotStoreService.logicDelete(depotStoreForm.getId());
        return new RestResponse("删除成功",null);
    }

    @RequestMapping(value = "storeReport")
    public Page<DepotStoreDto> storeReport(Pageable pageable,ReportQuery reportQuery){
        reportQuery.setOfficeIdList(officeClient.getOfficeFilterIds(RequestUtils.getRequestEntity().getOfficeId()));
        reportQuery.setDepotIdList(depotService.filterDepotIds());
        DepotStoreQuery depotStoreQuery = BeanUtil.map(reportQuery, DepotStoreQuery.class);
        Page<DepotStoreDto> page = depotStoreService.findPage(pageable,depotStoreQuery);
        depotStoreService.setReportData(page.getContent(),reportQuery);
        return page;
    }

    @RequestMapping(value = "storeReportDetail")
    public Map<String,Integer> storeReportDetail(ReportQuery reportQuery){
        return depotStoreService.getReportDetail(reportQuery);
    }


    @RequestMapping(value = "getReportQuery")
    public ReportQuery getReportQuery(ReportQuery reportQuery){
        reportQuery.getExtra().put("typeList", ReportTypeEnum.getList());
        reportQuery.getExtra().put("outTypeList", OutTypeEnum.getList());
        reportQuery.getExtra().put("boolMap", BoolEnum.getMap());
        CompanyConfigCacheDto companyConfigCacheDto = CompanyConfigUtil.findByCode( redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.PRODUCT_NAME.name());
        if(companyConfigCacheDto != null && "WZOPPO".equals(companyConfigCacheDto.getValue())) {
            reportQuery.setOutType(ProductImeStockReportOutTypeEnum.核销.name());
        }else{
            reportQuery.setOutType(ProductImeStockReportOutTypeEnum.电子保卡.name());
        }
        reportQuery.setType(ReportTypeEnum.核销.name());
        reportQuery.setScoreType(true);
        return reportQuery;
    }

    @RequestMapping(value = "getQuery")
    public DepotStoreQuery getQuery(DepotStoreQuery depotStoreQuery){
        return depotStoreQuery;
    }

}
