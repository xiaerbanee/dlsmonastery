package net.myspring.report.modules.future.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.sys.dto.OfficeRuleDto;
import net.myspring.report.modules.future.dto.DepotDto;
import net.myspring.report.modules.future.dto.ProductTypeDto;
import net.myspring.report.modules.future.repository.DepotRepository;
import net.myspring.report.modules.future.repository.ProductImeRepository;
import net.myspring.report.common.utils.CacheUtils;
import net.myspring.report.common.utils.RequestUtils;
import net.myspring.report.modules.future.client.OfficeClient;
import net.myspring.report.modules.future.client.OfficeRuleClient;
import net.myspring.report.modules.future.dto.DepotReportDto;
import net.myspring.report.modules.future.dto.ProductImeReportDto;
import net.myspring.report.modules.future.enums.OutTypeEnum;
import net.myspring.report.modules.future.enums.SumTypeEnum;
import net.myspring.report.modules.future.repository.ProductTypeRepository;
import net.myspring.report.modules.future.web.query.ReportQuery;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductImeService {

    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private ProductTypeRepository productTypeRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private OfficeRuleClient officeRuleClient;
    @Autowired
    private DepotRepository depotRepository;

    public Map<String,Object> productImeReport(ReportQuery reportQuery) {
        Map<String,Object> map=Maps.newHashMap();
        reportQuery.setDepotIdList(depotRepository.findByAccountId(RequestUtils.getAccountId()).stream().map(DepotDto::getId).collect(Collectors.toList()));
        Map<String, List<String>> lastRuleMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(reportQuery.getOfficeId())) {
            reportQuery.setOfficeIds(officeClient.getChildOfficeIds(reportQuery.getOfficeId()));
            lastRuleMap = officeClient.geMapByOfficeId(reportQuery.getOfficeId());
        }
        List<ProductImeReportDto> productImeSaleReportList = getProductImeReportList(reportQuery);
        Integer sum;
        if (StringUtils.isNotBlank(reportQuery.getOfficeId()) && SumTypeEnum.区域.name().equals(reportQuery.getSumType())) {
            Map<String, ProductImeReportDto> ProductImeReportMap = Maps.newHashMap();
            for (ProductImeReportDto productImeSaleReportDto : productImeSaleReportList) {
                String key = getOfficeKey(lastRuleMap, productImeSaleReportDto.getOfficeId());
                if (StringUtils.isNotBlank(key)) {
                    if (ProductImeReportMap.containsKey(key)) {
                        ProductImeReportMap.get(key).addQty(1);
                    } else {
                        ProductImeReportDto productImeSaleReport = new ProductImeReportDto(key, 1);
                        ProductImeReportMap.put(key, productImeSaleReport);
                    }
                }
            }
            List<String> filterOfficeIdList=RequestUtils.getOfficeIdList();
            for (String officeId : lastRuleMap.keySet()) {
                if (!ProductImeReportMap.containsKey(officeId)&&filterOfficeIdList.contains(officeId)) {
                    ProductImeReportMap.put(officeId, new ProductImeReportDto(officeId, 0));
                }
            }
            List<ProductImeReportDto> list = Lists.newArrayList(ProductImeReportMap.values());
            sum=setPercentage(list);
            cacheUtils.initCacheInput(list);
            map.put("list",list);
        } else {
            sum=setPercentage(productImeSaleReportList);
            cacheUtils.initCacheInput(productImeSaleReportList);
            map.put("list",productImeSaleReportList);
        }
        map.put("sum",sum);
        return map;
    }

    private String getOfficeKey(Map<String, List<String>> officeMap, String officeId) {
        for (Map.Entry<String, List<String>> entry : officeMap.entrySet()) {
            for (String childId : entry.getValue()) {
                if (childId.equals(officeId)) {
                    return entry.getKey();
                }
            }
        }
        return officeId;
    }

    private List<ProductImeReportDto> getProductImeReportList(ReportQuery reportQuery) {
        List<ProductImeReportDto> productImeReportList = Lists.newArrayList();
        if (OutTypeEnum.电子保卡.name().equals(reportQuery.getOutType())) {
            if ("销售报表".equals(reportQuery.getType())) {
                productImeReportList = productImeRepository.findBaokaSaleReport(reportQuery);
            } else if ("库存报表".equals(reportQuery.getType())) {
                productImeReportList = productImeRepository.findBaokaStoreReport(reportQuery);
            }
        } else {
            if ("销售报表".equals(reportQuery.getType())) {
                productImeReportList = productImeRepository.findSaleReport(reportQuery);
            } else if ("库存报表".equals(reportQuery.getType())) {
                productImeReportList = productImeRepository.findStoreReport(reportQuery);
            }
        }
        return productImeReportList;
    }

    private Integer setPercentage(List<ProductImeReportDto> productImeReportList) {
        Integer sum = 0;
        for (ProductImeReportDto productImeReportDto : productImeReportList) {
            sum = sum + productImeReportDto.getQty();
        }
        for (ProductImeReportDto productImeReportDto : productImeReportList) {
            productImeReportDto.setPercent(StringUtils.division(sum, productImeReportDto.getQty()));
        }
        return sum;
    }

    public SimpleExcelBook getFolderFileId(List<DepotReportDto> depotReportList, ReportQuery reportQuery) {
        Workbook workbook = new SXSSFWorkbook(10000);
        List<OfficeRuleDto> officeRuleList = officeRuleClient.findAll();
        Map<String,Map<String,String>> officeRuleMap=officeRuleClient.getOfficeRuleMap();
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        for(OfficeRuleDto officeRule:officeRuleList){
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "extra", officeRule.getName(), officeRule.getCode()));
            for(DepotReportDto depotReportDto:depotReportList){
                depotReportDto.getExtra().put(officeRule.getCode(),officeRuleMap.get(officeRule.getCode()).get(depotReportDto.getOfficeId()));
            }
        }
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "depotName", "门店名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "areaType", "区域类型"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "districtName", "地区名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "chainName", "连锁体系"));
        if ("按数量".equals(reportQuery.getExportType())) {
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productTypeName", "产品型号"));
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "qty", "数量"));
        } else if ("按串码".equals(reportQuery.getExportType())) {
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productTypeName", "产品型号"));
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productName", "产品名称"));
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "ime", "串码"));
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "employeeName", "核销人"));
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "employeePositionName", "岗位"));
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "saleDate", "核销时间"));
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "uploadDate", "上报时间"));
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "retailDate", "工厂注册时间"));
        } else if ("按合计".equals(reportQuery.getExportType())) {
            List<ProductTypeDto> productTypeList = productTypeRepository.findByScoreType(reportQuery.getScoreType());
            for (ProductTypeDto productType : productTypeList) {
                simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "extra", productType.getName(), productType.getName()));
            }
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "extra", "合计", "sum"));
            Map<String, DepotReportDto> map = Maps.newHashMap();
            for (DepotReportDto depotReport : depotReportList) {
                if (!map.containsKey(depotReport.getDepotId())) {
                    map.put(depotReport.getDepotId(), depotReport);
                }
                map.get(depotReport.getDepotId()).getExtra().put(depotReport.getProductTypeName(), depotReport.getQty());
            }
            depotReportList = Lists.newArrayList(map.values());
            Map<String, Object> sumMap = Maps.newHashMap();
            Integer totalSum = 0;
            for (DepotReportDto depotReport : depotReportList) {
                Integer sum = 0;
                for (ProductTypeDto productType : productTypeList) {
                    String key = productType.getName();
                    if (!depotReport.getExtra().containsKey(key)) {
                        depotReport.getExtra().put(key, 0);
                    }
                    if (!sumMap.containsKey(key)) {
                        sumMap.put(key, 0);
                    }
                    sumMap.put(key, (Integer) sumMap.get(key) + (Integer) depotReport.getExtra().get(key));
                    sum += (Integer) depotReport.getExtra().get(key);
                    totalSum += (Integer) depotReport.getExtra().get(key);
                }
                depotReport.getExtra().put("sum", sum);
            }
            DepotReportDto depotReportDto = new DepotReportDto();
            depotReportDto.setChainName("合计");
            depotReportDto.setExtra(sumMap);
            depotReportDto.getExtra().put("sum", totalSum);
            depotReportList.add(depotReportDto);
        }
        cacheUtils.initCacheInput(depotReportList);
        cacheUtils.initCacheInput(depotReportList);
        cacheUtils.initCacheInput(depotReportList);
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet(reportQuery.getType() + reportQuery.getExportType(), depotReportList, simpleExcelColumnList);
        ExcelUtils.doWrite(workbook,simpleExcelSheet);
        return new SimpleExcelBook(workbook, reportQuery.getType() + LocalDateUtils.format(LocalDate.now()) + ".xlsx", simpleExcelSheet);
    }
}
