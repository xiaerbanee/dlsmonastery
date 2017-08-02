package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.crm.domain.ProductImeUpload;
import net.myspring.future.modules.crm.domain.ProductMonthPrice;
import net.myspring.future.modules.crm.domain.ProductMonthPriceDetail;
import net.myspring.future.modules.crm.dto.*;
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
import net.myspring.util.time.LocalDateUtils;
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
import java.time.LocalDate;
import java.util.*;

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
    private DepotManager depotManager;
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
        if (StringUtils.isBlank(productMonthPriceSumQuery.getMonth())) {
            productMonthPriceSumQuery.setMonth(LocalDateUtils.format(LocalDate.now().minusMonths(1),"yyyy-MM"));
        }
        //获取动态的每月销售货品的类型列
        List<Map<String, Object>> productTypes = productImeUploadRepository.findProductTypesByMonth(productMonthPriceSumQuery.getMonth());
        List<Object> headers = getHeader(productTypes);
        resultData.put("header", headers);

        //获取动态的每月销售货品的数据
        if (StringUtils.isNotBlank(productMonthPriceSumQuery.getAreaId())) {
            Map<String,Object> map = getData(productMonthPriceSumQuery, productTypes, false);
            resultData.put("message", map.get("message"));
            resultData.put("data", map.get("data"));
        }
        return resultData;
    }

    public List<Object> getHeader(List<Map<String, Object>> productTypes) {

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

    public Map<String, Object> getData(ProductMonthPriceSumQuery productMonthPriceSumQuery, List<Map<String, Object>> productTypes, Boolean isExport) {

        Map<String, Object> result = Maps.newHashMap();
        productMonthPriceSumQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
        List<ReportImeUploadDto> productImeUploads = productImeUploadRepository.getReportDatas(productMonthPriceSumQuery);
        cacheUtils.initCacheInput(productImeUploads);

        List<String> productTypeIds = Lists.newArrayList();
        for (Map<String, Object> map : productTypes) {
            productTypeIds.add(map.get("productTypeId").toString());
        }

        //用于拼接前台要展示的保卡数据信息
        List<List<Object>> resultData = Lists.newArrayList();

        if (CollectionUtil.isNotEmpty(productImeUploads)) {
            ProductMonthPrice productMonthPrice = productMonthPriceRepository.findByMonthAndEnabledIsTrue(productMonthPriceSumQuery.getMonth());
            Map<String, ProductMonthPriceDetail> productMonthPriceDetailMap = Maps.newHashMap();
            if (productMonthPrice != null) {
                List<ProductMonthPriceDetail> productMonthPriceDetails = productMonthPriceDetailRepository.findByProductMonthPriceId(productMonthPrice.getId());
                if (CollectionUtil.isNotEmpty(productMonthPriceDetails)) {
                    for (ProductMonthPriceDetail productMonthPriceDetail : productMonthPriceDetails) {
                        if (productMonthPriceDetail.getBaokaPrice().compareTo(BigDecimal.ZERO) > 0) {
                            productMonthPriceDetailMap.put(productMonthPriceDetail.getProductTypeId(), productMonthPriceDetail);
                        }
                    }
                }
            } else {
                //每月价格没有设置
                result.put("message", productMonthPriceSumQuery.getMonth()+"没有进行每月价格的设置！");
                result.put("data", resultData);
                return result;
            }
            //根据门店和员工汇总数据
            Map<String,List<ReportImeUploadDto>> tableMap = Maps.newHashMap();
            for (ReportImeUploadDto reportImeUploadDto : productImeUploads) {
                if (!tableMap.containsKey(reportImeUploadDto.getKey())) {
                    tableMap.put(reportImeUploadDto.getKey(), new ArrayList<>());
                }
                tableMap.get(reportImeUploadDto.getKey()).add(reportImeUploadDto);
            }
            int totalQty = 0;
            BigDecimal totalBaokaPrice = BigDecimal.ZERO;
            BigDecimal totalPrice3 = BigDecimal.ZERO;
            Map<String,Integer> productTypeQtyMap = Maps.newHashMap();
            for (String key : tableMap.keySet()) {
                List<ReportImeUploadDto> reportImeUploads = tableMap.get(key);
                //根据型号名称同级
                Map<String, ReportImeUploadDto> productTypeMap = Maps.newHashMap();
                for(ReportImeUploadDto reportImeUpload:reportImeUploads) {
                    productTypeMap.put(reportImeUpload.getProductTypeId()+"", reportImeUpload);
                }

                int rowQty = 0;
                BigDecimal rowBaokaPrice = BigDecimal.ZERO;
                BigDecimal rowPrice3 = BigDecimal.ZERO;

                List<Object> row = Lists.newArrayList();
                row.add(reportImeUploads.get(0).getAreaName());
                row.add(reportImeUploads.get(0).getOfficeName());
                row.add(reportImeUploads.get(0).getShopName());
                row.add(StringUtil.isBlank(reportImeUploads.get(0).getEmployeeId()) ? "无促销员" : reportImeUploads.get(0).getEmployeeName());
                row.add(reportImeUploads.get(0).getIdcard());

                //设置动态生成的列
                for (String productTypeId : productTypeIds) {
                    Integer qty = 0;
                    if (productTypeMap.containsKey(productTypeId)) {
                        qty = productTypeMap.get(productTypeId).getQty();
                    }
                    row.add(qty);
                    if (qty > 0) {
                        rowQty = rowQty + qty;
                        ProductMonthPriceDetail productMonthPriceDetail = productMonthPriceDetailMap.get(productTypeId);
                        if (productMonthPriceDetail != null) {
                            rowBaokaPrice = rowBaokaPrice.add(new BigDecimal(qty).multiply(productMonthPriceDetail.getBaokaPrice()));
                            rowPrice3 = rowPrice3.add(new BigDecimal(qty).multiply(productMonthPriceDetail.getPrice3()));
                        }
                    }
                    if (!productTypeQtyMap.containsKey(productTypeId)) {
                        productTypeQtyMap.put(productTypeId, 0);
                    }
                    productTypeQtyMap.put(productTypeId, productTypeQtyMap.get(productTypeId) + qty);
                }

                //设置统计数量的列
                row.add(rowQty);
                row.add(rowBaokaPrice);
                row.add(rowPrice3);
                row.add(rowPrice3.subtract(rowBaokaPrice));
                resultData.add(row);
                totalQty =totalQty + rowQty;
                totalBaokaPrice = totalBaokaPrice.add(rowBaokaPrice);
                totalPrice3 = totalPrice3.add(rowPrice3);

            }
            Collections.sort(resultData, new Comparator<List<Object>>() {
                public int compare(List<Object> item1, List<Object> item2) {
                    return (item1.get(0).toString() + item1.get(1).toString() + item1.get(2).toString() + item1.get(3).toString()).compareTo(item2.get(0).toString() + item2.get(1).toString() + item2.get(2).toString() + item2.get(3).toString());
                }
            });

            //汇总数据
            List<Object> row = Lists.newArrayList();
            row.add("汇总");
            row.addAll(Arrays.asList("","","",""));
            for (String productTypeId : productTypeIds) {
                row.add(productTypeQtyMap.get(productTypeId));
            }
            row.add(totalQty);
            row.add(totalBaokaPrice);
            row.add(totalPrice3);
            row.add(totalPrice3.subtract(totalBaokaPrice));
            resultData.add(row);

            //如果是导出数据，需要在前面增加三列
            if (isExport) {
                //保卡价格
                row = Lists.newArrayList();
                row.add("保卡价格");
                row.addAll(Arrays.asList("", "", "",""));
                for (String productTypeId : productTypeIds) {
                    ProductMonthPriceDetail productMonthPriceDetail = productMonthPriceDetailMap.get(productTypeId);
                    if (productMonthPriceDetail == null) {
                        row.add(0);
                    } else {
                        row.add(productMonthPriceDetail.getBaokaPrice());
                    }
                }
                row.addAll(Arrays.asList("", "", "", "",""));
                resultData.add(0, row);

                //三级价格
                row = Lists.newArrayList();
                row.add("三级价格");
                row.addAll(Arrays.asList("","","",""));
                for(String productTypeName:productTypeIds) {
                    ProductMonthPriceDetail  productMonthPriceDetail = productMonthPriceDetailMap.get(productTypeName);
                    if(productMonthPriceDetail==null) {
                        row.add(0);
                    } else {
                        row.add(productMonthPriceDetail.getPrice3());
                    }
                }
                row.addAll(Arrays.asList("","","","",""));
                resultData.add(1, row);


                //批发保卡价格
                row = Lists.newArrayList();
                row.add("批发保卡价格");
                row.addAll(Arrays.asList("","","",""));
                for(String productTypeName:productTypeIds) {
                    ProductMonthPriceDetail  productMonthPriceDetail = productMonthPriceDetailMap.get(productTypeName);
                    if(productMonthPriceDetail==null) {
                        row.add(0);
                    } else {
                        row.add(productMonthPriceDetail.getPrice3().subtract(productMonthPriceDetail.getBaokaPrice()));
                    }
                }
                row.addAll(Arrays.asList("","","",""));
                resultData.add(2,row);


                //零售价格
                row = Lists.newArrayList();
                row.add("零售价格");
                row.addAll(Arrays.asList("","","",""));
                for(String productTypeName:productTypeIds) {
                    ProductMonthPriceDetail  productMonthPriceDetail = productMonthPriceDetailMap.get(productTypeName);
                    if(productMonthPriceDetail==null) {
                        row.add(0);
                    } else {
                        row.add(productMonthPriceDetail.getPrice2());
                    }
                }
                row.addAll(Arrays.asList("","","",""));
                resultData.add(3,row);
            }

        }

        result.put("data", resultData);
        return result;

    }

    public SimpleExcelBook export(ProductMonthPriceSumQuery productMonthPriceSumQuery) {
        if (StringUtils.isBlank(productMonthPriceSumQuery.getMonth())) {
            productMonthPriceSumQuery.setMonth(LocalDateUtils.format(LocalDate.now().minusMonths(1),"yyyy-MM"));
        }
        List<Map<String, Object>> productTypes = productImeUploadRepository.findProductTypesByMonth(productMonthPriceSumQuery.getMonth());

        Workbook workbook = new SXSSFWorkbook(10000);
        //sheet 汇总
        List<List<SimpleExcelColumn>> excelColumnList = Lists.newArrayList();
        Map<String, CellStyle> cellStyleMap = ExcelUtils.getCellStyleMap(workbook);
        CellStyle headCellStyle = cellStyleMap.get(ExcelCellStyle.HEADER.name());
        CellStyle dataCellStyle = cellStyleMap.get(ExcelCellStyle.DATA.name());

        List<SimpleExcelColumn> headColumnList = Lists.newArrayList();
        List<Object> headerTitles = getHeader(productTypes);
        for (Object title : headerTitles) {
            headColumnList.add(new SimpleExcelColumn(headCellStyle, title.toString()));
        }
        excelColumnList.add(headColumnList);

        Map<String,Object> dataMap = getData(productMonthPriceSumQuery, productTypes,true);
        List<List<Object>> datas = (List<List<Object>>) dataMap.get("data");
        for (List<Object> item : datas) {
            List<SimpleExcelColumn> simpleExcelColumns = Lists.newArrayList();
            for (int i =0;i<item.size();i++) {
                simpleExcelColumns.add(new SimpleExcelColumn(dataCellStyle, item.get(i)));
            }
            excelColumnList.add(simpleExcelColumns);
        }
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("汇总",excelColumnList);
        ExcelUtils.doWrite(workbook, simpleExcelSheet);

        List<List<SimpleExcelColumn>> excelColumnList1 = Lists.newArrayList();
        Map<String, CellStyle> cellStyleMap1 = ExcelUtils.getCellStyleMap(workbook);
        CellStyle headCellStyle1 = cellStyleMap1.get(ExcelCellStyle.HEADER.name());

        List<SimpleExcelColumn> headColumnList1 = Lists.newArrayList();
        headColumnList1.add(new SimpleExcelColumn(headCellStyle1,"月份"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle1,"办事处"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle1,"考核区域"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle1,"门店"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle1,"岗位"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle1,"促销员"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle1,"促销备注"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle1,"统计型号"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle1,"型号"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle1,"串码"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle1,"上报门店"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle1,"串码所在门店"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle1,"串码发货门店"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle1,"促销绑定门店"));
        excelColumnList1.add(headColumnList1);

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
            CellStyle dataCellStyle1 = cellStyleMap1.get(ExcelCellStyle.DATA.name());
            if (StringUtils.isNotBlank(productImeUploadDto.getSaleShopId()) && StringUtils.isNotBlank(productImeUploadDto.getShopId())
                    && !productImeUploadDto.getSaleShopId().equals(productImeUploadDto.getShopId())) {
                dataCellStyle1 = cellStyleMap1.get(ExcelCellStyle.RED.name());
            } else if (StringUtils.isNotBlank(productImeUploadDto.getEmployeeId()) && StringUtils.isNotBlank(productImeUploadDto.getAccountShopIds()) && StringUtils.isNotBlank(productImeUploadDto.getShopId())) {
                List<String>  idList= IdUtils.getIdList(productImeUploadDto.getAccountShopIds());
                if(!idList.contains(productImeUploadDto.getShopId())){
                    dataCellStyle1 = cellStyleMap1.get(ExcelCellStyle.RED.name());
                }
            }
            List<SimpleExcelColumn> dataColumnList=Lists.newArrayList();
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle1, productImeUploadDto.getMonth()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle1, productImeUploadDto.getShopAreaName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle1, productImeUploadDto.getShopOfficeName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle1, productImeUploadDto.getShopName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle1, productImeUploadDto.getPositionName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle1, productImeUploadDto.getEmployeeName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle1, productImeUploadDto.getSaleName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle1, productImeUploadDto.getProductTypeName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle1, productImeUploadDto.getProductImeProductName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle1, productImeUploadDto.getProductImeIme()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle1, productImeUploadDto.getShopName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle1, productImeUploadDto.getSaleShopName()));
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle1, productImeUploadDto.getGoodsOrderShopName()));
            String accountShopNames = accountDepotNamesMap.get(productImeUploadDto.getAccountId()) != null ? accountDepotNamesMap.get(productImeUploadDto.getAccountId()) : "";
            dataColumnList.add(new SimpleExcelColumn(dataCellStyle1, accountShopNames));
            excelColumnList1.add(dataColumnList);
        }

        SimpleExcelSheet simpleExcelSheet1 = new SimpleExcelSheet("串码",excelColumnList1);
        ExcelUtils.doWrite(workbook, simpleExcelSheet1);

        List<SimpleExcelSheet> sheets = Lists.newArrayList();
        sheets.add(simpleExcelSheet);
        sheets.add(simpleExcelSheet1);
        return new SimpleExcelBook(workbook, "保卡统计" + productMonthPriceSumQuery.getMonth() + ".xlsx", sheets);

    }

    @Transactional
    public void uploadAudit(ProductMonthPriceSumQuery productMonthPriceSumQuery) {
        if (StringUtils.isBlank(productMonthPriceSumQuery.getMonth())) {
            productMonthPriceSumQuery.setMonth(LocalDateUtils.format(LocalDate.now().minusMonths(1),"yyyy-MM"));
        }
        List<String> officeIds = officeClient.getChildOfficeIds(productMonthPriceSumQuery.getAreaId());
        List<ProductImeUpload> productImeUploads = productImeUploadRepository.findByMonthAndOfficeId(productMonthPriceSumQuery.getMonth(), officeIds);

        if (CollectionUtil.isNotEmpty(productImeUploads)) {
            for (ProductImeUpload productImeUpload : productImeUploads) {
                if (AuditStatusEnum.申请中.name().equals(productImeUpload.getStatus())) {
                    productImeUpload.setStatus(AuditStatusEnum.已通过.name());
                    productImeUploadRepository.save(productImeUpload);
                }
            }
        }
    }

    @Transactional
    public void save(ProductMonthPriceForm productMonthPriceForm) {
        ProductMonthPrice productMonthPrice = null;
        if (productMonthPriceForm.isCreate()) {
            productMonthPrice = new ProductMonthPrice();
            productMonthPrice.setMonth(productMonthPriceForm.getMonth());
            productMonthPrice.setRemarks(productMonthPriceForm.getRemarks());
            productMonthPriceRepository.save(productMonthPrice);

            batchSaveProductMonthPriceDetails(productMonthPriceForm.getProductMonthPriceDetailList(), productMonthPrice);
        } else {
            productMonthPrice = productMonthPriceRepository.findOne(productMonthPriceForm.getId());
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
