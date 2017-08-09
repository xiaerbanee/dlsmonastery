package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.modules.kingdee.domain.BdStock;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.Client;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.dto.DepotReportDto;
import net.myspring.future.modules.basic.dto.DepotStoreDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.repository.ClientRepository;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotStoreRepository;
import net.myspring.future.modules.basic.web.form.DepotStoreForm;
import net.myspring.future.modules.basic.web.query.DepotStoreQuery;
import net.myspring.future.modules.crm.web.query.ReportQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.*;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/5/12.
 */
@Service
@Transactional(readOnly = true)
public class DepotStoreService {
    @Autowired
    private DepotStoreRepository depotStoreRepository;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private CloudClient cloudClient;
    @Autowired
    private ClientRepository clientRepository;

    public Page<DepotStoreDto> findPage(Pageable pageable, DepotStoreQuery depotStoreQuery){
        depotStoreQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
        Page<DepotStoreDto> page=depotStoreRepository.findPage(pageable,depotStoreQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<DepotStoreDto> findFilter(DepotStoreQuery depotStoreQuery){
        List<DepotStoreDto> list=depotStoreRepository.findFilter(depotStoreQuery);
        cacheUtils.initCacheInput(list);
        return list;
    }

    public DepotStoreDto findOne(DepotStoreDto depotStoreDto) {
        if(!depotStoreDto.isCreate()) {
            DepotStore depotStore =depotStoreRepository.findOne(depotStoreDto.getId());
            depotStoreDto= BeanUtil.map(depotStore,DepotStoreDto.class);
            cacheUtils.initCacheInput(depotStoreDto);
        }
        return depotStoreDto;
    }

    @Transactional
    public DepotStore save(DepotStoreForm depotStoreForm) {
        DepotStore depotStore;
        //保存仓库
        if(depotStoreForm.isCreate()) {
            depotStoreForm.setDepotId(depotStoreForm.getDepotId());
            depotStore = BeanUtil.map(depotStoreForm,DepotStore.class);
            depotStoreRepository.save(depotStore);
        } else {
            depotStore = depotStoreRepository.findOne(depotStoreForm.getId());
            ReflectionUtil.copyProperties(depotStoreForm,depotStore);
            depotStore.setDepotId(depotStoreForm.getDepotId());
            depotStoreRepository.save(depotStore);
        }
        //保存门店
        Depot depot = depotRepository.findOne(depotStoreForm.getDepotId());
        depot.setDepotStoreId(depotStore.getId());
        depot.setPopShop(depotStoreForm.getPopShop());
        depotRepository.save(depot);

        Client client = clientRepository.findOne(depot.getClientId());
        depotStore.setOutId(client.getOutId());
        depotStore.setOutCode(client.getOutCode());
        depotStore.setOutDate(client.getOutDate());
        depotStore.setOutGroupId(client.getOutGroupId());
        depotStore.setOutGroupName(client.getOutGroupName());
        depotStoreRepository.save(depotStore);
        return depotStore;
    }

    public Integer setReportData(List<DepotStoreDto> depotStoreList,ReportQuery reportQuery) {
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
        if(CollectionUtil.isNotEmpty(depotReportList)){
            for(DepotReportDto depotReport:depotReportList){
                String key=depotReport.getProductName();
                if(!map.containsKey(key)){
                    map.put(key,0);
                }
                map.put(key,map.get(key)+1);
            }
        }
        return map;
    }

    public SimpleExcelBook export(ReportQuery depotStoreQuery1,ReportQuery reportQuery) {
        //获取仓库报表基本信息
        DepotStoreQuery depotStoreQuery = BeanUtil.map(depotStoreQuery1, DepotStoreQuery.class);
        List<DepotStoreDto> depotStoreDtos = findFilter(depotStoreQuery);

        //获取仓库报表详细信息
        List<List<Object>> dataDetails = Lists.newArrayList();
        for (DepotStoreDto depotStoreDto : depotStoreDtos) {
            reportQuery.setDepotId(depotStoreDto.getDepotId());
            reportQuery.setIsDetail(true);
            Map<String, Integer> map = getReportDetail(reportQuery);
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                List<Object> row = Lists.newArrayList();
                row.add(depotStoreDto.getAreaName());
                row.add(depotStoreDto.getOfficeName());
                row.add(depotStoreDto.getDepotName());
                row.add(entry.getKey());
                row.add(entry.getValue());
                dataDetails.add(row);
            }
        }

        Workbook workbook = new SXSSFWorkbook(10000);
        List<List<SimpleExcelColumn>> excelColumnList = Lists.newArrayList();
        Map<String, CellStyle> cellStyleMap = ExcelUtils.getCellStyleMap(workbook);
        CellStyle headCellStyle = cellStyleMap.get(ExcelCellStyle.HEADER.name());
        CellStyle dataCellStyle = cellStyleMap.get(ExcelCellStyle.DATA.name());
        //设置excel header
        List<SimpleExcelColumn> headColumnList = Lists.newArrayList();
        headColumnList.add(new SimpleExcelColumn(headCellStyle, "办事处"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle, "业务单元"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle, "仓库"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle, "货品"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle, "数量"));
        excelColumnList.add(headColumnList);
        //填充 excel data
        for (List<Object> row : dataDetails) {
            List<SimpleExcelColumn> simpleExcelColumns = Lists.newArrayList();
            for (Object cell : row) {
                simpleExcelColumns.add(new SimpleExcelColumn(dataCellStyle, cell));
            }
            excelColumnList.add(simpleExcelColumns);
        }
        SimpleExcelSheet sheet = new SimpleExcelSheet("仓库报表", excelColumnList);
        ExcelUtils.doWrite(workbook, sheet);

        return new SimpleExcelBook(workbook, "仓库报表.xlsx", sheet);
    }

    public SimpleExcelBook exportDetail(ReportQuery reportQuery, ReportQuery depotStoreQuery1) {
        DepotStoreQuery depotStoreQuery = BeanUtil.map(depotStoreQuery1, DepotStoreQuery.class);
        List<DepotStoreDto> depotStoreDtos = findFilter(depotStoreQuery);

        List<List<Object>> tableData = Lists.newArrayList();
        for (DepotStoreDto depotStoreDto : depotStoreDtos) {
            reportQuery.setDepotId(depotStoreDto.getDepotId());
            reportQuery.setIsDetail(true);
            List<DepotReportDto> depotReportList = depotStoreRepository.findStoreReport(reportQuery);
            for (DepotReportDto depotReportDto : depotReportList) {
                List<Object> row = Lists.newArrayList();
                row.add(depotStoreDto.getAreaName());
                row.add(depotStoreDto.getOfficeName());
                row.add(depotStoreDto.getDepotName());
                row.add(depotReportDto.getProductName());
                row.add(depotReportDto.getIme());
                tableData.add(row);
            }
        }

        Workbook workbook = new SXSSFWorkbook(200000);
        List<List<SimpleExcelColumn>> excelColumnList = Lists.newArrayList();
        Map<String, CellStyle> cellStyleMap = ExcelUtils.getCellStyleMap(workbook);

        //设置excel头
        List<SimpleExcelColumn> headColumnList = Lists.newArrayList();
        CellStyle headCellStyle = cellStyleMap.get(ExcelCellStyle.HEADER.name());
        headColumnList.add(new SimpleExcelColumn(headCellStyle, "办事处"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle, "业务单元"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle, "仓库"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle, "货品"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle, "串码"));
        excelColumnList.add(headColumnList);

        //设置excel体
        CellStyle dataCellStyle = cellStyleMap.get(ExcelCellStyle.DATA.name());
        for (List<Object> row : tableData) {
            List<SimpleExcelColumn> dataColumnList = Lists.newArrayList();
            for (Object cell : row) {
                dataColumnList.add(new SimpleExcelColumn(dataCellStyle, cell));
            }
            excelColumnList.add(dataColumnList);
        }

        //设置sheet并将数据写入excel
        SimpleExcelSheet sheet = new SimpleExcelSheet("仓库串码明细", excelColumnList);
        ExcelUtils.doWrite(workbook, sheet);

        return new SimpleExcelBook(workbook, "仓库串码明细.xlsx", sheet);
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

    @Transactional
    public void logicDelete(String id) {
        DepotStore depotStore = depotStoreRepository.findOne(id);
        Depot depot = depotRepository.findOne(depotStore.getDepotId());
        depot.setDepotStoreId(null);
        depot.setPopShop(null);
        depotRepository.save(depot);
        depotStoreRepository.logicDelete(id);
    }

    @Transactional
    public void syn() {
        List<BdStock> bdstocks = cloudClient.getAllStock();
        if(CollectionUtil.isNotEmpty(bdstocks)){
            List<DepotStore> outIdDepotStoreList=depotStoreRepository.findByOutIdIn(CollectionUtil.extractToList(bdstocks,"FStockId"));
            Map<String,DepotStore> depotStoreMap=CollectionUtil.extractToMap(outIdDepotStoreList,"outId");
            List<Depot> depotList=depotRepository.findByNameList(CollectionUtil.extractToList(bdstocks,"FName"));
            Map<String,Depot> depotMap=CollectionUtil.extractToMap(depotList,"name");
            for(BdStock bdStock:bdstocks){
                DepotStore store=depotStoreMap.get(bdStock.getFStockId());
                if(store==null){
                    store=new DepotStore();
                }
                Depot depot=depotMap.get(bdStock.getFName());
                if(depot==null){
                    depot=new Depot();
                }
                store.setOutId(bdStock.getFStockId());
                store.setOutGroupId(bdStock.getFGroup());
                store.setOutGroupName(bdStock.getFGroupName());
                store.setOutDate(bdStock.getFModifyDate());
                store.setOutCode(bdStock.getFNumber());
                store.setEnabled(true);
                depotStoreRepository.save(store);
                depot.setName(bdStock.getFName());
                store.setEnabled(true);
                depot.setIsHidden(false);
                depot.setNamePinyin(StringUtils.getFirstSpell(depot.getName()));
                depot.setDepotStoreId(store.getId());
                depotRepository.save(depot);
                store.setDepotId(depot.getId());
                depotStoreRepository.save(store);
            }
        }
    }
}
