package net.myspring.cloud.modules.report.service;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.modules.kingdee.domain.BdCustomerGroup;
import net.myspring.cloud.modules.kingdee.repository.BdCustomerGroupRepository;
import net.myspring.cloud.modules.report.dto.SalProxyReceiveDto;
import net.myspring.cloud.modules.report.repository.SalProxyReceiveRepository;
import net.myspring.cloud.modules.report.web.query.SalProxyReceiveQuery;
import net.myspring.util.excel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 代理销售收款汇总报表
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class SalProxyReceiveService {
    @Autowired
    private SalProxyReceiveRepository salProxyReceiveRepository;
    @Autowired
    private BdCustomerGroupRepository bdCustomerGroupRepository;

    public List<SalProxyReceiveDto> getSalProxyReceiveReport(SalProxyReceiveQuery salProxyReceiveQuery){
        LocalDate startDate = salProxyReceiveQuery.getDateStart();
        LocalDate endDate = salProxyReceiveQuery.getDateEnd();
        List<SalProxyReceiveDto> list = Lists.newLinkedList();
        List<SalProxyReceiveDto> salReceiveReportList = salProxyReceiveRepository.findAll(startDate,endDate);
        List<BdCustomerGroup> customerGroupList  = bdCustomerGroupRepository.findByNameLike("代理");
        for(BdCustomerGroup customerGroup: customerGroupList){
            BigDecimal skAmountForLX = BigDecimal.ZERO;
            BigDecimal fallAmountForLX = BigDecimal.ZERO;
            boolean flagLX = false;
            for (SalProxyReceiveDto receiveReport : salReceiveReportList) {
                if (customerGroup.getFNumber().equals(receiveReport.getGroupNumber()) && receiveReport.getFlx().equals("LX")) {
                    flagLX = true;
                    if (receiveReport.getSkAmount() != null){
                        skAmountForLX = skAmountForLX.add(receiveReport.getSkAmount());
                    }
                    fallAmountForLX = receiveReport.getFallAmount();
                    list.add(receiveReport);
                }
            }
            if (flagLX){
                SalProxyReceiveDto salReceiveLX = new SalProxyReceiveDto();
                salReceiveLX.setFlx("小计");
                salReceiveLX.setSkAmount(skAmountForLX);
                if (!fallAmountForLX.equals(BigDecimal.ZERO)){
                    salReceiveLX.setBackLV(skAmountForLX.divide(fallAmountForLX,4,BigDecimal.ROUND_HALF_UP));
                }else {
                    salReceiveLX.setBackLV(BigDecimal.ZERO);
                }
                list.add(salReceiveLX);
            }
            boolean flagTD = false;
            BigDecimal skAmountForTD = BigDecimal.ZERO;
            BigDecimal fallAmountForTD = BigDecimal.ZERO;
            for (SalProxyReceiveDto receiveReport : salReceiveReportList) {
                if (customerGroup.getFNumber().equals(receiveReport.getGroupNumber()) && receiveReport.getFlx().equals("TD")) {
                    flagTD = true;
                    if (receiveReport.getSkAmount() != null){
                        skAmountForTD = skAmountForTD.add(receiveReport.getSkAmount());
                    }
                    fallAmountForTD = receiveReport.getFallAmount();
                    list.add(receiveReport);
                }
            }
            if (flagTD){
                SalProxyReceiveDto salReceiveTD = new SalProxyReceiveDto();
                salReceiveTD.setFlx("小计");
                salReceiveTD.setSkAmount(skAmountForTD);
                if (!fallAmountForLX.equals(BigDecimal.ZERO)){
                    salReceiveTD.setBackLV(skAmountForTD.divide(fallAmountForTD,4,BigDecimal.ROUND_HALF_UP));
                }else {
                    salReceiveTD.setBackLV(BigDecimal.ZERO);
                }
                list.add(salReceiveTD);
            }
        }
        return list;
    }

    public SimpleExcelBook export(LocalDate dateStart, LocalDate dateEnd ){
        SalProxyReceiveQuery salProxyReceiveQuery = new SalProxyReceiveQuery();
        salProxyReceiveQuery.setDateStart(dateStart);
        salProxyReceiveQuery.setDateEnd(dateEnd);
        List<SalProxyReceiveDto> salProxyReceiveDtoList = getSalProxyReceiveReport(salProxyReceiveQuery);
        List<List<SimpleExcelColumn>> excelColumnList = Lists.newArrayList();
        Workbook workbook = new SXSSFWorkbook(10000);
        Map<String, CellStyle> cellStyleMap= ExcelUtils.getCellStyleMap(workbook);
        List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "客户分组名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()),  "制式"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "销售金额"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "银行名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "收款金额"));
        simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.HEADER.name()), "收款占比"));
        excelColumnList.add(simpleExcelColumnList);
        for (SalProxyReceiveDto salReceiveReport : salProxyReceiveDtoList) {
            simpleExcelColumnList = Lists.newArrayList();
            simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.DATA.name()),salReceiveReport.getGroupName()));
            simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.DATA.name()),salReceiveReport.getFlx()));
            if (salReceiveReport.getFallAmount() != null){
                simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.DATA.name()),salReceiveReport.getFallAmount().setScale(2,BigDecimal.ROUND_HALF_UP)));
            }else {
                simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.DATA.name()),""));
            }
            simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.DATA.name()),salReceiveReport.getBankName()));
            if (salReceiveReport.getSkAmount() != null){
                simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.DATA.name()),salReceiveReport.getSkAmount().setScale(2,BigDecimal.ROUND_HALF_UP)));
            }else {
                simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.DATA.name()),""));
            }
            if (salReceiveReport.getBackLV() != null){
                simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.DATA.name()),salReceiveReport.getBackLV().multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP)+"%"));
            }else {
                simpleExcelColumnList.add(new SimpleExcelColumn(cellStyleMap.get(ExcelCellStyle.DATA.name()),""));
            }
            excelColumnList.add(simpleExcelColumnList);
        }
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("代理销售收款汇总",excelColumnList);
        ExcelUtils.doWrite(workbook,simpleExcelSheet);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"代理销售收款汇总报表"+"（"+dateStart+'-'+dateEnd+"）"+".xlsx",simpleExcelSheet);
        return simpleExcelBook;
    }
}
