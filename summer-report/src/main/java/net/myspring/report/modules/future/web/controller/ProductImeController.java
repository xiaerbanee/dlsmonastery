package net.myspring.report.modules.future.web.controller;


import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.common.enums.BoolEnum;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.enums.CompanyNameEnum;
import net.myspring.report.common.utils.RequestUtils;
import net.myspring.report.modules.future.client.OfficeClient;
import net.myspring.report.modules.future.dto.DepotDto;
import net.myspring.report.modules.future.dto.DepotReportDto;
import net.myspring.report.modules.future.enums.*;
import net.myspring.report.modules.future.service.DepotService;
import net.myspring.report.modules.future.service.DepotShopService;
import net.myspring.report.modules.future.service.ProductImeService;
import net.myspring.report.modules.future.web.query.ReportQuery;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.excel.SimpleExcelBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "crm/productIme")
public class ProductImeController {

    @Autowired
    private ProductImeService productImeService;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DepotService depotService;
    @Autowired
    private DepotShopService depotShopService;

    @RequestMapping(value = "productImeReport")
    public Map<String,Object> productImeReport(ReportQuery productImeSaleReportQuery){
        return productImeService.productImeReport(productImeSaleReportQuery);
    }

    @RequestMapping(value = "getReportQuery")
    public ReportQuery getReportQuery(ReportQuery reportQuery){
        reportQuery.getExtra().put("sumTypeList", SumTypeEnum.getList());
        reportQuery.getExtra().put("areaTypeList", AreaTypeEnum.getList());
        reportQuery.getExtra().put("townTypeList", TownTypeEnum.getList());
        reportQuery.getExtra().put("areaList",officeClient.findByOfficeRuleName(OfficeRuleEnum.办事处.name()));
        reportQuery.getExtra().put("outTypeList", ProductImeStockReportOutTypeEnum.getList());
        reportQuery.getExtra().put("boolMap",BoolEnum.getMap());
        reportQuery.getExtra().put("netTypeList", NetTypeEnum.getList());
        reportQuery.setSumType(ProductImeStockReportSumTypeEnum.区域.name());
        CompanyConfigCacheDto  companyConfigCacheDto = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.COMPANY_NAME.name());
        if(companyConfigCacheDto != null && CompanyNameEnum.WZOPPO.name().equals(companyConfigCacheDto.getValue())) {
            reportQuery.setOutType(ProductImeStockReportOutTypeEnum.核销.name());
        }else{
            reportQuery.setOutType(ProductImeStockReportOutTypeEnum.电子保卡.name());
        }
        reportQuery.setScoreType(true);
        return reportQuery;
    }

    @RequestMapping(value = "exportReport",method = RequestMethod.GET)
    public ModelAndView exportReport(ReportQuery reportQuery) throws IOException{
        String accountId= RequestUtils.getAccountId();
        reportQuery.setDepotIdList(depotService.findByAccountId(accountId).stream().map(DepotDto::getId).collect(Collectors.toList()));
        if("按串码".equals(reportQuery.getExportType())){
            reportQuery.setIsDetail(true);
        }
        reportQuery.setOfficeIds(officeClient.getChildOfficeIds(reportQuery.getOfficeId()));
        reportQuery.setOfficeId(null);
        List<DepotReportDto> depotReportList=depotShopService.getProductImeReportList(reportQuery);
        SimpleExcelBook simpleExcelBook = productImeService.getFolderFileId(depotReportList,reportQuery);
        ExcelView excelView = new ExcelView();
        return new ModelAndView(excelView,"simpleExcelBook",simpleExcelBook);
    }
}
