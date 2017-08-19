package net.myspring.report.modules.future.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.report.common.utils.CacheUtils;
import net.myspring.report.modules.future.client.OfficeClient;
import net.myspring.report.modules.future.dto.ProductMonthPriceDetailDto;
import net.myspring.report.modules.future.dto.ProductMonthPriceSumDto;
import net.myspring.report.modules.future.dto.ProductTypeDto;
import net.myspring.report.modules.future.enums.AuditStatusEnum;
import net.myspring.report.modules.future.repository.ProductImeUploadRepository;
import net.myspring.report.modules.future.repository.ProductMonthPriceDetailRepository;
import net.myspring.report.modules.future.repository.ProductMonthPriceSumRepository;
import net.myspring.report.modules.future.repository.ProductTypeRepository;
import net.myspring.report.modules.future.web.query.ProductMonthPriceSumQuery;
import net.myspring.util.excel.*;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductMonthPriceSumService {

    @Autowired
    private ProductTypeRepository productTypeRepository;
    @Autowired
    private ProductMonthPriceSumRepository productMonthPriceSumRepository;
    @Autowired
    private ProductMonthPriceDetailRepository productMonthPriceDetailRepository;
    @Autowired
    private ProductImeUploadRepository productImeUploadRepository;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private CacheUtils cacheUtils;

    @Transactional
    public void uploadAudit(ProductMonthPriceSumQuery productMonthPriceSumQuery) {
        productMonthPriceSumQuery.setAuditOfficeIdList(officeClient.getChildOfficeIds(productMonthPriceSumQuery.getAreaId()));
        productImeUploadRepository.batchAudit(productMonthPriceSumQuery);
    }

    public SimpleExcelBook export(ProductMonthPriceSumQuery productMonthPriceSumQuery) {
        Workbook workbook = new SXSSFWorkbook(10000);
        Map<String, CellStyle> cellStyleMap = ExcelUtils.getCellStyleMap(workbook);
        CellStyle headCellStyle = cellStyleMap.get(ExcelCellStyle.HEADER.name());
        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();

        List<List<SimpleExcelColumn>> simpleExcelColumnsList = Lists.newArrayList();
        List<ProductTypeDto> productTypeDtoList = productTypeRepository.findByMonth(productMonthPriceSumQuery.getMonth());
        List<SimpleExcelColumn> simpleExcelColumns = Lists.newArrayList();
        simpleExcelColumns.add(new SimpleExcelColumn(headCellStyle, "办事处"));
        simpleExcelColumns.add(new SimpleExcelColumn(headCellStyle, "考核区域"));
        simpleExcelColumns.add(new SimpleExcelColumn(headCellStyle, "门店"));
        simpleExcelColumns.add(new SimpleExcelColumn(headCellStyle, "促销"));
        simpleExcelColumns.add(new SimpleExcelColumn(headCellStyle, "身份证号"));
        for (ProductTypeDto productTypeDto : productTypeDtoList) {
            simpleExcelColumns.add(new SimpleExcelColumn(workbook, productTypeDto.getName()));
        }
        simpleExcelColumns.add(new SimpleExcelColumn(headCellStyle, "数量合计"));
        simpleExcelColumns.add(new SimpleExcelColumn(headCellStyle, "保卡合计"));
        simpleExcelColumns.add(new SimpleExcelColumn(headCellStyle, "销售合计"));
        simpleExcelColumns.add(new SimpleExcelColumn(headCellStyle, "销售总合计"));
        simpleExcelColumnsList.add(simpleExcelColumns);
        //获取显示数据
        getSumData(productMonthPriceSumQuery, simpleExcelColumnsList, productTypeDtoList, workbook);
        SimpleExcelSheet sheet = new SimpleExcelSheet("保卡统计汇总", simpleExcelColumnsList);
        simpleExcelSheetList.add(sheet);

        List<List<SimpleExcelColumn>> detailsList = Lists.newArrayList();
        List<SimpleExcelColumn> detailList = Lists.newArrayList();
        detailList.add(new SimpleExcelColumn(headCellStyle, "月份"));
        detailList.add(new SimpleExcelColumn(headCellStyle, "办事处"));
        detailList.add(new SimpleExcelColumn(headCellStyle, "考核区域"));
        detailList.add(new SimpleExcelColumn(headCellStyle, "门店"));
        detailList.add(new SimpleExcelColumn(headCellStyle, "岗位"));
        detailList.add(new SimpleExcelColumn(headCellStyle, "促销员"));
        detailList.add(new SimpleExcelColumn(headCellStyle, "促销备注"));
        detailList.add(new SimpleExcelColumn(headCellStyle, "统计型号"));
        detailList.add(new SimpleExcelColumn(headCellStyle, "型号"));
        detailList.add(new SimpleExcelColumn(headCellStyle, "串码"));
        detailList.add(new SimpleExcelColumn(headCellStyle, "上报门店"));
        detailList.add(new SimpleExcelColumn(headCellStyle, "串码所在门店"));
        detailList.add(new SimpleExcelColumn(headCellStyle, "串码发货门店"));
        detailList.add(new SimpleExcelColumn(headCellStyle, "促销绑定门店"));
        detailsList.add(detailList);
        //获取显示数据
        getDetailData(productMonthPriceSumQuery, simpleExcelColumnsList, cellStyleMap);

        SimpleExcelSheet detailSheet = new SimpleExcelSheet("保卡统计明细", detailsList);
        simpleExcelSheetList.add(detailSheet);
        ExcelUtils.doWrite(workbook, simpleExcelSheetList);
        return new SimpleExcelBook(workbook, "保卡统计" + productMonthPriceSumQuery.getMonth() + ".xlsx", sheet);
    }

    private void getSumData(ProductMonthPriceSumQuery productMonthPriceSumQuery, List<List<SimpleExcelColumn>> simpleExcelColumnsList, List<ProductTypeDto> productTypeDtoList, Workbook workbook) {
        List<ProductMonthPriceDetailDto> productMonthPriceDetails = productMonthPriceDetailRepository.findByMonth(productMonthPriceSumQuery.getMonth());
        Map<String, ProductMonthPriceDetailDto> productMonthPriceDetailDtoMap = productMonthPriceDetails.stream().collect(Collectors.toMap(ProductMonthPriceDetailDto::getProductTypeId, ProductMonthPriceDetailDto -> ProductMonthPriceDetailDto));

        List<ProductMonthPriceSumDto> list = productMonthPriceSumRepository.findReportData(productMonthPriceSumQuery);
        cacheUtils.initCacheInput(list);
        Map<String, Map<String, ProductMonthPriceSumDto>> productMonthPriceSumMap = Maps.newHashMap();
        Map<String,Object> sumMap=Maps.newHashMap();
        Integer totalSumQty = 0;
        BigDecimal totalBaokaSum = BigDecimal.ZERO;
        BigDecimal totalSaleSum = BigDecimal.ZERO;
        BigDecimal totalRealSaleSum = BigDecimal.ZERO;
        for (ProductMonthPriceSumDto productMonthPriceSumDto : list) {
            String key = productMonthPriceSumDto.getKey();
            if (!productMonthPriceSumMap.containsKey(key)) {
                productMonthPriceSumMap.put(key, Maps.newHashMap());
            }
            if(!sumMap.containsKey(productMonthPriceSumDto.getProductTypeId())){
                sumMap.put(productMonthPriceSumDto.getProductTypeId(),BigDecimal.ZERO);
            }
            Integer qty=productMonthPriceSumDto.getQty();
            String productTypeId=productMonthPriceSumDto.getProductTypeId();
            ProductMonthPriceDetailDto productMonthPriceDetail = productMonthPriceDetailDtoMap.get(productTypeId);
            totalSumQty=totalSumQty+qty;
            totalBaokaSum=totalBaokaSum.add(BigDecimal.valueOf(qty).multiply(productMonthPriceDetail.getBaokaPrice()));
            totalSaleSum=totalSaleSum.add(new BigDecimal(qty).multiply(productMonthPriceDetail.getPrice3()));
            totalRealSaleSum=totalRealSaleSum.add(new BigDecimal(qty).multiply((productMonthPriceDetail.getPrice3().subtract(productMonthPriceDetail.getBaokaPrice()))));
            sumMap.put(productMonthPriceSumDto.getProductTypeId(),(Integer)sumMap.get(productTypeId)+qty);
            productMonthPriceSumMap.get(key).put(productMonthPriceSumDto.getProductTypeId(), productMonthPriceSumDto);
        }
        sumMap.put("totalSumQty",totalSumQty);
        sumMap.put("totalBaokaSum",totalBaokaSum);
        sumMap.put("totalSaleSum",totalSaleSum);
        sumMap.put("totalRealSaleSum",totalRealSaleSum);
        simpleExcelColumnsList.add(sumSimpleHeader("保卡价格", productTypeDtoList, productMonthPriceDetailDtoMap, sumMap,workbook));
        simpleExcelColumnsList.add(sumSimpleHeader("三级价格", productTypeDtoList, productMonthPriceDetailDtoMap, sumMap,workbook));
        simpleExcelColumnsList.add(sumSimpleHeader("批发保卡价格", productTypeDtoList, productMonthPriceDetailDtoMap,sumMap, workbook));
        simpleExcelColumnsList.add(sumSimpleHeader("零售价格", productTypeDtoList, productMonthPriceDetailDtoMap, sumMap,workbook));
        for (Map.Entry<String, Map<String, ProductMonthPriceSumDto>> map : productMonthPriceSumMap.entrySet()) {
            List<SimpleExcelColumn> simpleExcelColumns = Lists.newArrayList();
            ProductMonthPriceSumDto productMonthPriceSumDto = map.getValue().get(Lists.newArrayList(map.getValue().keySet()).get(0));
            simpleExcelColumns.add(new SimpleExcelColumn(workbook, productMonthPriceSumDto.getAreaName()));
            simpleExcelColumns.add(new SimpleExcelColumn(workbook, productMonthPriceSumDto.getOfficeName()));
            simpleExcelColumns.add(new SimpleExcelColumn(workbook, productMonthPriceSumDto.getShopName()));
            simpleExcelColumns.add(new SimpleExcelColumn(workbook, productMonthPriceSumDto.getEmployeeName()));
            simpleExcelColumns.add(new SimpleExcelColumn(workbook, productMonthPriceSumDto.getEmployeeSaleName()));
            Integer sumQty = 0;
            BigDecimal baokaSum = BigDecimal.ZERO;
            BigDecimal saleSun = BigDecimal.ZERO;
            BigDecimal realSaleSum = BigDecimal.ZERO;
            for (ProductTypeDto productTypeDto : productTypeDtoList) {
                Integer qty = map.getValue().get(productTypeDto.getId()).getQty();
                simpleExcelColumns.add(new SimpleExcelColumn(workbook, qty.toString()));
                ProductMonthPriceDetailDto productMonthPriceDetail = productMonthPriceDetailDtoMap.get(productTypeDto.getId());
                sumQty = sumQty + qty;
                baokaSum = baokaSum.add(BigDecimal.valueOf(productMonthPriceSumDto.getQty()).multiply(productMonthPriceDetail.getBaokaPrice()));
                saleSun = saleSun.add(new BigDecimal(qty).multiply(productMonthPriceDetail.getPrice3()));
                realSaleSum = realSaleSum.add(new BigDecimal(qty).multiply((productMonthPriceDetail.getPrice3().subtract(productMonthPriceDetail.getBaokaPrice()))));
            }
            simpleExcelColumns.add(new SimpleExcelColumn(workbook, sumQty.toString()));
            simpleExcelColumns.add(new SimpleExcelColumn(workbook, baokaSum.toString()));
            simpleExcelColumns.add(new SimpleExcelColumn(workbook, saleSun.toString()));
            simpleExcelColumns.add(new SimpleExcelColumn(workbook, realSaleSum.toString()));
            simpleExcelColumnsList.add(simpleExcelColumns);
        }
        simpleExcelColumnsList.add(sumSimpleHeader("汇总", productTypeDtoList, productMonthPriceDetailDtoMap, sumMap,workbook));
    }

    private List<SimpleExcelColumn> sumSimpleHeader(String firstCellValue, List<ProductTypeDto> productTypeDtos, Map<String, ProductMonthPriceDetailDto> productMonthPriceDetailDtoMap, Map<String,Object> sunMap,Workbook workbook) {
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, firstCellValue));
        simpleExcelColumnList.addAll(Lists.newArrayList(new SimpleExcelColumn(), new SimpleExcelColumn(), new SimpleExcelColumn(), new SimpleExcelColumn()));
        for (ProductTypeDto productTypeDto : productTypeDtos) {
            ProductMonthPriceDetailDto productMonthPriceDetailDto = productMonthPriceDetailDtoMap.get(productTypeDto.getId());
            if ("保卡价格".equals(firstCellValue)) {
                simpleExcelColumnList.add(new SimpleExcelColumn(workbook, productMonthPriceDetailDto.getBaokaPrice().toString()));
            } else if ("三级价格".equals(firstCellValue)) {
                simpleExcelColumnList.add(new SimpleExcelColumn(workbook, productMonthPriceDetailDto.getPrice3().toString()));
            } else if ("批发保卡价格".equals(firstCellValue)) {
                simpleExcelColumnList.add(new SimpleExcelColumn(workbook, productMonthPriceDetailDto.getPrice2().toString()));
            } else if ("零售价格".equals(firstCellValue)) {
                simpleExcelColumnList.add(new SimpleExcelColumn(workbook, productMonthPriceDetailDto.getPrice3().toString()));
            }else if("汇总".equals(firstCellValue)){
                simpleExcelColumnList.add(new SimpleExcelColumn(workbook, sunMap.get(productTypeDto.getId()).toString()));
            }
        }
        if("汇总".equals(firstCellValue)){
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook, sunMap.get("sumQty").toString()));
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook, sunMap.get("totalBaokaSum").toString()));
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook, sunMap.get("totalSaleSum").toString()));
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook, sunMap.get("totalRealSaleSum").toString()));
        }else {
            simpleExcelColumnList.addAll(Lists.newArrayList(new SimpleExcelColumn(), new SimpleExcelColumn(), new SimpleExcelColumn(), new SimpleExcelColumn()));
        }
        return simpleExcelColumnList;
    }

    private void getDetailData(ProductMonthPriceSumQuery productMonthPriceSumQuery, List<List<SimpleExcelColumn>> simpleExcelColumnsList,Map<String, CellStyle> cellStyleMap) {
        productMonthPriceSumQuery.setIsDetail(true);
        List<ProductMonthPriceSumDto> list = productMonthPriceSumRepository.findReportData(productMonthPriceSumQuery);
        cacheUtils.initCacheInput(list);
        for (ProductMonthPriceSumDto productMonthPriceSumDto : list) {
            List<SimpleExcelColumn> detailColumnList=Lists.newArrayList();
            CellStyle dataCellStyle = getCellStyle(cellStyleMap, productMonthPriceSumDto);
            detailColumnList.add(new SimpleExcelColumn(dataCellStyle, productMonthPriceSumDto.getMonth()));
            detailColumnList.add(new SimpleExcelColumn(dataCellStyle, productMonthPriceSumDto.getAreaName()));
            detailColumnList.add(new SimpleExcelColumn(dataCellStyle, productMonthPriceSumDto.getOfficeName()));
            detailColumnList.add(new SimpleExcelColumn(dataCellStyle, productMonthPriceSumDto.getShopName()));
            detailColumnList.add(new SimpleExcelColumn(dataCellStyle, StringUtils.isBlank(productMonthPriceSumDto.getEmployeeId()) ? "" : ""));
            detailColumnList.add(new SimpleExcelColumn(dataCellStyle, StringUtils.isBlank(productMonthPriceSumDto.getEmployeeId()) ? "无促销员" : productMonthPriceSumDto.getEmployeeName()));
            detailColumnList.add(new SimpleExcelColumn(dataCellStyle, StringUtils.isBlank(productMonthPriceSumDto.getEmployeeId()) ? "无促销员" : productMonthPriceSumDto.getEmployeeSaleName()));
            detailColumnList.add(new SimpleExcelColumn(dataCellStyle, productMonthPriceSumDto.getProductTypeName()));
            detailColumnList.add(new SimpleExcelColumn(dataCellStyle, productMonthPriceSumDto.getProductName()));
            detailColumnList.add(new SimpleExcelColumn(dataCellStyle, productMonthPriceSumDto.getIme()));
            detailColumnList.add(new SimpleExcelColumn(dataCellStyle, productMonthPriceSumDto.getShopName()));
            detailColumnList.add(new SimpleExcelColumn(dataCellStyle, productMonthPriceSumDto.getSaleShopName()));
            detailColumnList.add(new SimpleExcelColumn(dataCellStyle, productMonthPriceSumDto.getGoodsOrderShopName()));
            detailColumnList.add(new SimpleExcelColumn(dataCellStyle, StringUtils.join(productMonthPriceSumDto.getAccountShopName(), CharConstant.COMMA)));
            simpleExcelColumnsList.add(detailColumnList);
        }
    }

    private CellStyle getCellStyle(Map<String, CellStyle> cellStyleMap, ProductMonthPriceSumDto productMonthPriceSumDto) {
        CellStyle dataCellStyle = cellStyleMap.get(ExcelCellStyle.DATA.name());
        if (StringUtils.isNotBlank(productMonthPriceSumDto.getSaleShopId()) && StringUtils.isNotBlank(productMonthPriceSumDto.getShopId())&& !productMonthPriceSumDto.getSaleShopId().equals(productMonthPriceSumDto.getShopId())) {
            dataCellStyle = cellStyleMap.get(ExcelCellStyle.RED.name());
        } else if (StringUtils.isNotBlank(productMonthPriceSumDto.getEmployeeId()) && StringUtils.isNotBlank(productMonthPriceSumDto.getAccountShopIds()) && StringUtils.isNotBlank(productMonthPriceSumDto.getShopId())) {
            if(!productMonthPriceSumDto.getAccountShopIdList().contains(productMonthPriceSumDto.getShopId())){
                dataCellStyle = cellStyleMap.get(ExcelCellStyle.RED.name());
            }
        }
        return dataCellStyle;
    }
}
