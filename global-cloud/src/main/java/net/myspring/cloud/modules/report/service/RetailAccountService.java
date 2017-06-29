package net.myspring.cloud.modules.report.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.dto.TreeDto;
import net.myspring.cloud.common.enums.*;

import net.myspring.cloud.modules.kingdee.domain.BdDepartment;
import net.myspring.cloud.modules.kingdee.repository.BdDepartmentRepository;
import net.myspring.cloud.modules.kingdee.repository.GlcxViewRepository;
import net.myspring.cloud.modules.kingdee.repository.SalOutStockRepository;
import net.myspring.cloud.modules.kingdee.repository.SalReturnStockRepository;
import net.myspring.cloud.modules.report.dto.RetailAccountDto;
import net.myspring.cloud.modules.report.web.query.RetailAccountQuery;
import net.myspring.common.constant.CharConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.myspring.cloud.common.constant.KingdeeConstant.DOUBLE_ZERO;
import static net.myspring.cloud.common.constant.KingdeeConstant.TOTAL_DEPARTMENT;

/**
 * 零售费用报表
 * Created by lihx on 2017/6/7.
 */
@Service
@KingdeeDataSource
@Transactional
public class RetailAccountService {
    @Autowired
    private GlcxViewRepository glcxViewRepository;
    @Autowired
    private SalOutStockRepository salOutStockRepository;
    @Autowired
    private SalReturnStockRepository salReturnStockRepository;
    @Autowired
    private BdDepartmentRepository bdDepartmentRepository;

    //"合计"部门
    public BdDepartment getAddDepartment() {
        BdDepartment department = new BdDepartment();
        department.setFNumber(DOUBLE_ZERO);
        department.setFFullName(TOTAL_DEPARTMENT);
        return department;
    }

    //页面导出纵向显示数据
    public List<RetailAccountDto> getRetailReport2(RetailAccountQuery retailAccountQuery){
        YearMonth start = retailAccountQuery.getMonthStart();
        YearMonth end = retailAccountQuery.getMonthEnd();
        List<RetailAccountDto> retailOrWholeSaleReports = Lists.newArrayList();
        retailOrWholeSaleReports.addAll(findAmountAndPercentForAddDepartment(retailAccountQuery));
        retailOrWholeSaleReports.addAll(findSumAmountAndSumPercentForAddDepartment( start,  end));
        retailOrWholeSaleReports.addAll(findAmountAndPercentForDepartments(start, end));
        retailOrWholeSaleReports.addAll(findSumAmountAndSumPercentForDepartments(start, end));
        return retailOrWholeSaleReports;
    }
    //页面显示数据
    public List<List<Object>> getRetailReport(RetailAccountQuery retailAccountQuery) {
        List<List<Object>> retailReportModels = Lists.newArrayList();
        YearMonth start = retailAccountQuery.getMonthStart();
        YearMonth end = retailAccountQuery.getMonthEnd();
        List<BdDepartment> departmentList = Lists.newArrayList();
        departmentList.add(getAddDepartment());
        departmentList.addAll(bdDepartmentRepository.findAll());
        List<RetailAccountDto> itemDataList = findAmountAndPercentForAddDepartment(retailAccountQuery);
        itemDataList.addAll(findAmountAndPercentForDepartments(start, end));
        Map<String, RetailAccountDto> retailReportItemMap = Maps.newHashMap();
        for (RetailAccountDto tempItemData : itemDataList) {
            String key = tempItemData.getAccName() + CharConstant.UNDER_LINE + tempItemData.getFyNum() + CharConstant.UNDER_LINE + tempItemData.getDeptName() + CharConstant.UNDER_LINE + tempItemData.getYear() + CharConstant.UNDER_LINE + tempItemData.getMonth();
            if (!retailReportItemMap.containsKey(key)) {
                retailReportItemMap.put(key, tempItemData);
            }
        }
        //获取累计金额，占比
        List<RetailAccountDto> sumItemDataList = findSumAmountAndSumPercentForAddDepartment(start, end);
        sumItemDataList.addAll(findSumAmountAndSumPercentForDepartments(start, end));
        Map<String, RetailAccountDto> sumRetailReportItemMap = Maps.newHashMap();
        for (RetailAccountDto sumDirectShopDetail : sumItemDataList) {
            String key = sumDirectShopDetail.getAccName() + CharConstant.UNDER_LINE + sumDirectShopDetail.getFyNum() + CharConstant.UNDER_LINE +sumDirectShopDetail.getDeptName();
            if (!sumRetailReportItemMap.containsKey(key)) {
                sumRetailReportItemMap.put(key, sumDirectShopDetail);
            }
        }
        //数据
        for (Map.Entry<String,TreeDto> showEnum : showEnum().entrySet()) {
            List<Object> item = Lists.newArrayList();
            item.add(showEnum.getKey());
            for (BdDepartment department : departmentList) {
                YearMonth tempStart = start;
                while (tempStart.isBefore(end) || tempStart.equals(end)) {
                    int year = tempStart.getYear();
                    int month = tempStart.getMonthValue();
                    String key = showEnum.getValue().getParent() + CharConstant.UNDER_LINE +showEnum.getValue().getChild()+CharConstant.UNDER_LINE + department.getFFullName() + CharConstant.UNDER_LINE + year + CharConstant.UNDER_LINE + month;
                    if (retailReportItemMap.containsKey(key)) {
                        item.add(retailReportItemMap.get(key).getAmount());
                        item.add(retailReportItemMap.get(key).getPercent());
                    } else {
                        item.add(0);
                        item.add(0);
                    }
                    tempStart = tempStart.plusMonths(1);
                }
                //累计
                String key2 = showEnum.getValue().getParent() + CharConstant.UNDER_LINE + showEnum.getValue().getChild() + CharConstant.UNDER_LINE + department.getFFullName();
                if (sumRetailReportItemMap.containsKey(key2))  {
                    item.add(sumRetailReportItemMap.get(key2).getAmount());
                    item.add(sumRetailReportItemMap.get(key2).getPercent());
                } else {
                    item.add(0);
                    item.add(0);
                }
            }
            retailReportModels.add(item);
        }
        return retailReportModels;
    }


    //"合计" (金额+占比)
    private List<RetailAccountDto> findAmountAndPercentForAddDepartment(RetailAccountQuery retailAccountQuery) {
        List<RetailAccountDto> itemDataList = Lists.newArrayList();
        YearMonth dateStart = retailAccountQuery.getMonthStart();
        YearMonth dateEnd = retailAccountQuery.getMonthEnd();
        while (dateStart.isBefore(dateEnd) || dateStart.equals(dateEnd)) {
            int year = dateStart.getYear();
            int month = dateStart.getMonthValue();
            List<String> accNameForIncomeList = Arrays.stream(RetailAccountForIncomeEnum.values()).map(RetailAccountForIncomeEnum::getAccName).collect(Collectors.toList());
            List<String> fyNumForIncomeList = Arrays.stream(RetailAccountForIncomeEnum.values()).map(RetailAccountForIncomeEnum::getFyNum).collect(Collectors.toList());
            List<RetailAccountDto> incomeForAddDepartmentList = glcxViewRepository.findEntityByPeriodForTotalDepartment(year,month,accNameForIncomeList,fyNumForIncomeList);

            List<String> accNameForCostList = Arrays.stream(RetailAccountForCostEnum.values()).map(RetailAccountForCostEnum::getAccName).collect(Collectors.toList());
            List<String> fyNumForCostList = Arrays.stream(RetailAccountForCostEnum.values()).map(RetailAccountForCostEnum::getFyNum).collect(Collectors.toList());
            List<RetailAccountDto> costForAddDepartmentList = glcxViewRepository.findEntityByPeriodForTotalDepartment(year,month,accNameForCostList,fyNumForCostList);

            List<RetailAccountDto> managementFeeForAddDepartmentList = glcxViewRepository.findEntityByPeriodForTotalDepartment(year,month,"管理费用");
            List<RetailAccountDto> xsckdQtyForAddDepartmentList =  salOutStockRepository.findByPeriodForTotalDepartment(year, month);
            List<RetailAccountDto> xsthdQtyForAddDepartmentList =  salReturnStockRepository.findByPeriodForTotalDepartment(year, month);
            itemDataList.addAll(getSalesProductQtyList(xsckdQtyForAddDepartmentList,xsthdQtyForAddDepartmentList));
            itemDataList.addAll(getSubjectFeeEntityItem(incomeForAddDepartmentList,costForAddDepartmentList,managementFeeForAddDepartmentList));
            dateStart = dateStart.plusMonths(1);
        }
        return itemDataList;
    }

    //"合计" 累计金额+累计占比
    private List<RetailAccountDto> findSumAmountAndSumPercentForAddDepartment(YearMonth start, YearMonth end) {
        Integer startDate = start.getYear() * 100 + start.getMonthValue();
        Integer endDate = end.getYear() * 100 + end.getMonthValue();

        List<String> accNameForIncomeList = Arrays.stream(RetailAccountForIncomeEnum.values()).map(RetailAccountForIncomeEnum::getAccName).collect(Collectors.toList());
        List<String> fyNumForIncomeList = Arrays.stream(RetailAccountForIncomeEnum.values()).map(RetailAccountForIncomeEnum::getFyNum).collect(Collectors.toList());
        List<RetailAccountDto> incomeForAddDepartmentList = glcxViewRepository.findEntityBySumPeriodForTotalDepartment(startDate,endDate,accNameForIncomeList,fyNumForIncomeList);

        List<String> accNameForCostList = Arrays.stream(RetailAccountForCostEnum.values()).map(RetailAccountForCostEnum::getAccName).collect(Collectors.toList());
        List<String> fyNumForCostList = Arrays.stream(RetailAccountForCostEnum.values()).map(RetailAccountForCostEnum::getFyNum).collect(Collectors.toList());
        List<RetailAccountDto> costForAddDepartmentList = glcxViewRepository.findEntityBySumPeriodForTotalDepartment(startDate,endDate,accNameForCostList,fyNumForCostList);

        List<RetailAccountDto> managementFeeForAddDepartmentList = glcxViewRepository.findEntityBySumPeriodForTotalDepartment(startDate,endDate,"管理费用");
        List<RetailAccountDto> xsckdQtyForAddDepartmentList =  salOutStockRepository.findBySumPeriodForTotalDepartment(startDate, endDate);
        List<RetailAccountDto> xsthdQtyForAddDepartmentList =  salReturnStockRepository.findBySumPeriodForTotalDepartment(startDate, endDate);

        List<RetailAccountDto> list = Lists.newArrayList(getSalesProductQtyList(xsckdQtyForAddDepartmentList,xsthdQtyForAddDepartmentList));
        list.addAll(getSubjectFeeEntityItem(incomeForAddDepartmentList,costForAddDepartmentList,managementFeeForAddDepartmentList));
        return list;
    }

    //各个部门 金额+占比
    private List<RetailAccountDto> findAmountAndPercentForDepartments(YearMonth dateStart, YearMonth dateEnd) {
        List<RetailAccountDto> itemDataList = Lists.newArrayList();
        while (dateStart.isBefore(dateEnd) || dateStart.equals(dateEnd)) {
            int year = dateStart.getYear();
            int month = dateStart.getMonthValue();
            List<String> accNameForIncomeList = Arrays.stream(RetailAccountForIncomeEnum.values()).map(RetailAccountForIncomeEnum::getAccName).collect(Collectors.toList());
            List<String> fyNumForIncomeList = Arrays.stream(RetailAccountForIncomeEnum.values()).map(RetailAccountForIncomeEnum::getFyNum).collect(Collectors.toList());
            List<RetailAccountDto> incomeList = glcxViewRepository.findEntityByPeriod(year,month,accNameForIncomeList,fyNumForIncomeList);

            List<String> accNameForCostList = Arrays.stream(RetailAccountForCostEnum.values()).map(RetailAccountForCostEnum::getAccName).collect(Collectors.toList());
            List<String> fyNumForCostList = Arrays.stream(RetailAccountForCostEnum.values()).map(RetailAccountForCostEnum::getFyNum).collect(Collectors.toList());
            List<RetailAccountDto> costList = glcxViewRepository.findEntityByPeriod(year,month,accNameForCostList,fyNumForCostList);

            List<RetailAccountDto> managementFeeList = glcxViewRepository.findEntityByPeriod(year,month,"管理费用");
            List<RetailAccountDto> xsckdQuantity =  salOutStockRepository.findByPeriod(year, month);
            List<RetailAccountDto> xsthdQuantity =  salReturnStockRepository.findByPeriod(year, month);
            itemDataList.addAll(getSalesProductQtyList(xsckdQuantity,xsthdQuantity));
            itemDataList.addAll(getSubjectFeeEntityItem(incomeList,costList,managementFeeList));
            dateStart = dateStart.plusMonths(1);
        }
        return itemDataList;
    }

    //各个部门 累计金额+累计占比
    private List<RetailAccountDto> findSumAmountAndSumPercentForDepartments(YearMonth start, YearMonth end) {
        Integer startDate = start.getYear() * 100 + start.getMonthValue();
        Integer endDate = end.getYear() * 100 + end.getMonthValue();
        List<String> accNameForIncomeList = Arrays.stream(RetailAccountForIncomeEnum.values()).map(RetailAccountForIncomeEnum::getAccName).collect(Collectors.toList());
        List<String> fyNumForIncomeList = Arrays.stream(RetailAccountForIncomeEnum.values()).map(RetailAccountForIncomeEnum::getFyNum).collect(Collectors.toList());
        List<RetailAccountDto> incomeList = glcxViewRepository.findEntityByPeriod(startDate,endDate,accNameForIncomeList,fyNumForIncomeList);

        List<String> accNameForCostList = Arrays.stream(RetailAccountForCostEnum.values()).map(RetailAccountForCostEnum::getAccName).collect(Collectors.toList());
        List<String> fyNumForCostList = Arrays.stream(RetailAccountForCostEnum.values()).map(RetailAccountForCostEnum::getFyNum).collect(Collectors.toList());
        List<RetailAccountDto> costList = glcxViewRepository.findEntityByPeriod(startDate,endDate,accNameForCostList,fyNumForCostList);

        List<RetailAccountDto> managementFeeList = glcxViewRepository.findEntityBySumPeriod(startDate,endDate,"管理费用");
        List<RetailAccountDto> xsckdQuantity =  salOutStockRepository.findBySumPeriod(startDate, endDate);
        List<RetailAccountDto> xsthdQuantity =  salReturnStockRepository.findBySumPeriod(startDate, endDate);
        List<RetailAccountDto> list = Lists.newArrayList(getSalesProductQtyList(xsckdQuantity,xsthdQuantity));
        list.addAll(getSubjectFeeEntityItem(incomeList, costList,managementFeeList));
        return list;
    }

    private List<RetailAccountDto> getSubjectFeeEntityItem(List<RetailAccountDto> incomeList, List<RetailAccountDto> costList, List<RetailAccountDto> managementFeeList) {
        List<RetailAccountDto> itemDataList = Lists.newArrayList();
        itemDataList.addAll(incomeList);
        List<RetailAccountDto> incomeTotalList = getIncomeTotalList(incomeList);
        itemDataList.addAll(incomeTotalList);
        //对分期服务费处理
        getNewCostList(costList,managementFeeList);
        itemDataList.addAll(costList);
        List<RetailAccountDto> costTotalList = getCostTotalList(costList);
        itemDataList.addAll(costTotalList);
        List<RetailAccountDto> specialManagementFeeList = getSpecialManagementFeeList(managementFeeList);
        itemDataList.addAll(specialManagementFeeList);
        List<RetailAccountDto> netSalesRevenueList = getNetSalesRevenueList(incomeList,specialManagementFeeList);
        itemDataList.addAll(netSalesRevenueList);
        List<RetailAccountDto> totalGrossProfitList = getTotalGrossProfitList(incomeTotalList,costTotalList,specialManagementFeeList);
        itemDataList.addAll(totalGrossProfitList);
        List<RetailAccountDto> mobileGrossProfitList = getMobileGrossProfitList(incomeList,costList,specialManagementFeeList);
        itemDataList.addAll(getAddPercentList(mobileGrossProfitList,netSalesRevenueList));//netSalesRevenueList
        List<RetailAccountDto> accessoryGrossProfitList = getAccessoryGrossProfitList(incomeList,costList);
        itemDataList.addAll(getAddPercentList(accessoryGrossProfitList,netSalesRevenueList));//netSalesRevenueList
        List<RetailAccountDto> operatingCommissionGrossProfitList = getOperatingCommissionGrossProfitList(incomeList,costList);
        itemDataList.addAll(getAddPercentList(operatingCommissionGrossProfitList,netSalesRevenueList));//netSalesRevenueList
        List<RetailAccountDto> valueAddServiceProfitProfitList = getValueAddServiceProfitProfitList(incomeList,costList);
        itemDataList.addAll(getAddPercentList(valueAddServiceProfitProfitList,netSalesRevenueList));//netSalesRevenueList
        //费用1
        List<RetailAccountDto> managementFeeForFirstList = getManagementFeeForFirstList(managementFeeList,netSalesRevenueList);
        itemDataList.addAll(managementFeeForFirstList);
        List<RetailAccountDto> totalDailyOperatingExpensesList = getTotalDailyOperatingExpensesList(managementFeeForFirstList);
        itemDataList.addAll(getAddPercentList(totalDailyOperatingExpensesList,netSalesRevenueList));
        List<RetailAccountDto> adManagementFeeList = getADManagementFeeList(managementFeeList,netSalesRevenueList);
        itemDataList.addAll(adManagementFeeList);
        List<RetailAccountDto> operationCostSummaryList = getOperationCostSummary(adManagementFeeList,totalDailyOperatingExpensesList);
        itemDataList.addAll(getAddPercentList(operationCostSummaryList,netSalesRevenueList));
        List<RetailAccountDto> netProfitForFirstList = getNetProfitForFirst(totalGrossProfitList,operationCostSummaryList);
        itemDataList.addAll(getAddPercentList(netProfitForFirstList,netSalesRevenueList));
        //费用2
        List<RetailAccountDto> managementFeeForSecondList = getManagementFeeForSecondList(managementFeeList);
        itemDataList.addAll(managementFeeForSecondList);
        List<RetailAccountDto> netProfitForSecondList = getNetProfitForSecondList(managementFeeForSecondList);
        itemDataList.addAll(netProfitForSecondList);
        List<RetailAccountDto> netProfitForSumList = getNetProfitForSumList(netProfitForFirstList,netProfitForSecondList);
        itemDataList.addAll(netProfitForSumList);

        return itemDataList;
    }
    /**
     * 销售数量
     */
    private List<RetailAccountDto> getSalesProductQtyList(List<RetailAccountDto> xsckdQtyList, List<RetailAccountDto> xsthdQtyList){
        List<RetailAccountDto> itemDataList = Lists.newArrayList();
        List<String> deptNumList = xsckdQtyList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList());
        deptNumList.addAll(xsthdQtyList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList()));
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        for(String  deptNum : deptNumSet) {
            boolean flag = true;
            boolean flag2 = true;
            BigDecimal qty = BigDecimal.ZERO;
            RetailAccountDto tempItemData = new RetailAccountDto();
            tempItemData.setFyName(RetailAccountReportEnum.sales_mobile_qty.getFyName());
            tempItemData.setFyNum(RetailAccountReportEnum.sales_mobile_qty.getFyNum());
            tempItemData.setAccName(RetailAccountReportEnum.sales_mobile_qty.getAccName());
            for (RetailAccountDto xsthd : xsthdQtyList) {
                if (xsthd.getDeptNum().equals(deptNum)) {
                    flag2 = false;
                    tempItemData.setYear(xsthd.getYear());
                    tempItemData.setMonth(xsthd.getMonth());
                    tempItemData.setDeptNum(xsthd.getDeptNum());
                    tempItemData.setDeptName(xsthd.getDeptName());
                    qty = xsthd.getAmount();
                    break;
                }
            }
            for (RetailAccountDto xsckd : xsckdQtyList) {
                if (xsckd.getDeptNum().equals(deptNum)) {
                    flag = false;
                    if (flag2) {
                        tempItemData.setYear(xsckd.getYear());
                        tempItemData.setMonth(xsckd.getMonth());
                        tempItemData.setDeptNum(xsckd.getDeptNum());
                        tempItemData.setDeptName(xsckd.getDeptName());
                    }
                    qty = xsckd.getAmount().subtract(qty);
                }
            }
            if (flag) {
                qty = BigDecimal.ZERO.subtract(qty);
            }
            tempItemData.setAmount(qty);
            itemDataList.add(tempItemData);
        }
        return itemDataList;
    }
    /**
     * 净利润1=总毛利润-运营费用汇总
     * 占比: 之差
     */
    private List<RetailAccountDto> getNetProfitForFirst(List<RetailAccountDto> totalGrossProfitList, List<RetailAccountDto> operationCostSummaryList) {
        List<RetailAccountDto> itemDataList = Lists.newArrayList();
        List<String> deptNumList = totalGrossProfitList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList());
        deptNumList.addAll(operationCostSummaryList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList()));
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        for(String  deptNum : deptNumSet) {
            boolean flag = true;
            boolean flag2 = true;
            BigDecimal amount = BigDecimal.ZERO;
            RetailAccountDto tempItemData = new RetailAccountDto();
            tempItemData.setFyName(RetailAccountReportEnum.net_profit_1.getFyName());
            tempItemData.setFyNum(RetailAccountReportEnum.net_profit_1.getFyNum());
            tempItemData.setAccName(RetailAccountReportEnum.net_profit_1.getAccName());
            for (RetailAccountDto operationCostSummary : operationCostSummaryList) {
                if (operationCostSummary.getDeptNum().equals(deptNum)) {
                    flag2 = false;
                    tempItemData.setYear(operationCostSummary.getYear());
                    tempItemData.setMonth(operationCostSummary.getMonth());
                    tempItemData.setDeptNum(operationCostSummary.getDeptNum());
                    tempItemData.setDeptName(operationCostSummary.getDeptName());
                    amount = operationCostSummary.getAmount();
                    break;
                }
            }
            for (RetailAccountDto totalGrossProfit : totalGrossProfitList) {
                if (totalGrossProfit.getDeptNum().equals(deptNum)) {
                    flag = false;
                    if (flag2) {
                        tempItemData.setYear(totalGrossProfit.getYear());
                        tempItemData.setMonth(totalGrossProfit.getMonth());
                        tempItemData.setDeptNum(totalGrossProfit.getDeptNum());
                        tempItemData.setDeptName(totalGrossProfit.getDeptName());
                    }
                    amount = totalGrossProfit.getAmount().subtract(amount);
                }
            }
            if (flag) {
                amount = BigDecimal.ZERO.subtract(amount);
            }
            tempItemData.setAmount(amount);
            itemDataList.add(tempItemData);
        }
        return itemDataList;
    }
    /**
     * 净利润2=管理费用_2之和
     * 占比:0
     */
    private List<RetailAccountDto> getNetProfitForSecondList(List<RetailAccountDto> managementFeeForSecondList){
        List<String> deptNumList = managementFeeForSecondList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList());
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        List<RetailAccountDto> netProfitForSecondList = Lists.newArrayList();
        for(String deptNum : deptNumSet){
            RetailAccountDto subjectUnitEntity = new RetailAccountDto();
            subjectUnitEntity.setFyName(RetailAccountReportEnum.net_profit_2.getFyName());
            subjectUnitEntity.setFyNum(RetailAccountReportEnum.net_profit_2.getFyNum());
            subjectUnitEntity.setAccName(RetailAccountReportEnum.net_profit_2.getAccName());
            BigDecimal amount = BigDecimal.ZERO;
            for(RetailAccountDto cost : managementFeeForSecondList){
                if (cost.getDeptNum().equals(deptNum)){
                    subjectUnitEntity.setDeptNum(cost.getDeptNum());
                    subjectUnitEntity.setDeptName(cost.getDeptName());
                    subjectUnitEntity.setYear(cost.getYear());
                    subjectUnitEntity.setMonth(cost.getMonth());
                    amount = amount.add(cost.getAmount());
                }
            }
            subjectUnitEntity.setAmount(amount);
            netProfitForSecondList.add(subjectUnitEntity);
        }
        return netProfitForSecondList;
    }
    /**
     * 净利润合计=净利润1-净利润2
     * 占比:0
     */
    private List<RetailAccountDto> getNetProfitForSumList(List<RetailAccountDto> netProfitForFirstList,List<RetailAccountDto> netProfitForSecondList){
        List<RetailAccountDto> itemDataList = Lists.newArrayList();
        Map<String, RetailAccountDto> netProfitForSecondMap = netProfitForSecondList.stream().collect(Collectors.toMap(RetailAccountDto::getDeptNum,RetailAccountDto->RetailAccountDto));
        for (RetailAccountDto netProfitForFirst : netProfitForFirstList) {
            BigDecimal amount = BigDecimal.ZERO;
            RetailAccountDto tempItemData = new RetailAccountDto();
            tempItemData.setYear(netProfitForFirst.getYear());
            tempItemData.setMonth(netProfitForFirst.getMonth());
            tempItemData.setFyName(RetailAccountReportEnum.net_profit_sum.getFyName());
            tempItemData.setFyNum(RetailAccountReportEnum.net_profit_sum.getFyNum());
            tempItemData.setAccName(RetailAccountReportEnum.net_profit_sum.getAccName());
            tempItemData.setDeptName(netProfitForFirst.getDeptName());
            tempItemData.setDeptNum(netProfitForFirst.getDeptNum());
            if (netProfitForSecondMap.get(netProfitForFirst.getDeptNum()) != null) {
                amount = netProfitForSecondMap.get(netProfitForFirst.getDeptNum()).getAmount();
            }
            tempItemData.setAmount(netProfitForFirst.getAmount().subtract(amount));
            itemDataList.add(tempItemData);
        }
        return itemDataList;
    }
    /**
     * 运营费用汇总=日常运营费用合计+广告费
     * 占比: 之和
     */
    private List<RetailAccountDto> getOperationCostSummary(List<RetailAccountDto> adManagementFeeList, List<RetailAccountDto> totalDailyOperatingExpensesList) {
        List<RetailAccountDto> itemDataList = Lists.newArrayList();
        Map<String, RetailAccountDto> detailCostsADMap = adManagementFeeList.stream().collect(Collectors.toMap(RetailAccountDto::getDeptNum,RetailAccountDto->RetailAccountDto));
        for (RetailAccountDto totalDailyOperatingExpenses : totalDailyOperatingExpensesList) {
            BigDecimal amount = BigDecimal.ZERO;
            RetailAccountDto tempItemData = new RetailAccountDto();
            tempItemData.setYear(totalDailyOperatingExpenses.getYear());
            tempItemData.setMonth(totalDailyOperatingExpenses.getMonth());
            tempItemData.setFyName(RetailAccountReportEnum.operation_cost_summary.getFyName());
            tempItemData.setFyNum(RetailAccountReportEnum.operation_cost_summary.getFyNum());
            tempItemData.setAccName(RetailAccountReportEnum.operation_cost_summary.getAccName());
            tempItemData.setDeptName(totalDailyOperatingExpenses.getDeptName());
            tempItemData.setDeptNum(totalDailyOperatingExpenses.getDeptNum());
            if (detailCostsADMap.get(totalDailyOperatingExpenses.getDeptNum()) != null) {
                amount = detailCostsADMap.get(totalDailyOperatingExpenses.getDeptNum()).getAmount();
            }
            tempItemData.setAmount(totalDailyOperatingExpenses.getAmount().add(amount));
            itemDataList.add(tempItemData);
        }
        return itemDataList;
    }
    /**
     * 管理费用--广告
     * 占比=？
     */
    private List<RetailAccountDto> getADManagementFeeList(List<RetailAccountDto> managementFeeList, List<RetailAccountDto> netSalesRevenueList){
        List<RetailAccountDto> adManagementFeeList = Lists.newArrayList();
        for(RetailAccountDto subjectUnitEntity : managementFeeList){
            if(subjectUnitEntity.getFyNum().equals(RetailAccountReportEnum.advertising_fee.getFyNum())){
                boolean flag = true;
                for (RetailAccountDto netSalesRevenue : netSalesRevenueList) {
                    if (netSalesRevenue.getDeptName().equals(subjectUnitEntity.getDeptName())) {
                        flag = false;
                        if (netSalesRevenue.getAmount().intValue() == 0) {
                            subjectUnitEntity.setPercent(BigDecimal.ZERO);
                        } else {
                            subjectUnitEntity.setPercent(subjectUnitEntity.getAmount().divide(netSalesRevenue.getAmount(), 4, BigDecimal.ROUND_HALF_UP));
                        }
                        adManagementFeeList.add(subjectUnitEntity);
                        break;
                    }
                }
                if (flag) {
                    subjectUnitEntity.setPercent(BigDecimal.ZERO);
                    adManagementFeeList.add(subjectUnitEntity);
                }
            }
        }
        return adManagementFeeList;
    }
    /**
     * 日常运营费用合计=管理费用之和
     * 占比=？
     */
    private List<RetailAccountDto> getTotalDailyOperatingExpensesList(List<RetailAccountDto> generalManagementFeeList){
        List<String> deptNumList = generalManagementFeeList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList());
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        List<RetailAccountDto> costTotalList = Lists.newArrayList();
        for(String deptNum : deptNumSet){
            RetailAccountDto subjectUnitEntity = new RetailAccountDto();
            subjectUnitEntity.setFyName(RetailAccountReportEnum.daily_operating_expenses_total.getFyName());
            subjectUnitEntity.setFyNum(RetailAccountReportEnum.daily_operating_expenses_total.getFyNum());
            subjectUnitEntity.setAccName(RetailAccountReportEnum.daily_operating_expenses_total.getAccName());
            BigDecimal amount = BigDecimal.ZERO;
            for(RetailAccountDto cost : generalManagementFeeList){
                if (cost.getDeptNum().equals(deptNum)){
                    subjectUnitEntity.setDeptNum(cost.getDeptNum());
                    subjectUnitEntity.setDeptName(cost.getDeptName());
                    subjectUnitEntity.setYear(cost.getYear());
                    subjectUnitEntity.setMonth(cost.getMonth());
                    amount = amount.add(cost.getAmount());
                }
            }
            subjectUnitEntity.setAmount(amount);
            costTotalList.add(subjectUnitEntity);
        }
        return costTotalList;
    }
    /**
     * 管理费用_1
     * 占比=？
     */
    private List<RetailAccountDto> getManagementFeeForFirstList(List<RetailAccountDto> managementFeeList, List<RetailAccountDto> netSalesRevenueList){
        List<RetailAccountDto> generalManagementFeeList = Lists.newArrayList();
        List<RetailAccountDto> managementFeeForFirstList = Lists.newArrayList();
        for(RetailAccountDto item : managementFeeList){
            for (RetailAccountForManagerExpenses1Enum enum1 : RetailAccountForManagerExpenses1Enum.values()){
                if (item.getFyNum().equals(enum1.getFyNum())){
                    managementFeeForFirstList.add(item);
                }
            }
        }
        for(RetailAccountDto subjectUnitEntity : managementFeeForFirstList){
            boolean flag = true;
            for (RetailAccountDto netSalesRevenue : netSalesRevenueList) {
                if (netSalesRevenue.getDeptName().equals(subjectUnitEntity.getDeptName())) {
                    flag = false;
                    if (netSalesRevenue.getAmount().intValue() == 0) {
                        subjectUnitEntity.setPercent(BigDecimal.ZERO);
                    } else {
                        subjectUnitEntity.setPercent(subjectUnitEntity.getAmount().divide(netSalesRevenue.getAmount(), 4, BigDecimal.ROUND_HALF_UP));
                    }
                    generalManagementFeeList.add(subjectUnitEntity);
                    break;
                }
            }
            if (flag) {
                subjectUnitEntity.setPercent(BigDecimal.ZERO);
                generalManagementFeeList.add(subjectUnitEntity);
            }
        }
        return generalManagementFeeList;
    }
    /**
     * 管理费用_2
     * 占比=？
     */
    private List<RetailAccountDto> getManagementFeeForSecondList(List<RetailAccountDto> managementFeeList){
        List<RetailAccountDto> managementFeeForFirstList = Lists.newArrayList();
        for(RetailAccountDto item : managementFeeList){
            for (RetailAccountForManagerExpenses2Enum enum2 : RetailAccountForManagerExpenses2Enum.values()){
                if (item.getFyNum().equals(enum2.getFyNum())){
                    managementFeeForFirstList.add(item);
                }
            }
        }
        return managementFeeForFirstList;
    }

    /**
     * 增值业务利润=增值业务收入-(增值业务成本)
     * 占比=？
     */
    private List<RetailAccountDto> getValueAddServiceProfitProfitList(List<RetailAccountDto> incomeList, List<RetailAccountDto> costList){
        List<RetailAccountDto> itemDataList = Lists.newArrayList();
        List<RetailAccountDto> valueAddServiceIncomeList = Lists.newArrayList();
        for(RetailAccountDto cost : incomeList){
            if(cost.getFyNum().equals(RetailAccountForIncomeEnum.valueAdd_service_income.getFyNum())){
                valueAddServiceIncomeList.add(cost);
            }
        }
        List<String> deptNumList = valueAddServiceIncomeList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList());
        List<RetailAccountDto> valueAddServiceCostList = Lists.newArrayList();
        for(RetailAccountDto cost : costList){
            if(cost.getFyNum().equals(RetailAccountForCostEnum.valueAdd_service_cost.getFyNum())){
                valueAddServiceCostList.add(cost);
            }
        }
        deptNumList.addAll(valueAddServiceCostList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList()));
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        for(String deptNum : deptNumSet){
            boolean flag = true;
            boolean flag2 = true;
            BigDecimal amount = BigDecimal.ZERO;
            RetailAccountDto accessoryGrossProfit = new RetailAccountDto();
            accessoryGrossProfit.setFyName(RetailAccountReportEnum.valueAdd_service_profit.getFyName());
            accessoryGrossProfit.setFyNum(RetailAccountReportEnum.valueAdd_service_profit.getFyNum());
            accessoryGrossProfit.setAccName(RetailAccountReportEnum.valueAdd_service_profit.getAccName());
            accessoryGrossProfit.setPercent(BigDecimal.ZERO);
            for (RetailAccountDto detailCostOther : valueAddServiceCostList) {
                if (detailCostOther.getDeptNum().equals(deptNum)) {
                    flag2 = false;
                    accessoryGrossProfit.setYear(detailCostOther.getYear());
                    accessoryGrossProfit.setMonth(detailCostOther.getMonth());
                    accessoryGrossProfit.setDeptName(detailCostOther.getDeptName());
                    accessoryGrossProfit.setDeptNum(detailCostOther.getDeptNum());
                    amount = detailCostOther.getAmount();
                }
            }
            for (RetailAccountDto incomeTotal : valueAddServiceIncomeList) {
                if (incomeTotal.getDeptNum().equals(deptNum)){
                    flag = false;
                    if (flag2){
                        accessoryGrossProfit.setYear(incomeTotal.getYear());
                        accessoryGrossProfit.setMonth(incomeTotal.getMonth());
                        accessoryGrossProfit.setDeptName(incomeTotal.getDeptName());
                        accessoryGrossProfit.setDeptNum(incomeTotal.getDeptNum());
                    }
                    amount = incomeTotal.getAmount().subtract(amount);
                    break;
                }
            }
            if(flag){
                amount = BigDecimal.ZERO.subtract(amount);
            }
            accessoryGrossProfit.setAmount(amount);
            itemDataList.add(accessoryGrossProfit);
        }
        return itemDataList;
    }

    /**
     * 运营商毛利润-佣金=佣金收入-(运营商成本-佣金)
     * 占比=？
     */
    private List<RetailAccountDto> getOperatingCommissionGrossProfitList(List<RetailAccountDto> incomeList, List<RetailAccountDto> costList){
        List<RetailAccountDto> itemDataList = Lists.newArrayList();
        List<RetailAccountDto> commissionIncomeList = Lists.newArrayList();
        for(RetailAccountDto cost : incomeList){
            if(cost.getFyNum().equals(RetailAccountForIncomeEnum.commission_income.getFyNum())){
                commissionIncomeList.add(cost);
            }
        }
        List<String> deptNumList = commissionIncomeList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList());
        List<RetailAccountDto> commissionCostList = Lists.newArrayList();
        for(RetailAccountDto cost : costList){
            if(cost.getFyNum().equals(RetailAccountForCostEnum.commission_cost.getFyNum())){
                commissionCostList.add(cost);
            }
        }
        deptNumList.addAll(commissionCostList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList()));
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        for(String deptNum : deptNumSet){
            boolean flag = true;
            boolean flag2 = true;
            BigDecimal amount = BigDecimal.ZERO;
            RetailAccountDto accessoryGrossProfit = new RetailAccountDto();
            accessoryGrossProfit.setFyName(RetailAccountReportEnum.operating_commission_gross_profit.getFyName());
            accessoryGrossProfit.setFyNum(RetailAccountReportEnum.operating_commission_gross_profit.getFyNum());
            accessoryGrossProfit.setAccName(RetailAccountReportEnum.operating_commission_gross_profit.getAccName());
            accessoryGrossProfit.setPercent(BigDecimal.ZERO);
            for (RetailAccountDto detailCostOther : commissionCostList) {
                if (detailCostOther.getDeptNum().equals(deptNum)) {
                    flag2 = false;
                    accessoryGrossProfit.setYear(detailCostOther.getYear());
                    accessoryGrossProfit.setMonth(detailCostOther.getMonth());
                    accessoryGrossProfit.setDeptName(detailCostOther.getDeptName());
                    accessoryGrossProfit.setDeptNum(detailCostOther.getDeptNum());
                    amount = detailCostOther.getAmount();
                }
            }
            for (RetailAccountDto incomeTotal : commissionIncomeList) {
                if (incomeTotal.getDeptNum().equals(deptNum)){
                    flag = false;
                    if (flag2){
                        accessoryGrossProfit.setYear(incomeTotal.getYear());
                        accessoryGrossProfit.setMonth(incomeTotal.getMonth());
                        accessoryGrossProfit.setDeptName(incomeTotal.getDeptName());
                        accessoryGrossProfit.setDeptNum(incomeTotal.getDeptNum());
                    }
                    amount = incomeTotal.getAmount().subtract(amount);
                    break;
                }
            }
            if(flag){
                amount = BigDecimal.ZERO.subtract(amount);
            }
            accessoryGrossProfit.setAmount(amount);
            itemDataList.add(accessoryGrossProfit);
        }
        return itemDataList;
    }
    /**
     * 配件毛利润=配件收入-配件成本
     * 占比=？
     */
    private List<RetailAccountDto> getAccessoryGrossProfitList(List<RetailAccountDto> incomeList, List<RetailAccountDto> costList){
        List<RetailAccountDto> itemDataList = Lists.newArrayList();
        List<RetailAccountDto> accessoryIncomeList = Lists.newArrayList();
        for(RetailAccountDto cost : incomeList){
            if(cost.getFyNum().equals(RetailAccountForIncomeEnum.accessory_income.getFyNum())){
                accessoryIncomeList.add(cost);
            }
        }
        List<String> deptNumList = accessoryIncomeList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList());
        List<RetailAccountDto> accessoryCostList = Lists.newArrayList();
        for(RetailAccountDto cost : costList){
            if(cost.getFyNum().equals(RetailAccountForCostEnum.accessory_cost.getFyNum())){
                accessoryCostList.add(cost);
            }
        }
        deptNumList.addAll(accessoryCostList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList()));
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        for(String deptNum : deptNumSet){
            boolean flag = true;
            boolean flag2 = true;
            BigDecimal amount = BigDecimal.ZERO;
            RetailAccountDto accessoryGrossProfit = new RetailAccountDto();
            accessoryGrossProfit.setFyName(RetailAccountReportEnum.accessory_gross_profit.getFyName());
            accessoryGrossProfit.setFyNum(RetailAccountReportEnum.accessory_gross_profit.getFyNum());
            accessoryGrossProfit.setAccName(RetailAccountReportEnum.accessory_gross_profit.getAccName());
            accessoryGrossProfit.setPercent(BigDecimal.ZERO);
            for (RetailAccountDto detailCostOther : accessoryCostList) {
                if (detailCostOther.getDeptNum().equals(deptNum)) {
                    flag2 = false;
                    accessoryGrossProfit.setYear(detailCostOther.getYear());
                    accessoryGrossProfit.setMonth(detailCostOther.getMonth());
                    accessoryGrossProfit.setDeptName(detailCostOther.getDeptName());
                    accessoryGrossProfit.setDeptNum(detailCostOther.getDeptNum());
                    amount = detailCostOther.getAmount();
                }
            }
            for (RetailAccountDto incomeTotal : accessoryIncomeList) {
                if (incomeTotal.getDeptNum().equals(deptNum)){
                    flag = false;
                    if (flag2){
                        accessoryGrossProfit.setYear(incomeTotal.getYear());
                        accessoryGrossProfit.setMonth(incomeTotal.getMonth());
                        accessoryGrossProfit.setDeptName(incomeTotal.getDeptName());
                        accessoryGrossProfit.setDeptNum(incomeTotal.getDeptNum());
                    }
                    amount = incomeTotal.getAmount().subtract(amount);
                    break;
                }
            }
            if(flag){
                amount = BigDecimal.ZERO.subtract(amount);
            }
            accessoryGrossProfit.setAmount(amount);
            itemDataList.add(accessoryGrossProfit);
        }
        return itemDataList;
    }
    /**
     * 毛利润占比=毛利润/销售净收入
     * 占比=？
     */
    private List<RetailAccountDto> getAddPercentList(List<RetailAccountDto> grossProfitList, List<RetailAccountDto> netSalesRevenueList){
        List<RetailAccountDto> grossProfitPercentList = Lists.newArrayList();
        for(RetailAccountDto subjectUnitEntity : grossProfitList){
            boolean flag = true;
            for (RetailAccountDto netSalesRevenue : netSalesRevenueList) {
                if (netSalesRevenue.getDeptName().equals(subjectUnitEntity.getDeptName())) {
                    flag = false;
                    if (netSalesRevenue.getAmount().intValue() == 0) {
                        subjectUnitEntity.setPercent(BigDecimal.ZERO);
                    } else {
                        subjectUnitEntity.setPercent(subjectUnitEntity.getAmount().divide(netSalesRevenue.getAmount(), 4, BigDecimal.ROUND_HALF_UP));
                    }
                    grossProfitPercentList.add(subjectUnitEntity);
                    break;
                }
            }
            if (flag) {
                subjectUnitEntity.setPercent(BigDecimal.ZERO);
                grossProfitPercentList.add(subjectUnitEntity);
            }
        }
        return grossProfitPercentList;
    }
    /**
     * 手机毛利润=手机收入-手机成本-(调价+销售折让)
     * 占比=？
     */
    private List<RetailAccountDto> getMobileGrossProfitList(List<RetailAccountDto> incomeList, List<RetailAccountDto> costList, List<RetailAccountDto> specialManagementFeeList){
        List<RetailAccountDto> itemDataList = Lists.newArrayList();
        List<RetailAccountDto> mobileIncomeList = Lists.newArrayList();
        for(RetailAccountDto cost : incomeList){
            if(cost.getFyNum().equals(RetailAccountForIncomeEnum.mobile_income.getFyNum())){
                mobileIncomeList.add(cost);
            }
        }
        List<String> deptNumList = mobileIncomeList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList());
        List<RetailAccountDto> mobileCostList = Lists.newArrayList();
        for(RetailAccountDto cost : costList){
            if(cost.getFyNum().equals(RetailAccountForCostEnum.mobile_cost.getFyNum())){
                mobileCostList.add(cost);
            }
        }
        deptNumList.addAll(mobileCostList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList()));
        deptNumList.addAll(specialManagementFeeList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList()));
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        for(String deptNum : deptNumSet){
            boolean flag = true;
            boolean flag2 = true;
            BigDecimal amount = BigDecimal.ZERO;
            RetailAccountDto totalGrossProfitList = new RetailAccountDto();
            totalGrossProfitList.setFyName(RetailAccountReportEnum.mobile_gross_profit.getFyName());
            totalGrossProfitList.setFyNum(RetailAccountReportEnum.mobile_gross_profit.getFyNum());
            totalGrossProfitList.setAccName(RetailAccountReportEnum.mobile_gross_profit.getAccName());
            totalGrossProfitList.setPercent(BigDecimal.ZERO);
            List<RetailAccountDto> list = Lists.newArrayList(mobileCostList);
            list.addAll(specialManagementFeeList);
            for (RetailAccountDto detailCostOther : list) {
                if (detailCostOther.getDeptNum().equals(deptNum)) {
                    flag2 = false;
                    totalGrossProfitList.setYear(detailCostOther.getYear());
                    totalGrossProfitList.setMonth(detailCostOther.getMonth());
                    totalGrossProfitList.setDeptName(detailCostOther.getDeptName());
                    totalGrossProfitList.setDeptNum(detailCostOther.getDeptNum());
                    amount = amount.add(detailCostOther.getAmount());
                }
            }
            for (RetailAccountDto incomeTotal : mobileIncomeList) {
                if (incomeTotal.getDeptNum().equals(deptNum)){
                    flag = false;
                    if (flag2){
                        totalGrossProfitList.setYear(incomeTotal.getYear());
                        totalGrossProfitList.setMonth(incomeTotal.getMonth());
                        totalGrossProfitList.setDeptName(incomeTotal.getDeptName());
                        totalGrossProfitList.setDeptNum(incomeTotal.getDeptNum());
                    }
                    amount = incomeTotal.getAmount().subtract(amount);
                    break;
                }
            }
            if(flag){
                amount = BigDecimal.ZERO.subtract(amount);
            }
            totalGrossProfitList.setAmount(amount);
            itemDataList.add(totalGrossProfitList);
        }
        return itemDataList;
    }
    /**
     * 总毛利润=收入合计-成本合计-(调价+销售折让)
     * 占比=总毛利润/收入合计
     */
    private List<RetailAccountDto> getTotalGrossProfitList(List<RetailAccountDto> incomeTotalList, List<RetailAccountDto> costTotalList, List<RetailAccountDto> specialManagementFeeList){
        List<RetailAccountDto> itemDataList = Lists.newArrayList();
        List<String> deptNumList = incomeTotalList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList());
        deptNumList.addAll(costTotalList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList()));
        deptNumList.addAll(specialManagementFeeList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList()));
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        for(String deptNum : deptNumSet) {
            if (deptNum != null) {
                boolean flag = true;
                boolean flag2 = true;
                BigDecimal amount = BigDecimal.ZERO;
                BigDecimal percent = BigDecimal.ZERO;
                RetailAccountDto totalGrossProfitList = new RetailAccountDto();
                totalGrossProfitList.setFyName(RetailAccountReportEnum.total_gross_profit.getFyName());
                totalGrossProfitList.setFyNum(RetailAccountReportEnum.total_gross_profit.getFyNum());
                totalGrossProfitList.setAccName(RetailAccountReportEnum.total_gross_profit.getAccName());
                totalGrossProfitList.setPercent(BigDecimal.ZERO);
                List<RetailAccountDto> list = Lists.newArrayList(costTotalList);
                list.addAll(specialManagementFeeList);
                for (RetailAccountDto entity : list) {
                    if(entity.getDeptNum() != null) {
                        if (entity.getDeptNum().equals(deptNum)) {
                            flag2 = false;
                            totalGrossProfitList.setYear(entity.getYear());
                            totalGrossProfitList.setMonth(entity.getMonth());
                            totalGrossProfitList.setDeptName(entity.getDeptName());
                            totalGrossProfitList.setDeptNum(entity.getDeptNum());
                            amount = amount.add(entity.getAmount());
                        }
                    }else {
                        System.out.println(entity.getDeptName()+entity.getFyName()+entity.getAccName());
                    }
                }
                for (RetailAccountDto incomeTotal : incomeTotalList) {
                    if(incomeTotal.getDeptNum() != null) {
                        if (incomeTotal.getDeptNum().equals(deptNum)) {
                            flag = false;
                            if (flag2) {
                                totalGrossProfitList.setYear(incomeTotal.getYear());
                                totalGrossProfitList.setMonth(incomeTotal.getMonth());
                                totalGrossProfitList.setDeptName(incomeTotal.getDeptName());
                                totalGrossProfitList.setDeptNum(incomeTotal.getDeptNum());
                            }
                            amount = incomeTotal.getAmount().subtract(amount);
                            if (!incomeTotal.getAmount().equals(new BigDecimal("0.0000000000"))){
                                percent = amount.divide(incomeTotal.getAmount(), 4, BigDecimal.ROUND_HALF_UP);
                            }else {
                                percent = BigDecimal.ZERO;
                            }

                            break;
                        }
                    }else{
                        System.out.println(incomeTotal.getDeptName()+incomeTotal.getFyName()+incomeTotal.getAccName());
                    }
                }
                if (flag) {
                    amount = BigDecimal.ZERO.subtract(amount);
                }
                totalGrossProfitList.setAmount(amount);
                totalGrossProfitList.setPercent(percent);
                itemDataList.add(totalGrossProfitList);
            }
        }
        return itemDataList;
    }
    /**
     * 销售净收入=手机收入（主营业务收入中的手机001）-(调价+销售折让)
     * 占比=0
     */
    private List<RetailAccountDto> getNetSalesRevenueList(List<RetailAccountDto> incomeList, List<RetailAccountDto> specialManagementFeeList){
        List<RetailAccountDto> itemDataList = Lists.newArrayList();
        List<RetailAccountDto> mobileIncomeList = Lists.newArrayList();
        for(RetailAccountDto cost : incomeList){
            if(cost.getFyNum().equals(RetailAccountForIncomeEnum.mobile_income.getFyNum())){
                mobileIncomeList.add(cost);
            }
        }
        List<String> deptNumList = mobileIncomeList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList());
        deptNumList.addAll(specialManagementFeeList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList()));
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        for(String deptNum : deptNumSet){
            boolean flag = true;
            boolean flag2 = true;
            BigDecimal amount = BigDecimal.ZERO;
            RetailAccountDto netSalesRevenue = new RetailAccountDto();
            netSalesRevenue.setFyName(RetailAccountReportEnum.net_sales_revenue.getFyName());
            netSalesRevenue.setFyNum(RetailAccountReportEnum.net_sales_revenue.getFyNum());
            netSalesRevenue.setAccName(RetailAccountReportEnum.net_sales_revenue.getAccName());
            netSalesRevenue.setPercent(BigDecimal.ZERO);
            for (RetailAccountDto detailCostOther : specialManagementFeeList) {
                if (detailCostOther.getDeptNum().equals(deptNum)) {
                    flag2 = false;
                    netSalesRevenue.setYear(detailCostOther.getYear());
                    netSalesRevenue.setMonth(detailCostOther.getMonth());
                    netSalesRevenue.setDeptName(detailCostOther.getDeptName());
                    netSalesRevenue.setDeptNum(detailCostOther.getDeptNum());
                    amount = amount.add(detailCostOther.getAmount());
                }
            }
            for (RetailAccountDto mobileIncome : mobileIncomeList) {
                if (mobileIncome.getDeptNum().equals(deptNum)){
                    flag = false;
                    if (flag2){
                        netSalesRevenue.setYear(mobileIncome.getYear());
                        netSalesRevenue.setMonth(mobileIncome.getMonth());
                        netSalesRevenue.setDeptName(mobileIncome.getDeptName());
                        netSalesRevenue.setDeptNum(mobileIncome.getDeptNum());
                    }
                    amount = mobileIncome.getAmount().subtract(amount);
                    break;
                }
            }
            if(flag){
                amount = BigDecimal.ZERO.subtract(amount);
            }
            netSalesRevenue.setAmount(amount);
            itemDataList.add(netSalesRevenue);
        }
        return itemDataList;
    }
    /**
     * 管理费用--调价,管理费用--销售折让
     * 占比=0
     */
    private List<RetailAccountDto> getSpecialManagementFeeList(List<RetailAccountDto> managementFeeList){
        List<RetailAccountDto> specialManagementFeeList = Lists.newArrayList();
        for(RetailAccountDto managementFee: managementFeeList){
            if(managementFee.getFyNum().equals(RetailAccountReportEnum.price_adjustment.getFyNum()) || managementFee.getFyNum().equals(RetailAccountReportEnum.sales_allowance.getFyNum())){
                specialManagementFeeList.add(managementFee);
            }
        }
        return specialManagementFeeList;
    }
    private List<RetailAccountDto> getCostTotalList(List<RetailAccountDto> costList){
        List<String> deptNumList = costList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList());
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        List<RetailAccountDto> costTotalList = Lists.newArrayList();
        for(String deptNum : deptNumSet){
            RetailAccountDto subjectUnitEntity = new RetailAccountDto();
            subjectUnitEntity.setFyName(RetailAccountReportEnum.cost_total.getFyName());
            subjectUnitEntity.setFyNum(RetailAccountReportEnum.cost_total.getFyNum());
            subjectUnitEntity.setAccName(RetailAccountReportEnum.cost_total.getAccName());
            BigDecimal amount = BigDecimal.ZERO;
            for(RetailAccountDto cost : costList){
                if (cost.getDeptNum().equals(deptNum)){
                    subjectUnitEntity.setDeptNum(cost.getDeptNum());
                    subjectUnitEntity.setDeptName(cost.getDeptName());
                    subjectUnitEntity.setYear(cost.getYear());
                    subjectUnitEntity.setMonth(cost.getMonth());
                    amount = amount.add(cost.getAmount());
                }
            }
            subjectUnitEntity.setAmount(amount);
            costTotalList.add(subjectUnitEntity);
        }
        return costTotalList;
    }
    private List<RetailAccountDto> getIncomeTotalList(List<RetailAccountDto> incomeList){
        List<String> deptNumList = incomeList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList());
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        List<RetailAccountDto> incomeTotalList = Lists.newArrayList();
        for(String deptNum : deptNumSet){
            RetailAccountDto subjectUnitEntity = new RetailAccountDto();
            subjectUnitEntity.setFyName(RetailAccountReportEnum.income_total.getFyName());
            subjectUnitEntity.setFyNum(RetailAccountReportEnum.income_total.getFyNum());
            subjectUnitEntity.setAccName(RetailAccountReportEnum.income_total.getAccName());
            BigDecimal amount = BigDecimal.ZERO;
            for(RetailAccountDto income : incomeList){
                if (income.getDeptNum().equals(deptNum)){
                    subjectUnitEntity.setYear(income.getYear());
                    subjectUnitEntity.setMonth(income.getMonth());
                    subjectUnitEntity.setDeptNum(income.getDeptNum());
                    subjectUnitEntity.setDeptName(income.getDeptName());
                    amount = amount.add(income.getAmount());
                }
            }
            subjectUnitEntity.setAmount(amount);
            incomeTotalList.add(subjectUnitEntity);
        }
        return incomeTotalList;
    }

    /**
     * 成本list（其中分期服务费=其他业务支出的分期服务费+管理费用的分期服务费
     * @return
     */
    private void getNewCostList(List<RetailAccountDto> costList,List<RetailAccountDto> managementFeeList){
        List<String> deptNumList = managementFeeList.stream().map(RetailAccountDto::getDeptNum).collect(Collectors.toList());
        Map<String,RetailAccountDto> costMap = Maps.newHashMap();
        for(RetailAccountDto cost : costList){
            costMap.put(cost.getFyNum(),cost);
        }
        for(String deptNum: deptNumList) {
            for (RetailAccountDto managementFee : managementFeeList) {
                //管理费用的分期服务费
                if (deptNum.equals(managementFee.getDeptNum()) && managementFee.getAccName().equals("管理费用") && managementFee.getFyNum().equals(RetailAccountForCostEnum.valueAdd_service_cost.getFyNum())) {
                    if(!costMap.containsKey("004")){
                        managementFee.setAccName(RetailAccountForCostEnum.valueAdd_service_cost.getAccName());
                        costList.add(managementFee);
                    }else{
                        for (RetailAccountDto cost : costList) {
                            if (cost.getFyNum().equals(RetailAccountForCostEnum.valueAdd_service_cost.getFyNum()) && deptNum.equals(managementFee.getDeptNum()) ) {
                                cost.setAmount(cost.getAmount().add(managementFee.getAmount()));
                            }
                        }
                    }
                }
            }
        }
    }

    //显示的科目
    private Map<String,TreeDto> showEnum(){
        Map<String,TreeDto> showItemMap = Maps.newLinkedHashMap();
        RetailAccountReportEnum[] enumList = RetailAccountReportEnum.values();
        for(int i=0; i< enumList.length; i++){
            if(enumList[i].equals(RetailAccountReportEnum.income_total)){
                for(RetailAccountForIncomeEnum incomeEnum : RetailAccountForIncomeEnum.values()){
                    TreeDto tree = new TreeDto();
                    tree.setParent(incomeEnum.getAccName());
                    tree.setChild(incomeEnum.getFyNum());
                    showItemMap.put(incomeEnum.getFyName(),tree);
                }
                TreeDto tree = new TreeDto();
                tree.setParent(enumList[i].getAccName());
                tree.setChild(enumList[i].getFyNum());
                showItemMap.put(enumList[i].getFyName(),tree);
            }else if(enumList[i].equals(RetailAccountReportEnum.cost_total)){
                for(RetailAccountForCostEnum costEnum : RetailAccountForCostEnum.values()){
                    TreeDto tree = new TreeDto();
                    tree.setParent(costEnum.getAccName());
                    tree.setChild(costEnum.getFyNum());
                    showItemMap.put(costEnum.getFyName(),tree);
                }
                TreeDto tree = new TreeDto();
                tree.setParent(enumList[i].getAccName());
                tree.setChild(enumList[i].getFyNum());
                showItemMap.put(enumList[i].getFyName(),tree);
            }else if(enumList[i].equals(RetailAccountReportEnum.daily_operating_expenses_total)) {
                for (RetailAccountForManagerExpenses1Enum fee1 : RetailAccountForManagerExpenses1Enum.values()) {
                    TreeDto tree = new TreeDto();
                    tree.setParent(fee1.getAccName());
                    tree.setChild(fee1.getFyNum());
                    showItemMap.put(fee1.getFyName(), tree);
                }
                TreeDto tree = new TreeDto();
                tree.setParent(enumList[i].getAccName());
                tree.setChild(enumList[i].getFyNum());
                showItemMap.put(enumList[i].getFyName(), tree);
            }else if (enumList[i].equals(RetailAccountReportEnum.net_profit_1)){
                TreeDto tree = new TreeDto();
                tree.setParent(enumList[i].getAccName());
                tree.setChild(enumList[i].getFyNum());
                showItemMap.put(enumList[i].getFyName(), tree);
                for (RetailAccountForManagerExpenses2Enum fee2 : RetailAccountForManagerExpenses2Enum.values()){
                    TreeDto tree1 = new TreeDto();
                    tree1.setParent(fee2.getAccName());
                    tree1.setChild(fee2.getFyNum());
                    showItemMap.put(fee2.getFyName(), tree1);
                }
            }else{
                TreeDto tree = new TreeDto();
                tree.setParent(enumList[i].getAccName());
                tree.setChild(enumList[i].getFyNum());
                showItemMap.put(enumList[i].getFyName(),tree);
            }
        }
        return showItemMap;
    }
}
