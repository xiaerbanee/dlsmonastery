package net.myspring.future.modules.basic.web.controller;
import net.myspring.common.enums.BoolEnum;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.*;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.dto.DepotReportDetailDto;
import net.myspring.future.modules.basic.dto.DepotShopDto;
import net.myspring.future.modules.basic.service.AdPricesystemService;
import net.myspring.future.modules.basic.service.ChainService;
import net.myspring.future.modules.basic.service.DepotShopService;
import net.myspring.future.modules.basic.service.PricesystemService;
import net.myspring.future.modules.basic.web.form.DepotForm;
import net.myspring.future.modules.basic.web.form.DepotShopForm;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.future.modules.basic.web.query.DepotReportQuery;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(method = RequestMethod.GET)
    public Page<DepotShopDto> list(Pageable pageable, DepotQuery depotShopQuery){
        Page<DepotShopDto> page = depotShopService.findPage(pageable,depotShopQuery);
        return page;
    }

    @RequestMapping(value = "save")
    public RestResponse list(DepotShopForm depotShopForm){
        depotShopService.save(depotShopForm);
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "saveDepot")
    public RestResponse saveDepot(DepotForm depotForm){
        depotShopService.saveDepot(depotForm);
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "getForm")
    public DepotShopForm getForm(DepotShopForm depotShopForm){
        depotShopForm = depotShopService.getForm(depotShopForm);
        depotShopForm.setTownTypeList(TownTypeEnum.getList());
        return depotShopForm;
    }

    @RequestMapping(value = "findDepotForm")
    public DepotForm findDepotForm(DepotForm depotForm){
        depotForm = depotShopService.findDepotForm(depotForm);
        depotForm.setAdPricesystemList(adPricesystemService.findAllEnabled());
        depotForm.setPricesystemList(pricesystemService.findAllEnabled());
        depotForm.setChainList(chainService.findAllEnabled());
        return depotForm;
    }

    @RequestMapping(value = "depotReport")
    public Page<DepotShopDto> depotReport(Pageable pageable, DepotReportQuery depotReportQuery){
        DepotQuery depotQuery=BeanUtil.map(depotReportQuery,DepotQuery.class);
        Page<DepotShopDto> page=depotShopService.findPage(pageable,depotQuery);
        depotShopService.setReportData(page.getContent(), depotReportQuery);
        return page;
    }

    @RequestMapping(value = "getReportQuery")
    public DepotReportQuery getReportQuery(DepotReportQuery depotReportQuery){
        depotReportQuery.getExtra().put("typeList", ReportTypeEnum.getList());
        depotReportQuery.getExtra().put("areaTypeList", AreaTypeEnum.getList());
        depotReportQuery.getExtra().put("townTypeList",TownTypeEnum.getList());
        depotReportQuery.getExtra().put("outTypeList", OutTypeEnum.getList());
        depotReportQuery.getExtra().put("boolMap", BoolEnum.getMap());
        CompanyConfigCacheDto companyConfigCacheDto = CompanyConfigUtil.findByCode( redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.PRODUCT_NAME.name());
        if(companyConfigCacheDto != null && "WZOPPO".equals(companyConfigCacheDto.getValue())) {
            depotReportQuery.setOutType(ProductImeStockReportOutTypeEnum.核销.name());
        }else{
            depotReportQuery.setOutType(ProductImeStockReportOutTypeEnum.电子保卡.name());
        }
        depotReportQuery.setType(ReportTypeEnum.核销.name());
        depotReportQuery.setScoreType(true);
        return depotReportQuery;
    }

    @RequestMapping(value = "depotReportDetail")
    public DepotReportDetailDto depotReportDetail(DepotReportQuery depotReportQuery){
        DepotReportDetailDto depotReportDetailDto=depotShopService.getReportDataDetail(depotReportQuery);
        return depotReportDetailDto;
    }
}
