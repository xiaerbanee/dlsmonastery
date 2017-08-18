package net.myspring.report.modules.future.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.common.enums.BoolEnum;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.report.common.utils.RequestUtils;
import net.myspring.report.modules.future.client.OfficeClient;
import net.myspring.report.modules.future.dto.DepotDto;
import net.myspring.report.modules.future.dto.DepotStoreDto;
import net.myspring.report.modules.future.enums.OutTypeEnum;
import net.myspring.report.modules.future.enums.ProductImeStockReportOutTypeEnum;
import net.myspring.report.modules.future.enums.ReportTypeEnum;
import net.myspring.report.modules.future.service.DepotService;
import net.myspring.report.modules.future.service.DepotStoreService;
import net.myspring.report.modules.future.web.query.DepotStoreQuery;
import net.myspring.report.modules.future.web.query.ReportQuery;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @RequestMapping(value = "storeReport")
    public Map<String,Object> storeReport(ReportQuery reportQuery){
        Map<String,Object> map=Maps.newHashMap();
        String accountId= RequestUtils.getAccountId();
        reportQuery.setDepotIdList(depotService.findByAccountId(accountId).stream().map(DepotDto::getId).collect(Collectors.toList()));
        DepotStoreQuery depotStoreQuery = BeanUtil.map(reportQuery, DepotStoreQuery.class);
        List<DepotStoreDto> list = depotStoreService.findFilter(depotStoreQuery);
        map.put("sum",depotStoreService.setReportData(list,reportQuery));
        map.put("list",list);
        return map;
    }

    @RequestMapping(value = "storeReportDetail")
    public Map<String,Integer> storeReportDetail(ReportQuery reportQuery){
        return depotStoreService.getReportDetail(reportQuery);
    }


    @RequestMapping(value = "export")
    public ModelAndView export(ReportQuery reportQuery) {
        ReportQuery depotStoreQuery;
        depotStoreQuery = reportQuery;
        String accountId=RequestUtils.getAccountId();
        reportQuery.setDepotIdList(depotService.findByAccountId(accountId).stream().map(DepotDto::getId).collect(Collectors.toList()));
        return new ModelAndView(new ExcelView(), "simpleExcelBook", depotStoreService.export(reportQuery));
    }

    @RequestMapping(value = "getReportQuery")
    public ReportQuery getReportQuery(ReportQuery reportQuery) {
        reportQuery.getExtra().put("typeList", ReportTypeEnum.getList());
        reportQuery.getExtra().put("outTypeList", OutTypeEnum.getList());
        reportQuery.getExtra().put("boolMap", BoolEnum.getMap());
        CompanyConfigCacheDto companyConfigCacheDto = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.COMPANY_NAME.name());
        if (companyConfigCacheDto != null && "WZOPPO".equals(companyConfigCacheDto.getValue())) {
            reportQuery.setOutType(ProductImeStockReportOutTypeEnum.核销.name());
        } else {
            reportQuery.setOutType(ProductImeStockReportOutTypeEnum.电子保卡.name());
        }
        reportQuery.setType(ReportTypeEnum.核销.name());
        reportQuery.setScoreType(true);
        return reportQuery;
    }
}
