package net.myspring.report.modules.future.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.report.common.utils.CacheUtils;
import net.myspring.report.modules.future.client.OfficeClient;
import net.myspring.report.modules.future.dto.DepotReportDto;
import net.myspring.report.modules.future.dto.DepotStoreDto;
import net.myspring.report.modules.future.repository.DepotRepository;
import net.myspring.report.modules.future.repository.DepotStoreRepository;
import net.myspring.report.modules.future.web.query.DepotStoreQuery;
import net.myspring.report.modules.future.web.query.ReportQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by liuj on 2017/5/12.
 */
@Service
@Transactional(readOnly = true)
public class DepotStoreService {
    @Autowired
    private DepotStoreRepository depotStoreRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private OfficeClient officeClient;

    public List<DepotStoreDto> findFilter(DepotStoreQuery depotStoreQuery){
        return depotStoreRepository.findFilter(depotStoreQuery);
    }

    public Integer setReportData(List<DepotStoreDto> depotStoreList, ReportQuery reportQuery) {
        reportQuery.setDepotIds(CollectionUtil.extractToList(depotStoreList,"depotId"));
        List<DepotReportDto> depotReportList = depotStoreRepository.findStoreReport(reportQuery);
        Map<String,DepotReportDto> depotReportMap= CollectionUtil.extractToMap(depotReportList,"depotId");
        for(DepotStoreDto depotStore:depotStoreList){
            DepotReportDto depotReportDto=depotReportMap.get(depotStore.getDepotId());
            if(depotReportDto!=null){
                depotStore.setQty(depotReportDto.getQty());
            }
        }
        return setPercentage(depotStoreList);
    }

    public Map<String,Integer> getReportDetail(ReportQuery reportQuery) {
        Map<String,Integer> map= Maps.newHashMap();
        List<DepotReportDto> depotReportList = depotStoreRepository.findStoreReport(reportQuery);
        for(DepotReportDto depotReportDto:depotReportList){
            if(!map.containsKey(depotReportDto.getProductName())){
                map.put(depotReportDto.getProductName(),0);
            }
            map.put(depotReportDto.getProductName(),map.get(depotReportDto.getProductName())+1);
        }
        return map;
    }

    public SimpleExcelBook export(ReportQuery reportQuery) {
        Workbook workbook = new SXSSFWorkbook(10000);
        List<SimpleExcelColumn> simpleExcelColumns = Lists.newArrayList();
        simpleExcelColumns.add(new SimpleExcelColumn("areaName", "办事处"));
        simpleExcelColumns.add(new SimpleExcelColumn("officeName", "业务单元"));
        simpleExcelColumns.add(new SimpleExcelColumn("depotName", "仓库"));
        simpleExcelColumns.add(new SimpleExcelColumn("productName", "货品"));
        if("按串码".equals(reportQuery.getExportType())){
            reportQuery.setIsDetail(true);
            simpleExcelColumns.add(new SimpleExcelColumn("ime", "串码"));
        }else  if("按合计".equals(reportQuery.getExportType())){
            simpleExcelColumns.add(new SimpleExcelColumn("qty", "数量"));
        }
        List<DepotReportDto> report = depotStoreRepository.findStoreReport(reportQuery);
        cacheUtils.initCacheInput(report);
        SimpleExcelSheet sheet = new SimpleExcelSheet("仓库报表", report,simpleExcelColumns);
        ExcelUtils.doWrite(workbook, sheet);
        return new SimpleExcelBook(workbook, "仓库报表"+reportQuery.getExportType()+".xlsx", sheet);
    }

    private Integer setPercentage(List<DepotStoreDto> depotStoreList) {
        Integer sum = 0;
        for (DepotStoreDto depotStore : depotStoreList) {
            sum +=  depotStore.getQty();
        }
        for (DepotStoreDto depotStore : depotStoreList) {
            depotStore.setPercentage(StringUtils.division(sum, depotStore.getQty()));
        }
        return sum;
    }
}
