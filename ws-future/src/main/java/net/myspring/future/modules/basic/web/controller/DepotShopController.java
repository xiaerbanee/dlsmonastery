package net.myspring.future.modules.basic.web.controller;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.common.enums.*;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.*;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.DictEnumClient;
import net.myspring.future.modules.basic.client.DictMapClient;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.dto.DepotReportDetailDto;
import net.myspring.future.modules.basic.dto.DepotReportDto;
import net.myspring.future.modules.basic.dto.DepotShopDto;
import net.myspring.future.modules.basic.service.AdPricesystemService;
import net.myspring.future.modules.basic.service.ChainService;
import net.myspring.future.modules.basic.service.DepotShopService;
import net.myspring.future.modules.basic.service.PricesystemService;
import net.myspring.future.modules.basic.web.form.DepotForm;
import net.myspring.future.modules.basic.web.form.DepotShopForm;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.future.modules.basic.web.query.DepotStoreQuery;
import net.myspring.future.modules.crm.web.query.ReportQuery;
import net.myspring.future.modules.layout.web.query.ShopAdQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by liuj on 2017/5/12.
 */
@RestController
@RequestMapping(value = "basic/depotShop")
public class DepotShopController {

    @Autowired
    private DepotShopService depotShopService;
    @Autowired
    private ChainService chainService;
    @Autowired
    private AdPricesystemService adPricesystemService;
    @Autowired
    private PricesystemService pricesystemService;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DictEnumClient dictEnumClient;
    @Autowired
    private DictMapClient dictMapClient;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DepotShopDto> list(Pageable pageable, DepotQuery depotShopQuery){
        Page<DepotShopDto> page = depotShopService.findPage(pageable,depotShopQuery);
        return page;
    }
    @RequestMapping(value = "getQuery")
    public DepotQuery getQuery(DepotQuery depotShopQuery) {
        return depotShopQuery;
    }


    @RequestMapping(value = "save")
    public RestResponse list(DepotShopForm depotShopForm){
        depotShopService.save(depotShopForm);
        return new RestResponse("保存成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "saveDepot")
    public RestResponse saveDepot(DepotForm depotForm){
        depotShopService.saveDepot(depotForm);
        return new RestResponse("保存成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getForm")
    public DepotShopForm getForm(DepotShopForm depotShopForm){
        depotShopForm.getExtra().put("depotStoreTypeList",DepotStoreTypeEnum.getList());
        depotShopForm.getExtra().put("jointLevelList", JointLevelEnum.getList());
        depotShopForm.getExtra().put("areaList", dictMapClient.findByCategory(DictMapCategoryEnum.门店_地区属性.name()));
        depotShopForm.getExtra().put("channelList",dictMapClient.findByCategory((DictMapCategoryEnum.门店_渠道类型.name())));
        depotShopForm.getExtra().put("chainList",dictMapClient.findByCategory((DictMapCategoryEnum.门店_连锁属性.name())));
        depotShopForm.getExtra().put("salePointList",dictMapClient.findByCategory((DictMapCategoryEnum.门店_售点类型.name())));
        depotShopForm.getExtra().put("turnoverList",dictMapClient.findByCategory((DictMapCategoryEnum.门店_营业额分类.name())));
        depotShopForm.getExtra().put("shopAreaList",dictMapClient.findByCategory((DictMapCategoryEnum.门店_店面尺寸.name())));
        depotShopForm.getExtra().put("carrierList",dictMapClient.findByCategory((DictMapCategoryEnum.门店_运营商属性.name())));
        depotShopForm.getExtra().put("businessCenterList",dictMapClient.findByCategory((DictMapCategoryEnum.门店_核心商圈.name())));
        depotShopForm.getExtra().put("specialityStoreList",dictMapClient.findByCategory((DictMapCategoryEnum.门店_体验店类型.name())));
        depotShopForm.getExtra().put("shopMonthTotalList",dictEnumClient.findByCategory((DictEnumCategoryEnum.SHOP_MONTH_TOTAL.name())));
        return depotShopForm;
    }

    @RequestMapping(value = "findOne")
    public DepotShopDto findOne(String id){
        DepotShopDto depotShopDto = depotShopService.findOne(id);
        return depotShopDto;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id){
        depotShopService.logicDelete(id);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "findDepotForm")
    public DepotForm findDepotForm(DepotForm depotForm){
        depotForm.getExtra().put("adPricesystemList",adPricesystemService.findAllEnabled());
        depotForm.getExtra().put("pricesystemList",pricesystemService.findAllEnabled());
        depotForm.getExtra().put("chainList",chainService.findAllEnabled());
        return depotForm;
    }

    @RequestMapping(value = "findShop")
    public DepotDto findShop(String id){
        DepotDto depotDto = depotShopService.findShop(id);
        return depotDto;
    }

    @RequestMapping(value = "depotReportDate")
    public List<DepotReportDto> depotReport(ReportQuery reportQuery){
        List<DepotReportDto> list=depotShopService.setReportData(reportQuery);
        return list;
    }

    @RequestMapping(value = "getReportQuery")
    public ReportQuery getReportQuery(ReportQuery reportQuery){
        reportQuery.getExtra().put("typeList", ReportTypeEnum.getList());
        reportQuery.getExtra().put("areaTypeList", AreaTypeEnum.getList());
        reportQuery.getExtra().put("townTypeList",TownTypeEnum.getList());
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

    @RequestMapping(value = "depotReportDetail")
    public DepotReportDetailDto depotReportDetail(ReportQuery reportQuery){
        DepotReportDetailDto depotReportDetailDto=depotShopService.getReportDataDetail(reportQuery);
        return depotReportDetailDto;
    }
}
