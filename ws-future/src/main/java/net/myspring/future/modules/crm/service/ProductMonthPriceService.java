package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.crm.domain.ProductImeUpload;
import net.myspring.future.modules.crm.domain.ProductMonthPrice;
import net.myspring.future.modules.crm.domain.ProductMonthPriceDetail;
import net.myspring.future.modules.crm.dto.ProductImeUploadDto;
import net.myspring.future.modules.crm.dto.ProductMonthPriceDetailDto;
import net.myspring.future.modules.crm.dto.ProductMonthPriceDto;
import net.myspring.future.modules.crm.dto.ReportImeUploadDto;
import net.myspring.future.modules.crm.repository.ProductImeUploadRepository;
import net.myspring.future.modules.crm.repository.ProductMonthPriceDetailRepository;
import net.myspring.future.modules.crm.repository.ProductMonthPriceRepository;
import net.myspring.future.modules.crm.web.form.ProductMonthPriceDetailForm;
import net.myspring.future.modules.crm.web.form.ProductMonthPriceForm;
import net.myspring.future.modules.crm.web.query.ProductMonthPriceQuery;
import net.myspring.future.modules.crm.web.query.ProductMonthPriceSumQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.*;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductMonthPriceService {

    @Autowired
    private ProductMonthPriceRepository productMonthPriceRepository;
    @Autowired
    private ProductMonthPriceDetailRepository productMonthPriceDetailRepository;
    @Autowired
    private ProductImeUploadRepository productImeUploadRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private OfficeClient officeClient;

    public Page<ProductMonthPriceDto> findPage(Pageable pageable, ProductMonthPriceQuery productMonthPriceQuery) {
        Page<ProductMonthPriceDto> page = productMonthPriceRepository.findPage(pageable, productMonthPriceQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public String checkMonth(String id, String month) {

        ProductMonthPrice productMonthPrice = productMonthPriceRepository.findByMonthAndEnabledIsTrue(month);
        if (productMonthPrice == null) {
            return null;
        } else if (StringUtils.isBlank(id)) {
            return "月份已经存在";
        } else if (!productMonthPrice.getId().equals(id)) {
            return "月份已经存在";
        }
        return null;
    }

    public ProductMonthPriceDto findDto(String id) {
        ProductMonthPriceDto productMonthPriceDto = productMonthPriceRepository.findDto(id);
        cacheUtils.initCacheInput(productMonthPriceDto);
        return productMonthPriceDto;
    }

    public List<ProductMonthPriceDetailDto> findDetailListForNew() {
        List<ProductMonthPriceDetailDto> result = productMonthPriceDetailRepository.findDetailListForNew();
        cacheUtils.initCacheInput(result);
        return result;
    }

    public List<ProductMonthPriceDetailDto> findDetailListForEdit(String productMonthPriceId) {
        List<ProductMonthPriceDetailDto> result = productMonthPriceDetailRepository.findDetailListForEdit(productMonthPriceId);
        cacheUtils.initCacheInput(result);
        return result;
    }

    public Map<String, Object> findProductMonthPriceSum(ProductMonthPriceSumQuery productMonthPriceSumQuery) {
        Map<String, Object> resultData = Maps.newHashMap();

        List<Map<String, Object>> productTypes = productImeUploadRepository.findProductTypesByMonth(productMonthPriceSumQuery.getMonth());

        resultData.put("header", getHeader(productTypes));
        if (StringUtils.isNotBlank(productMonthPriceSumQuery.getAreaId())) {
            resultData.put("data", getTableData(productMonthPriceSumQuery, productTypes.stream().map(each -> each.get("productTypeId").toString()).collect(Collectors.toList()), false));
        }
        return resultData;
    }

    private List<Object> getHeader(List<Map<String, Object>> productTypes) {

        List<Object> headers = Lists.newArrayList();
        headers.add("办事处");
        headers.add("考核区域");
        headers.add("门店");
        headers.add("促销");
        headers.add("身份证号");
        for (Map<String,Object> mapEntry : productTypes) {
            headers.add(mapEntry.get("productTypeName"));
        }
        headers.add("数量合计");
        headers.add("保卡合计");
        headers.add("销售合计");
        headers.add("销售总合计");

        return headers;
    }

    private  List<List<Object>> getTableData(ProductMonthPriceSumQuery productMonthPriceSumQuery, List<String> productTypeIds, Boolean isExport) {

        List<ReportImeUploadDto> reportImeUploadDtoList = productImeUploadRepository.getReportData(productMonthPriceSumQuery);
        if(CollectionUtil.isEmpty(reportImeUploadDtoList)){
            return Lists.newArrayList();
        }
        cacheUtils.initCacheInput(reportImeUploadDtoList);

        Map<String, ProductMonthPriceDetail> productMonthPriceDetailMap = getProductMonthPriceDetailMap(productMonthPriceSumQuery.getMonth());

        List<List<Object>> preRowList =(isExport ? getPreRowListForExport(productTypeIds, productMonthPriceDetailMap) : Lists.newArrayList());
        List<List<Object>> contentRowList = getContentRowList(productTypeIds, reportImeUploadDtoList, productMonthPriceDetailMap);
        List<Object> sumRow = buildSumRow(contentRowList);

        List<List<Object>> resultData = Lists.newArrayList();
        resultData.addAll(preRowList);
        resultData.addAll(contentRowList);
        resultData.add(sumRow);

        return resultData;
    }

    private List<List<Object>> getPreRowListForExport(List<String> productTypeIds, Map<String, ProductMonthPriceDetail> productMonthPriceDetailMap) {

        List<Object> baoKaPriceRow = Lists.newArrayList("保卡价格","", "", "","");
        baoKaPriceRow.addAll(productTypeIds.stream().map(each ->(productMonthPriceDetailMap.containsKey(each) ? productMonthPriceDetailMap.get(each).getBaokaPrice() : 0) ).collect(Collectors.toList()));
        baoKaPriceRow.addAll(Arrays.asList("","","",""));

        List<Object> price3Row = Lists.newArrayList("三级价格","", "", "","");
        price3Row.addAll(productTypeIds.stream().map(each ->(productMonthPriceDetailMap.containsKey(each) ? productMonthPriceDetailMap.get(each).getPrice3() : 0) ).collect(Collectors.toList()));
        price3Row.addAll(Arrays.asList("","","",""));

        List<Object> batchBaoKaPriceRow = Lists.newArrayList("批发保卡价格","", "", "","");
        batchBaoKaPriceRow.addAll(productTypeIds.stream().map(each ->(productMonthPriceDetailMap.containsKey(each) ? productMonthPriceDetailMap.get(each).getPrice3().subtract(productMonthPriceDetailMap.get(each).getBaokaPrice()) : 0) ).collect(Collectors.toList()));
        batchBaoKaPriceRow.addAll(Arrays.asList("","","",""));

        List<Object> price2Row = Lists.newArrayList("零售价格","", "", "","");
        price2Row.addAll(productTypeIds.stream().map(each ->(productMonthPriceDetailMap.containsKey(each) ? productMonthPriceDetailMap.get(each).getPrice2() : 0) ).collect(Collectors.toList()));
        price2Row.addAll(Arrays.asList("","","",""));

        List<List<Object>> resultData =  Lists.newArrayList();
        resultData.add(baoKaPriceRow);
        resultData.add(price3Row);
        resultData.add(batchBaoKaPriceRow);
        resultData.add(price2Row);
        return resultData;

    }

    private List<List<Object>> getContentRowList(List<String> productTypeIds, List<ReportImeUploadDto> reportImeUploadDtoList, Map<String, ProductMonthPriceDetail> productMonthPriceDetailMap) {
        //根据门店和员工汇总数据
        List<List<Object>> contentRowList = Lists.newArrayList();
        Map<String,List<ReportImeUploadDto>> tableMap = Maps.newHashMap();
        for (ReportImeUploadDto reportImeUploadDto : reportImeUploadDtoList) {
            if (!tableMap.containsKey(reportImeUploadDto.getKey())) {
                tableMap.put(reportImeUploadDto.getKey(), new ArrayList<>());
            }
            tableMap.get(reportImeUploadDto.getKey()).add(reportImeUploadDto);
        }

        for (String key : tableMap.keySet()) {
            contentRowList.add(buildContentRow(productTypeIds, productMonthPriceDetailMap, tableMap.get(key)));
        }
        Comparator<List<Object>> firstComparator = Comparator.comparing(item -> item.get(0).toString());
        contentRowList.sort(firstComparator.thenComparing(item -> item.get(1).toString()).thenComparing(item -> item.get(2).toString()).thenComparing(item -> item.get(3).toString()));
        return contentRowList;
    }

    private List<Object> buildSumRow(List<List<Object>> contentRowList) {
        List<Object> sumRow = Lists.newArrayList("汇总", "","","","");

        int cols = contentRowList.get(0).size();
        for (int i = 5; i < cols-3; i++) {
            int colSum = 0;
            for(List<Object> contentRow : contentRowList){
                colSum += (Integer) contentRow.get(i);
            }
            sumRow.add(colSum);
        }
        for (int i = cols-3; i < cols; i++) {
            BigDecimal colSum = BigDecimal.ZERO;
            for(List<Object> contentRow : contentRowList){
                colSum = colSum.add((BigDecimal) contentRow.get(i));
            }
            sumRow.add(colSum);
        }
        return sumRow;
    }

    private List<Object> buildContentRow(List<String> productTypeIds, Map<String, ProductMonthPriceDetail> productMonthPriceDetailMap, List<ReportImeUploadDto> reportImeUploads) {

        List<Object> row = Lists.newArrayList();
        row.add(reportImeUploads.get(0).getAreaName());
        row.add(reportImeUploads.get(0).getOfficeName());
        row.add(reportImeUploads.get(0).getShopName());
        row.add(StringUtil.isBlank(reportImeUploads.get(0).getEmployeeId()) ? "无促销员" : reportImeUploads.get(0).getEmployeeName());
        row.add(reportImeUploads.get(0).getIdcard());

        //设置动态生成的列
        int rowTotalQty = 0;
        BigDecimal rowTotalBaokaPrice = BigDecimal.ZERO;
        BigDecimal rowTotalPrice3 = BigDecimal.ZERO;
        Map<String, ReportImeUploadDto> productTypeMap = CollectionUtil.extractToMap(reportImeUploads, "productTypeId");
        for (String productTypeId : productTypeIds) {
            Integer qty = (productTypeMap.containsKey(productTypeId) ? productTypeMap.get(productTypeId).getQty() : 0);
            row.add(qty);

            if (qty > 0) {
                rowTotalQty = rowTotalQty + qty;
                ProductMonthPriceDetail productMonthPriceDetail = productMonthPriceDetailMap.get(productTypeId);
                if (productMonthPriceDetail != null) {
                    rowTotalBaokaPrice = rowTotalBaokaPrice.add(new BigDecimal(qty).multiply(productMonthPriceDetail.getBaokaPrice()));
                    rowTotalPrice3 = rowTotalPrice3.add(new BigDecimal(qty).multiply(productMonthPriceDetail.getPrice3()));
                }
            }
        }

        //设置统计数量的列
        row.add(rowTotalQty);
        row.add(rowTotalBaokaPrice);
        row.add(rowTotalPrice3);
        row.add(rowTotalPrice3.subtract(rowTotalBaokaPrice));
        return row;
    }

    private Map<String, ProductMonthPriceDetail> getProductMonthPriceDetailMap(String month) {

        ProductMonthPrice productMonthPrice = productMonthPriceRepository.findByMonthAndEnabledIsTrue(month);
        if (productMonthPrice == null) {
            throw new ServiceException(month+"没有进行每月价格的设置！");
        }

        List<ProductMonthPriceDetail> productMonthPriceDetails = productMonthPriceDetailRepository.findByProductMonthPriceId(productMonthPrice.getId());
        return CollectionUtil.extractToMap(productMonthPriceDetails, "productTypeId");
    }

    public SimpleExcelBook export(ProductMonthPriceSumQuery productMonthPriceSumQuery) {

        Workbook workbook = new SXSSFWorkbook(50000);
        Map<String, CellStyle> cellStyleMap = ExcelUtils.getCellStyleMap(workbook);

        SimpleExcelSheet sumSheet = getSumSheet(productMonthPriceSumQuery, cellStyleMap);
        ExcelUtils.doWrite(workbook, sumSheet);

        SimpleExcelSheet detailSheet = getDetailSheet(productMonthPriceSumQuery, cellStyleMap);
        ExcelUtils.doWrite(workbook, detailSheet);

        return new SimpleExcelBook(workbook, "保卡统计" + productMonthPriceSumQuery.getMonth() + ".xlsx", Lists.newArrayList(sumSheet, detailSheet));
    }

    private SimpleExcelSheet getSumSheet(ProductMonthPriceSumQuery productMonthPriceSumQuery, Map<String, CellStyle> cellStyleMap) {
        CellStyle headCellStyle = cellStyleMap.get(ExcelCellStyle.HEADER.name());
        CellStyle dataCellStyle = cellStyleMap.get(ExcelCellStyle.DATA.name());
        List<List<SimpleExcelColumn>> excelColumnList = Lists.newArrayList();
        List<SimpleExcelColumn> headColumnList = Lists.newArrayList();
        List<Map<String, Object>> productTypes = productImeUploadRepository.findProductTypesByMonth(productMonthPriceSumQuery.getMonth());
        List<Object> headerTitles = getHeader(productTypes);
        for (Object title : headerTitles) {
            headColumnList.add(new SimpleExcelColumn(headCellStyle, title.toString()));
        }
        excelColumnList.add(headColumnList);

        List<List<Object>> tableData = getTableData(productMonthPriceSumQuery, productTypes.stream().map(each -> each.get("productTypeId").toString()).collect(Collectors.toList()),true);

        for (List<Object> row : tableData) {
            List<SimpleExcelColumn> simpleExcelColumns = Lists.newArrayList();
            for (Object cell : row) {
                simpleExcelColumns.add(new SimpleExcelColumn(dataCellStyle, cell));
            }
            excelColumnList.add(simpleExcelColumns);
        }
        return new SimpleExcelSheet("汇总", excelColumnList);
    }

    private SimpleExcelSheet getDetailSheet(ProductMonthPriceSumQuery productMonthPriceSumQuery, Map<String, CellStyle> cellStyleMap) {
        List<List<SimpleExcelColumn>> excelColumnList = Lists.newArrayList();
        CellStyle headCellStyle = cellStyleMap.get(ExcelCellStyle.HEADER.name());

        List<SimpleExcelColumn> headColumnList = Lists.newArrayList();
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"月份"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"办事处"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"考核区域"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"门店"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"岗位"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"促销员"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"促销备注"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"统计型号"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"型号"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"串码"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"上报门店"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"串码所在门店"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"串码发货门店"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"促销绑定门店"));
        excelColumnList.add(headColumnList);

        List<ProductImeUploadDto> productImeUploadDtos = productImeUploadRepository.findImeUploads(productMonthPriceSumQuery);
        cacheUtils.initCacheInput(productImeUploadDtos);
        cacheUtils.initCacheInput(productImeUploadDtos);
        cacheUtils.initCacheInput(productImeUploadDtos);
        Set<String> accountIdList = CollectionUtil.extractToSet(productImeUploadDtos, "accountId");
        List<Map<String, Object>> accountDepotNamesList = productImeUploadRepository.findAccountDepotNamesMap(accountIdList);
        Map<String, String> accountDepotNamesMap= Maps.newHashMap();
        for (Map<String, Object> map : accountDepotNamesList) {
            accountDepotNamesMap.put(map.get("accountId").toString(), map.get("accountShopNames").toString());
        }

        for (ProductImeUploadDto productImeUploadDto : productImeUploadDtos) {
            CellStyle dataCellStyle = getCellStyle(cellStyleMap, productImeUploadDto);
            List<SimpleExcelColumn> dataColumnList=Lists.newArrayList();
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle, productImeUploadDto.getMonth()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle, productImeUploadDto.getShopAreaName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle, productImeUploadDto.getShopOfficeName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle, productImeUploadDto.getShopName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle, productImeUploadDto.getPositionName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle, productImeUploadDto.getEmployeeName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle, productImeUploadDto.getSaleName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle, productImeUploadDto.getProductTypeName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle, productImeUploadDto.getProductImeProductName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle, productImeUploadDto.getProductImeIme()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle, productImeUploadDto.getShopName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle, productImeUploadDto.getSaleShopName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle, productImeUploadDto.getGoodsOrderShopName()));
            String accountShopNames = accountDepotNamesMap.get(productImeUploadDto.getAccountId()) != null ? accountDepotNamesMap.get(productImeUploadDto.getAccountId()) : "";
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle, accountShopNames));
            excelColumnList.add(dataColumnList);
        }
        return new SimpleExcelSheet("串码", excelColumnList);
    }

    private CellStyle getCellStyle(Map<String, CellStyle> cellStyleMap, ProductImeUploadDto productImeUploadDto) {
        CellStyle dataCellStyle = cellStyleMap.get(ExcelCellStyle.DATA.name());
        if (StringUtils.isNotBlank(productImeUploadDto.getSaleShopId()) && StringUtils.isNotBlank(productImeUploadDto.getShopId())
                && !productImeUploadDto.getSaleShopId().equals(productImeUploadDto.getShopId())) {
            dataCellStyle = cellStyleMap.get(ExcelCellStyle.RED.name());
        } else if (StringUtils.isNotBlank(productImeUploadDto.getEmployeeId()) && StringUtils.isNotBlank(productImeUploadDto.getAccountShopIds()) && StringUtils.isNotBlank(productImeUploadDto.getShopId())) {
            List<String> idList= IdUtils.getIdList(productImeUploadDto.getAccountShopIds());
            if(!idList.contains(productImeUploadDto.getShopId())){
                dataCellStyle = cellStyleMap.get(ExcelCellStyle.RED.name());
            }
        }
        return dataCellStyle;
    }

    @Transactional
    public void uploadAudit(ProductMonthPriceSumQuery productMonthPriceSumQuery) {

        List<String> auditOfficeIds = officeClient.getChildOfficeIds(productMonthPriceSumQuery.getAreaId());
        List<ProductImeUpload> productImeUploads = productImeUploadRepository.findForBatchAudit(productMonthPriceSumQuery.getMonth(), auditOfficeIds);

        for (ProductImeUpload productImeUpload : productImeUploads) {
            if (AuditStatusEnum.申请中.name().equals(productImeUpload.getStatus())) {
                productImeUpload.setStatus(AuditStatusEnum.已通过.name());
            }
        }
        productImeUploadRepository.save(productImeUploads);
    }

    @Transactional
    public void save(ProductMonthPriceForm productMonthPriceForm) {

        if (productMonthPriceForm.isCreate()) {
            ProductMonthPrice productMonthPrice = new ProductMonthPrice();
            productMonthPrice.setMonth(productMonthPriceForm.getMonth());
            productMonthPrice.setRemarks(productMonthPriceForm.getRemarks());
            productMonthPriceRepository.save(productMonthPrice);

            batchSaveProductMonthPriceDetails(productMonthPriceForm.getProductMonthPriceDetailList(), productMonthPrice);
        } else {
            ProductMonthPrice productMonthPrice = productMonthPriceRepository.findOne(productMonthPriceForm.getId());
            productMonthPrice.setRemarks(productMonthPriceForm.getRemarks());
            productMonthPriceRepository.save(productMonthPrice);

            batchSaveProductMonthPriceDetails(productMonthPriceForm.getProductMonthPriceDetailList(), productMonthPrice);
        }
    }

    private void batchSaveProductMonthPriceDetails(List<ProductMonthPriceDetailForm> productMonthPriceDetailList, ProductMonthPrice productMonthPrice) {

        if (productMonthPriceDetailList == null || productMonthPriceDetailList.isEmpty()) {
            return;
        }

        Map<String, ProductMonthPriceDetail> productMonthPriceDetailMap = productMonthPriceDetailRepository.findMap(CollectionUtil.extractToList(productMonthPriceDetailList, "id"));

        List<ProductMonthPriceDetail> productMonthPriceDetailsToBeSaved = new ArrayList<>();
        for (ProductMonthPriceDetailForm productMonthPriceDetailForm : productMonthPriceDetailList) {
            ProductMonthPriceDetail productMonthPriceDetail;
            if (productMonthPriceDetailForm.isCreate()) {
                productMonthPriceDetail = new ProductMonthPriceDetail();
                productMonthPriceDetail.setBaokaPrice(productMonthPriceDetailForm.getBaokaPrice() == null ? BigDecimal.ZERO : productMonthPriceDetailForm.getBaokaPrice());
                productMonthPriceDetail.setPrice2(productMonthPriceDetailForm.getPrice2() == null ? BigDecimal.ZERO : productMonthPriceDetailForm.getPrice2());
                productMonthPriceDetail.setPrice3(productMonthPriceDetailForm.getPrice3() == null ? BigDecimal.ZERO : productMonthPriceDetailForm.getPrice3());
                productMonthPriceDetail.setProductMonthPriceId(productMonthPrice.getId());
                productMonthPriceDetail.setProductTypeId(productMonthPriceDetailForm.getProductTypeId());
            } else {
                productMonthPriceDetail = productMonthPriceDetailMap.get(productMonthPriceDetailForm.getId());
                productMonthPriceDetail.setBaokaPrice(productMonthPriceDetailForm.getBaokaPrice() == null ? BigDecimal.ZERO : productMonthPriceDetailForm.getBaokaPrice());
                productMonthPriceDetail.setPrice2(productMonthPriceDetailForm.getPrice2() == null ? BigDecimal.ZERO : productMonthPriceDetailForm.getPrice2());
                productMonthPriceDetail.setPrice3(productMonthPriceDetailForm.getPrice3() == null ? BigDecimal.ZERO : productMonthPriceDetailForm.getPrice3());
            }

            productMonthPriceDetailsToBeSaved.add(productMonthPriceDetail);
        }
        productMonthPriceDetailRepository.save(productMonthPriceDetailsToBeSaved);
    }
}
