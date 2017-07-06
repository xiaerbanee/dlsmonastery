package net.myspring.cloud.modules.report.manager;

import com.google.common.collect.Lists;
import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.cloud.modules.kingdee.repository.GlcxViewRepository;
import net.myspring.cloud.modules.report.dto.NestedHeaderCellDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.util.List;

import static net.myspring.cloud.common.constant.KingdeeConstant.*;

/**
 * 零售报表工具类
 * Created by lihx on 2017/7/5.
 */
@Component
public class RetailAccountManager {
    @Autowired
    private GlcxViewRepository glcxViewRepository;
    
    //"合计"部门
    public BdDepartment getAddDepartment() {
        BdDepartment department = new BdDepartment();
        department.setFNumber(DOUBLE_ZERO);
        department.setFFullName(TOTAL_DEPARTMENT);
        return department;
    }

    //表头数据
    public List<List<Object>> getRetailReportHead(YearMonth start, YearMonth end) {
        List<List<Object>> retailReportModels = Lists.newArrayList();
        List<BdDepartment> departmentList = Lists.newArrayList();
        departmentList.add(getAddDepartment());
        departmentList.addAll(glcxViewRepository.findAllDepartment());
        //表头--部门代码
        List<Object> itemDepartmentCode = Lists.newArrayList();
        itemDepartmentCode.add(CharConstant.SPACE);
        //计算起始月份和最后月份差
        Integer betweenMonth = Period.between(LocalDate.of(start.getYear(), start.getMonth(), 1), LocalDate.of(end.getYear(), end.getMonth(), 1)).getMonths() + 2;
        for (BdDepartment department : departmentList) {
            itemDepartmentCode.add(department.getFNumber());
            //占格为相差月份*2格+累计占格 2格
            for (int i = 0; i < 2 * betweenMonth - 1; i++) {
                itemDepartmentCode.add(CharConstant.SPACE);
            }
        }
        retailReportModels.add(itemDepartmentCode);
        //表头--部门名称
        List<Object> itemDepartmentName = Lists.newArrayList();
        itemDepartmentName.add(CharConstant.SPACE);
        for (BdDepartment department : departmentList) {
            itemDepartmentName.add(department.getFFullName());
            //占格为相差月份*2格+累计占格 2格
            for (int i = 0; i < 2 * betweenMonth - 1; i++) {
                itemDepartmentName.add(CharConstant.SPACE);
            }
        }
        retailReportModels.add(itemDepartmentName);
        //表头--时间
        List<Object> itemTime = Lists.newArrayList();
        itemTime.add(CharConstant.SPACE);
        for (BdDepartment department : departmentList) {
            YearMonth tempStart = start;
            //占格  累计 2格 + 月份2格
            while (tempStart.isBefore(end) || tempStart.equals(end)) {
                itemTime.add(LocalDateUtils.format(tempStart));
                itemTime.add(CharConstant.SPACE);
                tempStart = tempStart.plusMonths(1);
            }
            itemTime.add(ACCUMULATE);
            itemTime.add(CharConstant.SPACE);
        }
        retailReportModels.add(itemTime);
        //表头---金额+占比
        List<Object> itemAccountAndParent = Lists.newArrayList();
        itemAccountAndParent.add(SUBJECT_NAME);
        for (BdDepartment department : departmentList) {
            YearMonth tempStart = start;
            while (tempStart.isBefore(end) || tempStart.equals(end)) {
                itemAccountAndParent.add(AMOUNT);
                itemAccountAndParent.add(PROPORTION);
                tempStart = tempStart.plusMonths(1);
            }
            itemAccountAndParent.add(AMOUNT);
            itemAccountAndParent.add(PROPORTION);
        }
        retailReportModels.add(itemAccountAndParent);
        return retailReportModels;
    }

    //嵌套表头数据
    public List<List<NestedHeaderCellDto>> getNestedHeaders(YearMonth start, YearMonth end) {
        List<BdDepartment> departmentList = Lists.newArrayList();
        departmentList.add(getAddDepartment());
        departmentList.addAll(glcxViewRepository.findAllDepartment());
        List<List<NestedHeaderCellDto>> data = Lists.newArrayList();
        List<NestedHeaderCellDto> cellForDepartmentCodeList = Lists.newArrayList();
        cellForDepartmentCodeList.add(getNestedHeaderCellDto("", 0));
        List<NestedHeaderCellDto> cellForDepartmentList = Lists.newArrayList();
        cellForDepartmentList.add(getNestedHeaderCellDto("", 0));
        List<NestedHeaderCellDto> cellForMonthList = Lists.newArrayList();
        cellForMonthList.add(getNestedHeaderCellDto("", 0));
        List<NestedHeaderCellDto> cellForAmountAndPercentList = Lists.newArrayList();
        cellForAmountAndPercentList.add(getNestedHeaderCellDto(SUBJECT_NAME, 0));
        for (BdDepartment department : departmentList) {
            YearMonth tempStart = start;
            Integer betweenMonth = Period.between(LocalDate.of(start.getYear(), start.getMonth(), 1), LocalDate.of(end.getYear(), end.getMonth(), 1)).getMonths() + 2;
            cellForDepartmentCodeList.add(getNestedHeaderCellDto(department.getFNumber(), betweenMonth * 2));
            cellForDepartmentList.add(getNestedHeaderCellDto(department.getFFullName(), betweenMonth * 2));
            while (tempStart.isBefore(end.plusMonths(1))) {
                cellForMonthList.add(getNestedHeaderCellDto(LocalDateUtils.format(tempStart), 2));
                cellForAmountAndPercentList.add(getNestedHeaderCellDto(AMOUNT, 0));
                cellForAmountAndPercentList.add(getNestedHeaderCellDto(PROPORTION, 0));
                tempStart = tempStart.plusMonths(1);
            }
            cellForMonthList.add(getNestedHeaderCellDto(ACCUMULATE, 2));
            cellForAmountAndPercentList.add(getNestedHeaderCellDto(AMOUNT, 0));
            cellForAmountAndPercentList.add(getNestedHeaderCellDto(PROPORTION, 0));
        }
        data.add(cellForDepartmentCodeList);
        data.add(cellForDepartmentList);
        data.add(cellForMonthList);
        data.add(cellForAmountAndPercentList);
        return data;
    }

    private NestedHeaderCellDto getNestedHeaderCellDto(String Label, Integer colSpan) {
        NestedHeaderCellDto nestedHeaderCell = new NestedHeaderCellDto();
        nestedHeaderCell.setLabel(Label);
        nestedHeaderCell.setColspan(colSpan);
        return nestedHeaderCell;
    }
}
