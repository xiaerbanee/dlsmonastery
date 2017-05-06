package net.myspring.cloud.modules.report.service;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.constant.K3CloudConstant;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.CharEnum;
import net.myspring.cloud.common.enums.DateFormat;
import net.myspring.cloud.common.handsontable.NestedHeaderCell;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import net.myspring.cloud.modules.report.mapper.GlcxViewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static net.myspring.cloud.common.utils.Const.*;

/**
 * 零售报表工具类
 * Created by lihx on 2016/11/8.
 */
@Service
@KingdeeDataSource
public class RetailReportForAssistService {
    @Autowired
    private GlcxViewMapper glcxViewMapper;

    //"合计"部门
    public NameNumberDto getAddDepartment() {
        NameNumberDto department = new NameNumberDto();
        department.setNumber(K3CloudConstant.DOUBLE_ZERO);
        department.setName(K3CloudConstant.TOTAL_DEPARTMENT);
        return department;
    }

    //excel table for head
    public List<List<Object>> getRetailReportHead(YearMonth start, YearMonth end) {
        List<List<Object>> retailReportModels = Lists.newArrayList();
        List<NameNumberDto> departmentList = Lists.newArrayList();
        departmentList.add(getAddDepartment());
        departmentList.addAll(glcxViewMapper.findDepartment());
        //表头--部门代码
        List<Object> itemDepartmentCode = Lists.newArrayList();
        itemDepartmentCode.add(CharEnum.SPACE.getValue());
        //计算起始月份和最后月份差
        Integer betweenMonth = Period.between(LocalDate.of(start.getYear(), start.getMonth(), 1), LocalDate.of(end.getYear(), end.getMonth(), 1)).getMonths() + 2;
        for (NameNumberDto department : departmentList) {
            itemDepartmentCode.add(department.getNumber());
            //占格为相差月份*2格+累计占格 2格
            for (int i = 0; i < 2 * betweenMonth - 1; i++) {
                itemDepartmentCode.add(CharEnum.SPACE.getValue());
            }
        }
        retailReportModels.add(itemDepartmentCode);
        //表头--部门名称
        List<Object> itemDepartmentName = Lists.newArrayList();
        itemDepartmentName.add(CharEnum.SPACE.getValue());
        for (NameNumberDto department : departmentList) {
            itemDepartmentName.add(department.getName());
            //占格为相差月份*2格+累计占格 2格
            for (int i = 0; i < 2 * betweenMonth - 1; i++) {
                itemDepartmentName.add(CharEnum.SPACE.getValue());
            }
        }
        retailReportModels.add(itemDepartmentName);
        //表头--时间
        List<Object> itemTime = Lists.newArrayList();
        itemTime.add(CharEnum.SPACE.getValue());
        for (NameNumberDto department : departmentList) {
            YearMonth tempStart = start;
            //占格  累计 2格 + 月份2格
            while (tempStart.isBefore(end) || tempStart.equals(end)) {
                itemTime.add(tempStart.format(DateTimeFormatter.ofPattern(DateFormat.MONTH.getValue())));
                itemTime.add(CharEnum.SPACE.getValue());
                tempStart = tempStart.plusMonths(1);
            }
            itemTime.add(ACCUMULATE);
            itemTime.add(CharEnum.SPACE.getValue());
        }
        retailReportModels.add(itemTime);
        //表头---金额+占比
        List<Object> itemAccountAndParent = Lists.newArrayList();
        itemAccountAndParent.add(K3CloudConstant.SUBJECT_NAME);
        for (NameNumberDto department : departmentList) {
            YearMonth tempStart = start;
            while (tempStart.isBefore(end) || tempStart.equals(end)) {
                itemAccountAndParent.add(K3CloudConstant.AMOUNT);
                itemAccountAndParent.add(K3CloudConstant.PROPORTION);
                tempStart = tempStart.plusMonths(1);
            }
            itemAccountAndParent.add(K3CloudConstant.AMOUNT);
            itemAccountAndParent.add(K3CloudConstant.PROPORTION);
        }
        retailReportModels.add(itemAccountAndParent);
        return retailReportModels;
    }

    //handsontable for nested head
    public List<List<NestedHeaderCell>> getNestedHeads(YearMonth start, YearMonth end) {
        List<NameNumberDto> departmentList = Lists.newArrayList();
        departmentList.add(getAddDepartment());
        departmentList.addAll(glcxViewMapper.findDepartment());
        List<List<NestedHeaderCell>> data = Lists.newArrayList();
        List<NestedHeaderCell> cellForDepartmentCodeList = Lists.newArrayList();
        cellForDepartmentCodeList.add(getNestedHeadCell("", 0));
        List<NestedHeaderCell> cellForDepartmentList = Lists.newArrayList();
        cellForDepartmentList.add(getNestedHeadCell("", 0));
        List<NestedHeaderCell> cellForMonthList = Lists.newArrayList();
        cellForMonthList.add(getNestedHeadCell("", 0));
        List<NestedHeaderCell> cellForAmountAndPercentList = Lists.newArrayList();
        cellForAmountAndPercentList.add(getNestedHeadCell(K3CloudConstant.SUBJECT_NAME, 0));
        for (NameNumberDto department : departmentList) {
            YearMonth tempStart = start;
            Integer betweenMonth = Period.between(LocalDate.of(start.getYear(), start.getMonth(), 1), LocalDate.of(end.getYear(), end.getMonth(), 1)).getMonths() + 2;
            cellForDepartmentCodeList.add(getNestedHeadCell(department.getNumber(), betweenMonth * 2));
            cellForDepartmentList.add(getNestedHeadCell(department.getName(), betweenMonth * 2));
            while (tempStart.isBefore(end.plusMonths(1))) {
                cellForMonthList.add(getNestedHeadCell(tempStart.format(DateTimeFormatter.ofPattern(DateFormat.MONTH.getValue())), 2));
                cellForAmountAndPercentList.add(getNestedHeadCell(K3CloudConstant.AMOUNT, 0));
                cellForAmountAndPercentList.add(getNestedHeadCell(K3CloudConstant.PROPORTION, 0));
                tempStart = tempStart.plusMonths(1);
            }
            cellForMonthList.add(getNestedHeadCell(K3CloudConstant.ACCUMULATE, 2));
            cellForAmountAndPercentList.add(getNestedHeadCell(K3CloudConstant.AMOUNT, 0));
            cellForAmountAndPercentList.add(getNestedHeadCell(K3CloudConstant.PROPORTION, 0));
        }
        data.add(cellForDepartmentCodeList);
        data.add(cellForDepartmentList);
        data.add(cellForMonthList);
        data.add(cellForAmountAndPercentList);
        return data;
    }

    private NestedHeaderCell getNestedHeadCell(String Label, Integer colSpan) {
        NestedHeaderCell nestedHeaderCell = new NestedHeaderCell();
        nestedHeaderCell.setLabel(Label);
        nestedHeaderCell.setColspan(colSpan);
        return nestedHeaderCell;
    }

}
