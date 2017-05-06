package net.myspring.cloud.modules.report.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.constant.K3CloudConstant;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.CharEnum;
import net.myspring.cloud.common.enums.RetailReportEnum;
import net.myspring.cloud.common.enums.RetailReportForCostEnum;
import net.myspring.cloud.common.enums.RetailReportForIncomeEnum;
import net.myspring.cloud.modules.report.domain.Retail;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import net.myspring.cloud.modules.report.mapper.GlcxViewMapper;
import net.myspring.cloud.modules.report.mapper.RetailReportMapper;
import net.myspring.cloud.modules.sys.domain.DynamicSubject;
import net.myspring.cloud.modules.sys.service.DynamicSubjectForRetailReportService;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@KingdeeDataSource
public class RetailReportService {
    @Autowired
    private GlcxViewMapper glcxViewMapper;
    @Autowired
    private RetailReportMapper retailReportMapper;
    @Autowired
    private RetailReportForAssistService retailReportForAssistService;
    @Autowired
    private DynamicSubjectForRetailReportService dynamicSubjectForRetailReportService;

    //显示的科目
    private Map<String,NameNumberDto> showEnum(){
        List<DynamicSubject> newSubjectList = glcxViewMapper.findByAccName("管理费用");
        List<DynamicSubject> dynamicSubjectList = dynamicSubjectForRetailReportService.findCloudDynamicSubjectList(newSubjectList);
        Map<String,NameNumberDto> showItemMap = Maps.newLinkedHashMap();
        RetailReportEnum[] enumList = RetailReportEnum.values();
        for(int i=0; i< enumList.length; i++){
            if(enumList[i].equals(RetailReportEnum.income_total)){
                for(RetailReportForIncomeEnum incomeEnum : RetailReportForIncomeEnum.values()){
                    NameNumberDto tree = new NameNumberDto();
                    tree.setName(incomeEnum.getAccName());
                    tree.setNumber(incomeEnum.getFyNum());
                    showItemMap.put(incomeEnum.getFyName(),tree);
                }
                NameNumberDto tree = new NameNumberDto();
                tree.setName(enumList[i].getAccName());
                tree.setNumber(enumList[i].getFyNum());
                showItemMap.put(enumList[i].getFyName(),tree);
            }else if(enumList[i].equals(RetailReportEnum.cost_total)){
                for(RetailReportForCostEnum costEnum : RetailReportForCostEnum.values()){
                    NameNumberDto tree = new NameNumberDto();
                    tree.setName(costEnum.getAccName());
                    tree.setNumber(costEnum.getFyNum());
                    showItemMap.put(costEnum.getFyName(),tree);
                }
                NameNumberDto tree = new NameNumberDto();
                tree.setName(enumList[i].getAccName());
                tree.setNumber(enumList[i].getFyNum());
                showItemMap.put(enumList[i].getFyName(),tree);
            }else if(enumList[i].equals(RetailReportEnum.daily_operating_expenses_total)){
                for(DynamicSubject fee: dynamicSubjectList){
                    NameNumberDto tree = new NameNumberDto();
                    tree.setName(fee.getAccName());
                    tree.setNumber(fee.getFyNum());
                    showItemMap.put(fee.getFyName(),tree);
                }
                NameNumberDto tree = new NameNumberDto();
                tree.setName(enumList[i].getAccName());
                tree.setNumber(enumList[i].getFyNum());
                showItemMap.put(enumList[i].getFyName(),tree);
            }else{
                NameNumberDto tree = new NameNumberDto();
                tree.setName(enumList[i].getAccName());
                tree.setNumber(enumList[i].getFyNum());
                showItemMap.put(enumList[i].getFyName(),tree);
            }
        }
        return showItemMap;
    }

    //页面导出纵向显示数据
    public List<Retail> getRetailReport2(YearMonth start, YearMonth end){
        List<Retail> retailOrWholeSaleReports = Lists.newArrayList();
        retailOrWholeSaleReports.addAll(findAmountAndPercentForAddDepartment(start, end));
        retailOrWholeSaleReports.addAll(findSumAmountAndSumPercentForAddDepartment( start,  end));
        retailOrWholeSaleReports.addAll(findAmountAndPercentForDepartments(start, end));
        retailOrWholeSaleReports.addAll(findSumAmountAndSumPercentForDepartments(start, end));
        return retailOrWholeSaleReports;
    }

    //页面显示数据
    public List<List<Object>> getRetailReport(YearMonth start, YearMonth end) {
        List<List<Object>> retailReportModels = Lists.newArrayList();
        List<NameNumberDto> departmentList = Lists.newArrayList();
        departmentList.add(retailReportForAssistService.getAddDepartment());
        departmentList.addAll(glcxViewMapper.findDepartment());
        List<Retail> itemDataList = findAmountAndPercentForAddDepartment(start, end);
        itemDataList.addAll(findAmountAndPercentForDepartments(start, end));
        Map<String, Retail> retailReportItemMap = Maps.newHashMap();
        for (Retail tempItemData : itemDataList) {
            String key = tempItemData.getAccName() + CharEnum.UNDER_LINE.getValue() + tempItemData.getFyNum() + CharEnum.UNDER_LINE.getValue() + tempItemData.getDeptName() + CharEnum.UNDER_LINE.getValue() + tempItemData.getYear() + CharEnum.UNDER_LINE.getValue() + tempItemData.getMonth();
            if (!retailReportItemMap.containsKey(key)) {
                retailReportItemMap.put(key, tempItemData);
            }
        }
        //获取累计金额，占比
        List<Retail> sumItemDataList = findSumAmountAndSumPercentForAddDepartment(start, end);
        sumItemDataList.addAll(findSumAmountAndSumPercentForDepartments(start, end));
        Map<String, Retail> sumRetailReportItemMap = Maps.newHashMap();
        for (Retail sumDirectShopDetail : sumItemDataList) {
            String key = sumDirectShopDetail.getAccName() + CharEnum.UNDER_LINE.getValue() + sumDirectShopDetail.getFyNum() + CharEnum.UNDER_LINE.getValue() +sumDirectShopDetail.getDeptName();
            if (!sumRetailReportItemMap.containsKey(key)) {
                sumRetailReportItemMap.put(key, sumDirectShopDetail);
            }
        }
        //数据
        for (Map.Entry<String,NameNumberDto> showEnum : showEnum().entrySet()) {
            List<Object> item = Lists.newArrayList();
            item.add(showEnum.getKey());
            for (NameNumberDto department : departmentList) {
                YearMonth tempStart = start;
                while (tempStart.isBefore(end) || tempStart.equals(end)) {
                    int year = tempStart.getYear();
                    int month = tempStart.getMonthValue();
                    String key = showEnum.getValue().getName() + CharEnum.UNDER_LINE.getValue() +showEnum.getValue().getNumber()+ CharEnum.UNDER_LINE.getValue() + department.getName() + CharEnum.UNDER_LINE.getValue() + year + CharEnum.UNDER_LINE.getValue() + month;
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
                String key2 = showEnum.getValue().getName() + CharEnum.UNDER_LINE.getValue() + showEnum.getValue().getNumber() + CharEnum.UNDER_LINE.getValue()+department.getName();
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
    private List<Retail> findAmountAndPercentForAddDepartment(YearMonth dateStart, YearMonth dateEnd) {
        List<Retail> itemDataList = Lists.newArrayList();
        while (dateStart.isBefore(dateEnd) || dateStart.equals(dateEnd)) {
            int year = dateStart.getYear();
            int month = dateStart.getMonthValue();
            List<Retail> incomeList = Lists.newArrayList();
            for(RetailReportForIncomeEnum retailReportForIncomeEnum: RetailReportForIncomeEnum.values()){
                incomeList.addAll(glcxViewMapper.findEntityByPeriodForTotalDepartment(year,month,retailReportForIncomeEnum.getAccName(),retailReportForIncomeEnum.getFyNum()));
            }
            List<Retail> incomeForAddDepartmentList = Lists.newArrayList();
            for(Retail income : incomeList){
                income.setDeptName(K3CloudConstant.TOTAL_DEPARTMENT);
                income.setDeptNum(K3CloudConstant.DOUBLE_ZERO);
                incomeForAddDepartmentList.add(income);
            }
            List<Retail> costList = Lists.newArrayList();
            for(RetailReportForCostEnum retailReportForCostEnum: RetailReportForCostEnum.values()){
                costList.addAll(glcxViewMapper.findEntityByPeriodForTotalDepartment(year,month,retailReportForCostEnum.getAccName(),retailReportForCostEnum.getFyNum()));
            }
            List<Retail> costForAddDepartmentList = Lists.newArrayList();
            for(Retail cost : costList){
                cost.setDeptName(K3CloudConstant.TOTAL_DEPARTMENT);
                cost.setDeptNum(K3CloudConstant.DOUBLE_ZERO);
                costForAddDepartmentList.add(cost);
            }
            List<Retail> managementFeeList = glcxViewMapper.findEntityByPeriodForTotalDepartment(year,month,"管理费用",null);
            List<Retail> managementFeeForAddDepartmentList = Lists.newArrayList();
            for(Retail managementFee : managementFeeList){
                managementFee.setDeptName(K3CloudConstant.TOTAL_DEPARTMENT);
                managementFee.setDeptNum(K3CloudConstant.DOUBLE_ZERO);
                managementFeeForAddDepartmentList.add(managementFee);
            }
            List<Retail> xsckdQuantityList =  retailReportMapper.findXSCKDByPeriodForTotalDepartment(year, month);
            List<Retail> xsckdQuantityForAddDepartmentList = Lists.newArrayList();
            for(Retail xsckdQuantity : xsckdQuantityList){
                xsckdQuantity.setDeptName(K3CloudConstant.TOTAL_DEPARTMENT);
                xsckdQuantity.setDeptNum(K3CloudConstant.DOUBLE_ZERO);
                xsckdQuantityForAddDepartmentList.add(xsckdQuantity);
            }
            List<Retail> xsthdQuantityList =  retailReportMapper.findXSTHDByPeriodForTotalDepartment(year, month);
            List<Retail> xsthdQuantityForAddDepartmentList = Lists.newArrayList();
            for(Retail xsthdQuantity : xsthdQuantityList){
                xsthdQuantity.setDeptName(K3CloudConstant.TOTAL_DEPARTMENT);
                xsthdQuantity.setDeptNum(K3CloudConstant.DOUBLE_ZERO);
                xsthdQuantityForAddDepartmentList.add(xsthdQuantity);
            }
            itemDataList.addAll(getSalesProductQuantity(xsckdQuantityForAddDepartmentList,xsthdQuantityForAddDepartmentList));
            itemDataList.addAll(getSubjectFeeEntityItem(incomeForAddDepartmentList,costForAddDepartmentList,managementFeeForAddDepartmentList));
            dateStart = dateStart.plusMonths(1);
        }
        return itemDataList;
    }

    //"合计" 累计金额+累计占比
    private List<Retail> findSumAmountAndSumPercentForAddDepartment(YearMonth start, YearMonth end) {
        Integer startDate = start.getYear() * 100 + start.getMonthValue();
        Integer endDate = end.getYear() * 100 + end.getMonthValue();
        List<Retail> incomeList = Lists.newArrayList();
        for(RetailReportForIncomeEnum retailReportForIncomeEnum: RetailReportForIncomeEnum.values()){
            incomeList.addAll(glcxViewMapper.findEntityBySumPeriodForTotalDepartment(startDate,endDate,retailReportForIncomeEnum.getAccName(),retailReportForIncomeEnum.getFyNum()));
        }
        List<Retail> incomeForAddDepartmentList = Lists.newArrayList();
        for(Retail income : incomeList){
            income.setDeptName(K3CloudConstant.TOTAL_DEPARTMENT);
            income.setDeptNum(K3CloudConstant.DOUBLE_ZERO);
            incomeForAddDepartmentList.add(income);
        }
        List<Retail> costList = Lists.newArrayList();
        for(RetailReportForCostEnum retailReportForCostEnum: RetailReportForCostEnum.values()){
            costList.addAll(glcxViewMapper.findEntityBySumPeriodForTotalDepartment(startDate,endDate,retailReportForCostEnum.getAccName(),retailReportForCostEnum.getFyNum()));
        }
        List<Retail> costForAddDepartmentList = Lists.newArrayList();
        for(Retail cost : costList){
            cost.setDeptName(K3CloudConstant.TOTAL_DEPARTMENT);
            cost.setDeptNum(K3CloudConstant.DOUBLE_ZERO);
            costForAddDepartmentList.add(cost);
        }
        List<Retail> managementFeeList = glcxViewMapper.findEntityBySumPeriodForTotalDepartment(startDate,endDate,"管理费用",null);
        List<Retail> managementFeeForAddDepartmentList = Lists.newArrayList();
        for(Retail managementFee : managementFeeList){
            managementFee.setDeptName(K3CloudConstant.TOTAL_DEPARTMENT);
            managementFee.setDeptNum(K3CloudConstant.DOUBLE_ZERO);
            managementFeeForAddDepartmentList.add(managementFee);
        }
        List<Retail> xsckdQuantityList =  retailReportMapper.findXSCKDBySumPeriodForTotalDepartment(startDate, endDate);
        List<Retail> xsckdQuantityForAddDepartmentList = Lists.newArrayList();
        for(Retail xsckdQuantity : xsckdQuantityList){
            xsckdQuantity.setDeptName(K3CloudConstant.TOTAL_DEPARTMENT);
            xsckdQuantity.setDeptNum(K3CloudConstant.DOUBLE_ZERO);
            xsckdQuantityForAddDepartmentList.add(xsckdQuantity);
        }
        List<Retail> xsthdQuantityList =  retailReportMapper.findXSTHDBySumPeriodForTotalDepartment(startDate, endDate);
        List<Retail> xsthdQuantityForAddDepartmentList = Lists.newArrayList();
        for(Retail xsthdQuantity : xsthdQuantityList){
            xsthdQuantity.setDeptName(K3CloudConstant.TOTAL_DEPARTMENT);
            xsthdQuantity.setDeptNum(K3CloudConstant.DOUBLE_ZERO);
            xsthdQuantityForAddDepartmentList.add(xsthdQuantity);
        }
        List<Retail> list = Lists.newArrayList(getSalesProductQuantity(xsckdQuantityForAddDepartmentList,xsthdQuantityForAddDepartmentList));
        list.addAll(getSubjectFeeEntityItem(incomeForAddDepartmentList,costForAddDepartmentList,managementFeeForAddDepartmentList));
        return list;
    }

    //各个部门 金额+占比
    private List<Retail> findAmountAndPercentForDepartments(YearMonth dateStart, YearMonth dateEnd) {
        List<Retail> itemDataList = Lists.newArrayList();
        while (dateStart.isBefore(dateEnd) || dateStart.equals(dateEnd)) {
            int year = dateStart.getYear();
            int month = dateStart.getMonthValue();
            List<Retail> incomeList = Lists.newArrayList();
            for(RetailReportForIncomeEnum retailReportForIncomeEnum: RetailReportForIncomeEnum.values()){
                incomeList.addAll(glcxViewMapper.findEntityByPeriod(year,month,retailReportForIncomeEnum.getAccName(),retailReportForIncomeEnum.getFyNum()));
            }
            List<Retail> costList = Lists.newArrayList();
            for(RetailReportForCostEnum retailReportForCostEnum: RetailReportForCostEnum.values()){
                costList.addAll(glcxViewMapper.findEntityByPeriod(year,month,retailReportForCostEnum.getAccName(),retailReportForCostEnum.getFyNum()));
            }
            List<Retail> managementFeeList = glcxViewMapper.findEntityByPeriod(year,month,"管理费用",null);
            List<Retail> xsckdQuantity =  retailReportMapper.findXSCKDByPeriod(year, month);
            List<Retail> xsthdQuantity =  retailReportMapper.findXSTHDByPeriod(year, month);
            itemDataList.addAll(getSalesProductQuantity(xsckdQuantity,xsthdQuantity));
            itemDataList.addAll(getSubjectFeeEntityItem(incomeList,costList,managementFeeList));
            dateStart = dateStart.plusMonths(1);
        }
        return itemDataList;
    }

    //各个部门 累计金额+累计占比
    private List<Retail> findSumAmountAndSumPercentForDepartments(YearMonth start, YearMonth end) {
        Integer startDate = start.getYear() * 100 + start.getMonthValue();
        Integer endDate = end.getYear() * 100 + end.getMonthValue();
        List<Retail> incomeList = Lists.newArrayList();
        for(RetailReportForIncomeEnum retailReportForIncomeEnum: RetailReportForIncomeEnum.values()){
            incomeList.addAll(glcxViewMapper.findEntityBySumPeriod(startDate,endDate,retailReportForIncomeEnum.getAccName(),retailReportForIncomeEnum.getFyNum()));
        }
        List<Retail> costList = Lists.newArrayList();
        for(RetailReportForCostEnum retailReportForCostEnum: RetailReportForCostEnum.values()){
            costList.addAll(glcxViewMapper.findEntityBySumPeriod(startDate,endDate,retailReportForCostEnum.getAccName(),retailReportForCostEnum.getFyNum()));
        }
        List<Retail> managementFeeList = glcxViewMapper.findEntityBySumPeriod(startDate,endDate,"管理费用",null);
        List<Retail> xsckdQuantity =  retailReportMapper.findXSCKDBySumPeriod(startDate, endDate);
        List<Retail> xsthdQuantity =  retailReportMapper.findXSTHDBySumPeriod(startDate, endDate);
        List<Retail> list = Lists.newArrayList(getSalesProductQuantity(xsckdQuantity,xsthdQuantity));
        list.addAll(getSubjectFeeEntityItem(incomeList, costList,managementFeeList));
        return list;
    }

    private List<Retail> getSubjectFeeEntityItem(List<Retail> incomeList, List<Retail> costList, List<Retail> managementFeeList) {
        List<Retail> itemDataList = Lists.newArrayList();
        itemDataList.addAll(incomeList);
        List<Retail> incomeTotalList = getIncomeTotalList(incomeList);
        itemDataList.addAll(incomeTotalList);
        //对分期服务费处理
        getNewCostList(costList,managementFeeList);
        itemDataList.addAll(costList);
        List<Retail> costTotalList = getCostTotalList(costList);
        itemDataList.addAll(costTotalList);
        List<Retail> specialManagementFeeList = getSpecialManagementFeeList(managementFeeList);
        itemDataList.addAll(specialManagementFeeList);
        List<Retail> netSalesRevenueList = getNetSalesRevenueList(incomeList,specialManagementFeeList);
        itemDataList.addAll(netSalesRevenueList);
        List<Retail> totalGrossProfitList = getTotalGrossProfitList(incomeTotalList,costTotalList,specialManagementFeeList);
        itemDataList.addAll(totalGrossProfitList);
        List<Retail> mobileGrossProfitList = getMobileGrossProfitList(incomeList,costList,specialManagementFeeList);
        itemDataList.addAll(getAddPercentList(mobileGrossProfitList,netSalesRevenueList));//netSalesRevenueList
        List<Retail> accessoryGrossProfitList = getAccessoryGrossProfitList(incomeList,costList);
        itemDataList.addAll(getAddPercentList(accessoryGrossProfitList,netSalesRevenueList));//netSalesRevenueList
        List<Retail> operatingCommissionGrossProfitList = getOperatingCommissionGrossProfitList(incomeList,costList);
        itemDataList.addAll(getAddPercentList(operatingCommissionGrossProfitList,netSalesRevenueList));//netSalesRevenueList
        List<Retail> valueAddServiceProfitProfitList = getValueAddServiceProfitProfitList(incomeList,costList);
        itemDataList.addAll(getAddPercentList(valueAddServiceProfitProfitList,netSalesRevenueList));//netSalesRevenueList
        List<Retail> generalManagementFeeList = getGeneralManagementFeeList(managementFeeList,netSalesRevenueList);
        itemDataList.addAll(generalManagementFeeList);
        List<Retail> totalDailyOperatingExpensesList = getTotalDailyOperatingExpensesList(generalManagementFeeList);
        itemDataList.addAll(getAddPercentList(totalDailyOperatingExpensesList,netSalesRevenueList));
        List<Retail> adManagementFeeList = getADManagementFeeList(managementFeeList,netSalesRevenueList);
        itemDataList.addAll(adManagementFeeList);
        List<Retail> operationCostSummaryList = getOperationCostSummary(adManagementFeeList,totalDailyOperatingExpensesList);
        itemDataList.addAll(getAddPercentList(operationCostSummaryList,netSalesRevenueList));
        List<Retail> netProfitList = getNetProfit(totalGrossProfitList,operationCostSummaryList);
        itemDataList.addAll(getAddPercentList(netProfitList,netSalesRevenueList));
        return itemDataList;
    }
    /**
     * 销售数量
     */
    private List<Retail> getSalesProductQuantity(List<Retail> xsckdQuantity, List<Retail> xsthdQuantity ){
        List<Retail> itemDataList = Lists.newArrayList();
        List<String> deptNumList = CollectionUtil.extractToList(xsckdQuantity,"deptNum");
        deptNumList.addAll(CollectionUtil.extractToList(xsthdQuantity,"deptNum"));
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        for(String  deptNum : deptNumSet) {
            boolean flag = true;
            boolean flag2 = true;
            BigDecimal qty = BigDecimal.ZERO;
            Retail tempItemData = new Retail();
            tempItemData.setFyName(RetailReportEnum.sales_mobile_qty.getFyName());
            tempItemData.setFyNum(RetailReportEnum.sales_mobile_qty.getFyNum());
            tempItemData.setAccName(RetailReportEnum.sales_mobile_qty.getAccName());
            for (Retail xsthd : xsthdQuantity) {
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
            for (Retail xsckd : xsckdQuantity) {
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
     * 净利润=总毛利润-运营费用汇总
     * 占比: 之差
     */
    private List<Retail> getNetProfit(List<Retail> totalGrossProfitList, List<Retail> operationCostSummaryList) {
        List<Retail> itemDataList = Lists.newArrayList();
        List<String> deptNumList = CollectionUtil.extractToList(totalGrossProfitList,"deptNum");
        deptNumList.addAll(CollectionUtil.extractToList(operationCostSummaryList,"deptNum"));
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        for(String  deptNum : deptNumSet) {
            boolean flag = true;
            boolean flag2 = true;
            BigDecimal amount = BigDecimal.ZERO;
            Retail tempItemData = new Retail();
            tempItemData.setFyName(RetailReportEnum.net_profit.getFyName());
            tempItemData.setFyNum(RetailReportEnum.net_profit.getFyNum());
            tempItemData.setAccName(RetailReportEnum.net_profit.getAccName());
            for (Retail operationCostSummary : operationCostSummaryList) {
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
            for (Retail totalGrossProfit : totalGrossProfitList) {
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
     * 运营费用汇总=日常运营费用合计+广告费
     * 占比: 之和
     */
    private List<Retail> getOperationCostSummary(List<Retail> adManagementFeeList, List<Retail> totalDailyOperatingExpensesList) {
        List<Retail> itemDataList = Lists.newArrayList();
        Map<String, Retail> detailCostsADMap = CollectionUtil.extractToMap(adManagementFeeList, "deptNum");
        for (Retail totalDailyOperatingExpenses : totalDailyOperatingExpensesList) {
            BigDecimal amount = BigDecimal.ZERO;
            Retail tempItemData = new Retail();
            tempItemData.setYear(totalDailyOperatingExpenses.getYear());
            tempItemData.setMonth(totalDailyOperatingExpenses.getMonth());
            tempItemData.setFyName(RetailReportEnum.operation_cost_summary.getFyName());
            tempItemData.setFyNum(RetailReportEnum.operation_cost_summary.getFyNum());
            tempItemData.setAccName(RetailReportEnum.operation_cost_summary.getAccName());
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
    private List<Retail> getADManagementFeeList(List<Retail> managementFeeList, List<Retail> netSalesRevenueList){
        List<Retail> adManagementFeeList = Lists.newArrayList();
        for(Retail retail : managementFeeList){
            if(retail.getFyNum().equals(RetailReportEnum.advertising_fee.getFyNum())){
                boolean flag = true;
                for (Retail netSalesRevenue : netSalesRevenueList) {
                    if (netSalesRevenue.getDeptName().equals(retail.getDeptName())) {
                        flag = false;
                        if (netSalesRevenue.getAmount().intValue() == 0) {
                            retail.setPercent(BigDecimal.ZERO);
                        } else {
                            retail.setPercent(retail.getAmount().divide(netSalesRevenue.getAmount(), 4, BigDecimal.ROUND_HALF_UP));
                        }
                        adManagementFeeList.add(retail);
                        break;
                    }
                }
                if (flag) {
                    retail.setPercent(BigDecimal.ZERO);
                    adManagementFeeList.add(retail);
                }
            }
        }
        return adManagementFeeList;
    }
    /**
     * 日常运营费用合计=管理费用之和
     * 占比=？
     */
    private List<Retail> getTotalDailyOperatingExpensesList(List<Retail> generalManagementFeeList){
        List<String> deptNumList = CollectionUtil.extractToList(generalManagementFeeList,"deptNum");
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        List<Retail> costTotalList = Lists.newArrayList();
        for(String deptNum : deptNumSet){
            Retail retail = new Retail();
            retail.setFyName(RetailReportEnum.daily_operating_expenses_total.getFyName());
            retail.setFyNum(RetailReportEnum.daily_operating_expenses_total.getFyNum());
            retail.setAccName(RetailReportEnum.daily_operating_expenses_total.getAccName());
            BigDecimal amount = BigDecimal.ZERO;
            for(Retail cost : generalManagementFeeList){
                if (cost.getDeptNum().equals(deptNum)){
                    retail.setDeptNum(cost.getDeptNum());
                    retail.setDeptName(cost.getDeptName());
                    retail.setYear(cost.getYear());
                    retail.setMonth(cost.getMonth());
                    amount = amount.add(cost.getAmount());
                }
            }
            retail.setAmount(amount);
            costTotalList.add(retail);
        }
        return costTotalList;
    }
    /**
     * 一般管理费用
     * 占比=？
     */
     private List<Retail> getGeneralManagementFeeList(List<Retail> managementFeeList, List<Retail> netSalesRevenueList){
        List<Retail> generalManagementFeeList = Lists.newArrayList();
        for(Retail retail : managementFeeList){
            if(!retail.getFyNum().equals(RetailReportEnum.price_adjustment.getFyNum()) && !retail.getFyNum().equals(RetailReportEnum.sales_allowance.getFyNum()) && !retail.getFyNum().equals(RetailReportEnum.advertising_fee.getFyNum())){
                boolean flag = true;
                for (Retail netSalesRevenue : netSalesRevenueList) {
                    if (netSalesRevenue.getDeptName().equals(retail.getDeptName())) {
                        flag = false;
                        if (netSalesRevenue.getAmount().intValue() == 0) {
                            retail.setPercent(BigDecimal.ZERO);
                        } else {
                            retail.setPercent(retail.getAmount().divide(netSalesRevenue.getAmount(), 4, BigDecimal.ROUND_HALF_UP));
                        }
                        generalManagementFeeList.add(retail);
                        break;
                    }
                }
                if (flag) {
                    retail.setPercent(BigDecimal.ZERO);
                    generalManagementFeeList.add(retail);
                }
            }
        }
        return generalManagementFeeList;
    }
    /**
     * 增值业务利润=增值业务收入-(增值业务成本)
     * 占比=？
     */
    private List<Retail> getValueAddServiceProfitProfitList(List<Retail> incomeList, List<Retail> costList){
        List<Retail> itemDataList = Lists.newArrayList();
        List<Retail> valueAddServiceIncomeList = Lists.newArrayList();
        for(Retail cost : incomeList){
            if(cost.getFyNum().equals(RetailReportForIncomeEnum.valueAdd_service_income.getFyNum())){
                valueAddServiceIncomeList.add(cost);
            }
        }
        List<String> deptNumList = CollectionUtil.extractToList(valueAddServiceIncomeList,"deptNum");
        List<Retail> valueAddServiceCostList = Lists.newArrayList();
        for(Retail cost : costList){
            if(cost.getFyNum().equals(RetailReportForCostEnum.valueAdd_service_cost.getFyNum())){
                valueAddServiceCostList.add(cost);
            }
        }
        deptNumList.addAll(CollectionUtil.extractToList(valueAddServiceCostList,"deptNum"));
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        for(String deptNum : deptNumSet){
            boolean flag = true;
            boolean flag2 = true;
            BigDecimal amount = BigDecimal.ZERO;
            Retail accessoryGrossProfit = new Retail();
            accessoryGrossProfit.setFyName(RetailReportEnum.valueAdd_service_profit.getFyName());
            accessoryGrossProfit.setFyNum(RetailReportEnum.valueAdd_service_profit.getFyNum());
            accessoryGrossProfit.setAccName(RetailReportEnum.valueAdd_service_profit.getAccName());
            accessoryGrossProfit.setPercent(BigDecimal.ZERO);
            for (Retail detailCostOther : valueAddServiceCostList) {
                if (detailCostOther.getDeptNum().equals(deptNum)) {
                    flag2 = false;
                    accessoryGrossProfit.setYear(detailCostOther.getYear());
                    accessoryGrossProfit.setMonth(detailCostOther.getMonth());
                    accessoryGrossProfit.setDeptName(detailCostOther.getDeptName());
                    accessoryGrossProfit.setDeptNum(detailCostOther.getDeptNum());
                    amount = detailCostOther.getAmount();
                }
            }
            for (Retail incomeTotal : valueAddServiceIncomeList) {
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
    private List<Retail> getOperatingCommissionGrossProfitList(List<Retail> incomeList, List<Retail> costList){
        List<Retail> itemDataList = Lists.newArrayList();
        List<Retail> commissionIncomeList = Lists.newArrayList();
        for(Retail cost : incomeList){
            if(cost.getFyNum().equals(RetailReportForIncomeEnum.commission_income.getFyNum())){
                commissionIncomeList.add(cost);
            }
        }
        List<String> deptNumList = CollectionUtil.extractToList(commissionIncomeList,"deptNum");
        List<Retail> commissionCostList = Lists.newArrayList();
        for(Retail cost : costList){
            if(cost.getFyNum().equals(RetailReportForCostEnum.commission_cost.getFyNum())){
                commissionCostList.add(cost);
            }
        }
        deptNumList.addAll(CollectionUtil.extractToList(commissionCostList,"deptNum"));
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        for(String deptNum : deptNumSet){
            boolean flag = true;
            boolean flag2 = true;
            BigDecimal amount = BigDecimal.ZERO;
            Retail accessoryGrossProfit = new Retail();
            accessoryGrossProfit.setFyName(RetailReportEnum.operating_commission_gross_profit.getFyName());
            accessoryGrossProfit.setFyNum(RetailReportEnum.operating_commission_gross_profit.getFyNum());
            accessoryGrossProfit.setAccName(RetailReportEnum.operating_commission_gross_profit.getAccName());
            accessoryGrossProfit.setPercent(BigDecimal.ZERO);
            for (Retail detailCostOther : commissionCostList) {
                if (detailCostOther.getDeptNum().equals(deptNum)) {
                    flag2 = false;
                    accessoryGrossProfit.setYear(detailCostOther.getYear());
                    accessoryGrossProfit.setMonth(detailCostOther.getMonth());
                    accessoryGrossProfit.setDeptName(detailCostOther.getDeptName());
                    accessoryGrossProfit.setDeptNum(detailCostOther.getDeptNum());
                    amount = detailCostOther.getAmount();
                }
            }
            for (Retail incomeTotal : commissionIncomeList) {
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
    private List<Retail> getAccessoryGrossProfitList(List<Retail> incomeList, List<Retail> costList){
        List<Retail> itemDataList = Lists.newArrayList();
        List<Retail> accessoryIncomeList = Lists.newArrayList();
        for(Retail cost : incomeList){
            if(cost.getFyNum().equals(RetailReportForIncomeEnum.accessory_income.getFyNum())){
                accessoryIncomeList.add(cost);
            }
        }
        List<String> deptNumList = CollectionUtil.extractToList(accessoryIncomeList,"deptNum");
        List<Retail> accessoryCostList = Lists.newArrayList();
        for(Retail cost : costList){
            if(cost.getFyNum().equals(RetailReportForCostEnum.accessory_cost.getFyNum())){
                accessoryCostList.add(cost);
            }
        }
        deptNumList.addAll(CollectionUtil.extractToList(accessoryCostList,"deptNum"));
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        for(String deptNum : deptNumSet){
            boolean flag = true;
            boolean flag2 = true;
            BigDecimal amount = BigDecimal.ZERO;
            Retail accessoryGrossProfit = new Retail();
            accessoryGrossProfit.setFyName(RetailReportEnum.accessory_gross_profit.getFyName());
            accessoryGrossProfit.setFyNum(RetailReportEnum.accessory_gross_profit.getFyNum());
            accessoryGrossProfit.setAccName(RetailReportEnum.accessory_gross_profit.getAccName());
            accessoryGrossProfit.setPercent(BigDecimal.ZERO);
            for (Retail detailCostOther : accessoryCostList) {
                if (detailCostOther.getDeptNum().equals(deptNum)) {
                    flag2 = false;
                    accessoryGrossProfit.setYear(detailCostOther.getYear());
                    accessoryGrossProfit.setMonth(detailCostOther.getMonth());
                    accessoryGrossProfit.setDeptName(detailCostOther.getDeptName());
                    accessoryGrossProfit.setDeptNum(detailCostOther.getDeptNum());
                    amount = detailCostOther.getAmount();
                }
            }
            for (Retail incomeTotal : accessoryIncomeList) {
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
    private List<Retail> getAddPercentList(List<Retail> grossProfitList, List<Retail> netSalesRevenueList){
        List<Retail> grossProfitPercentList = Lists.newArrayList();
        for(Retail retail : grossProfitList){
            boolean flag = true;
            for (Retail netSalesRevenue : netSalesRevenueList) {
                if (netSalesRevenue.getDeptName().equals(retail.getDeptName())) {
                    flag = false;
                    if (netSalesRevenue.getAmount().intValue() == 0) {
                        retail.setPercent(BigDecimal.ZERO);
                    } else {
                        retail.setPercent(retail.getAmount().divide(netSalesRevenue.getAmount(), 4, BigDecimal.ROUND_HALF_UP));
                    }
                    grossProfitPercentList.add(retail);
                    break;
                }
            }
            if (flag) {
                retail.setPercent(BigDecimal.ZERO);
                grossProfitPercentList.add(retail);
            }
        }
        return grossProfitPercentList;
    }
    /**
     * 手机毛利润=手机收入-手机成本-(调价+销售折让)
     * 占比=？
     */
    private List<Retail> getMobileGrossProfitList(List<Retail> incomeList, List<Retail> costList, List<Retail> specialManagementFeeList){
        List<Retail> itemDataList = Lists.newArrayList();
        List<Retail> mobileIncomeList = Lists.newArrayList();
        for(Retail cost : incomeList){
            if(cost.getFyNum().equals(RetailReportForIncomeEnum.mobile_income.getFyNum())){
                mobileIncomeList.add(cost);
            }
        }
        List<String> deptNumList = CollectionUtil.extractToList(mobileIncomeList,"deptNum");
        List<Retail> mobileCostList = Lists.newArrayList();
        for(Retail cost : costList){
            if(cost.getFyNum().equals(RetailReportForCostEnum.mobile_cost.getFyNum())){
                mobileCostList.add(cost);
            }
        }
        deptNumList.addAll(CollectionUtil.extractToList(mobileCostList,"deptNum"));
        deptNumList.addAll(CollectionUtil.extractToList(specialManagementFeeList,"deptNum"));
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        for(String deptNum : deptNumSet){
            boolean flag = true;
            boolean flag2 = true;
            BigDecimal amount = BigDecimal.ZERO;
            Retail totalGrossProfitList = new Retail();
            totalGrossProfitList.setFyName(RetailReportEnum.mobile_gross_profit.getFyName());
            totalGrossProfitList.setFyNum(RetailReportEnum.mobile_gross_profit.getFyNum());
            totalGrossProfitList.setAccName(RetailReportEnum.mobile_gross_profit.getAccName());
            totalGrossProfitList.setPercent(BigDecimal.ZERO);
            List<Retail> list = Lists.newArrayList(mobileCostList);
            list.addAll(specialManagementFeeList);
            for (Retail detailCostOther : list) {
                if (detailCostOther.getDeptNum().equals(deptNum)) {
                    flag2 = false;
                    totalGrossProfitList.setYear(detailCostOther.getYear());
                    totalGrossProfitList.setMonth(detailCostOther.getMonth());
                    totalGrossProfitList.setDeptName(detailCostOther.getDeptName());
                    totalGrossProfitList.setDeptNum(detailCostOther.getDeptNum());
                    amount = amount.add(detailCostOther.getAmount());
                }
            }
            for (Retail incomeTotal : mobileIncomeList) {
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
     * 占比=？
     */
    private List<Retail> getTotalGrossProfitList(List<Retail> incomeTotalList, List<Retail> costTotalList, List<Retail> specialManagementFeeList){
        List<Retail> itemDataList = Lists.newArrayList();
         List<String> deptNumList = CollectionUtil.extractToList(incomeTotalList,"deptNum");
        deptNumList.addAll(CollectionUtil.extractToList(costTotalList,"deptNum"));
        deptNumList.addAll(CollectionUtil.extractToList(specialManagementFeeList,"deptNum"));
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        for(String deptNum : deptNumSet) {
            if (deptNum != null) {
                boolean flag = true;
                boolean flag2 = true;
                BigDecimal amount = BigDecimal.ZERO;
                BigDecimal percent = BigDecimal.ZERO;
                Retail totalGrossProfitList = new Retail();
                totalGrossProfitList.setFyName(RetailReportEnum.total_gross_profit.getFyName());
                totalGrossProfitList.setFyNum(RetailReportEnum.total_gross_profit.getFyNum());
                totalGrossProfitList.setAccName(RetailReportEnum.total_gross_profit.getAccName());
                totalGrossProfitList.setPercent(BigDecimal.ZERO);
                List<Retail> list = Lists.newArrayList(costTotalList);
                list.addAll(specialManagementFeeList);
                for (Retail entity : list) {
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
                for (Retail incomeTotal : incomeTotalList) {
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
                            percent = amount.divide(incomeTotal.getAmount(), 4, BigDecimal.ROUND_HALF_UP);
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
    private List<Retail> getNetSalesRevenueList(List<Retail> incomeList, List<Retail> specialManagementFeeList){
        List<Retail> itemDataList = Lists.newArrayList();
        List<Retail> mobileIncomeList = Lists.newArrayList();
        for(Retail cost : incomeList){
            if(cost.getFyNum().equals(RetailReportForIncomeEnum.mobile_income.getFyNum())){
                mobileIncomeList.add(cost);
            }
        }
        List<String> deptNumList = CollectionUtil.extractToList(mobileIncomeList,"deptNum");
        deptNumList.addAll(CollectionUtil.extractToList(specialManagementFeeList,"deptNum"));
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        for(String deptNum : deptNumSet){
            boolean flag = true;
            boolean flag2 = true;
            BigDecimal amount = BigDecimal.ZERO;
            Retail netSalesRevenue = new Retail();
            netSalesRevenue.setFyName(RetailReportEnum.net_sales_revenue.getFyName());
            netSalesRevenue.setFyNum(RetailReportEnum.net_sales_revenue.getFyNum());
            netSalesRevenue.setAccName(RetailReportEnum.net_sales_revenue.getAccName());
            netSalesRevenue.setPercent(BigDecimal.ZERO);
            for (Retail detailCostOther : specialManagementFeeList) {
                if (detailCostOther.getDeptNum().equals(deptNum)) {
                    flag2 = false;
                    netSalesRevenue.setYear(detailCostOther.getYear());
                    netSalesRevenue.setMonth(detailCostOther.getMonth());
                    netSalesRevenue.setDeptName(detailCostOther.getDeptName());
                    netSalesRevenue.setDeptNum(detailCostOther.getDeptNum());
                    amount = amount.add(detailCostOther.getAmount());
                }
            }
            for (Retail mobileIncome : mobileIncomeList) {
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
    private List<Retail> getSpecialManagementFeeList(List<Retail> managementFeeList){
        List<Retail> specialManagementFeeList = Lists.newArrayList();
        for(Retail managementFee: managementFeeList){
            if(managementFee.getFyNum().equals(RetailReportEnum.price_adjustment.getFyNum()) || managementFee.getFyNum().equals(RetailReportEnum.sales_allowance.getFyNum())){
                specialManagementFeeList.add(managementFee);
            }
        }
        return specialManagementFeeList;
    }
    private List<Retail> getCostTotalList(List<Retail> costList){
        List<String> deptNumList = CollectionUtil.extractToList(costList,"deptNum");
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        List<Retail> costTotalList = Lists.newArrayList();
        for(String deptNum : deptNumSet){
            Retail retail = new Retail();
            retail.setFyName(RetailReportEnum.cost_total.getFyName());
            retail.setFyNum(RetailReportEnum.cost_total.getFyNum());
            retail.setAccName(RetailReportEnum.cost_total.getAccName());
            BigDecimal amount = BigDecimal.ZERO;
            for(Retail cost : costList){
                if (cost.getDeptNum().equals(deptNum)){
                    retail.setDeptNum(cost.getDeptNum());
                    retail.setDeptName(cost.getDeptName());
                    retail.setYear(cost.getYear());
                    retail.setMonth(cost.getMonth());
                    amount = amount.add(cost.getAmount());
                }
            }
            retail.setAmount(amount);
            costTotalList.add(retail);
        }
        return costTotalList;
    }
    private List<Retail> getIncomeTotalList(List<Retail> incomeList){
        List<String> deptNumList = CollectionUtil.extractToList(incomeList,"deptNum");
        HashSet<String> deptNumSet = new HashSet<String>(deptNumList);
        List<Retail> incomeTotalList = Lists.newArrayList();
        for(String deptNum : deptNumSet){
            Retail retail = new Retail();
            retail.setFyName(RetailReportEnum.income_total.getFyName());
            retail.setFyNum(RetailReportEnum.income_total.getFyNum());
            retail.setAccName(RetailReportEnum.income_total.getAccName());
            BigDecimal amount = BigDecimal.ZERO;
            for(Retail income : incomeList){
                if (income.getDeptNum().equals(deptNum)){
                    retail.setYear(income.getYear());
                    retail.setMonth(income.getMonth());
                    retail.setDeptNum(income.getDeptNum());
                    retail.setDeptName(income.getDeptName());
                    amount = amount.add(income.getAmount());
                }
            }
            retail.setAmount(amount);
            incomeTotalList.add(retail);
        }
        return incomeTotalList;
    }

    /**
     * 成本list（其中分期服务费=其他业务支出的分期服务费+管理费用的分期服务费
     * @return
     */
    private void getNewCostList(List<Retail> costList,List<Retail> managementFeeList){
        List<String> deptNumList = CollectionUtil.extractToList(managementFeeList,"deptNum");
        Map<String,Retail> costMap = Maps.newHashMap();
        for(Retail cost : costList){
            costMap.put(cost.getFyNum(),cost);
        }
        for(String deptNum: deptNumList) {
            for (Retail managementFee : managementFeeList) {
                //管理费用的分期服务费
                if (deptNum.equals(managementFee.getDeptNum()) && managementFee.getAccName().equals("管理费用") && managementFee.getFyNum().equals(RetailReportForCostEnum.valueAdd_service_cost.getFyNum())) {
                    if(!costMap.containsKey("004")){
                        managementFee.setAccName(RetailReportForCostEnum.valueAdd_service_cost.getAccName());
                        costList.add(managementFee);
                    }else{
                        for (Retail cost : costList) {
                            if (cost.getFyNum().equals(RetailReportForCostEnum.valueAdd_service_cost.getFyNum()) && deptNum.equals(managementFee.getDeptNum()) ) {
                                cost.setAmount(cost.getAmount().add(managementFee.getAmount()));
                            }
                        }
                    }
                }
            }
        }
    }

}
