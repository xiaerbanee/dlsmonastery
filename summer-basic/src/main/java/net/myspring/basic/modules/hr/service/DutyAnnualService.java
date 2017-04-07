package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.enums.EmployeeStatusEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.domain.DutyAnnual;
import net.myspring.basic.modules.hr.domain.Employee;
import net.myspring.basic.modules.hr.dto.DutyAnnualDto;
import net.myspring.basic.modules.hr.mapper.AccountMapper;
import net.myspring.basic.modules.hr.mapper.DutyAnnualMapper;
import net.myspring.basic.modules.hr.mapper.EmployeeMapper;
import net.myspring.basic.modules.hr.web.query.DutyAnnualQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class DutyAnnualService {

    @Autowired
    private DutyAnnualMapper dutyAnnualMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public Double getAvailableHour(String  employeeId) {
        DutyAnnual dutyAnnual = dutyAnnualMapper.findByEmployee(employeeId);
        return dutyAnnual==null ? 0.0 : dutyAnnual.getLeftHour();
    }

    public DutyAnnual findOne(String id){
        DutyAnnual dutyAnnual=dutyAnnualMapper.findOne(id);
        return dutyAnnual;
    }

    public Page<DutyAnnualDto> findPage(Pageable pageable, DutyAnnualQuery dutyAnnualQuery){
        Page<DutyAnnual> page=dutyAnnualMapper.findPage(pageable,dutyAnnualQuery);
        Page<DutyAnnualDto> dutyAnnualDtoPage= BeanMapper.convertPage(page,DutyAnnualDto.class);
        cacheUtils.initCacheInput(dutyAnnualDtoPage.getContent());
        return dutyAnnualDtoPage;
    }

    public SimpleExcelSheet findSimpleExcelSheet(Workbook workbook){
        List<Employee> employeeList = employeeMapper.findByStatusAndregularDate(EmployeeStatusEnum.在职.name(), LocalDateTime.now().minusYears(1));
        List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"name","用户名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"account.loginName","登录名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"extendMap.hour","年假时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"extendMap.leftHour","剩余时间"));
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("年假导入模版",employeeList,simpleExcelColumnList);
        return simpleExcelSheet;
    }

    public void save(File file, String annualYear, String remarks){
        Workbook workbook= ExcelUtils.getWorkbook(file);
        List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn("employee.name","用户名"));
        simpleExcelColumnList.add(new SimpleExcelColumn("employee.account.loginName","登录名"));
        simpleExcelColumnList.add(new SimpleExcelColumn("hour","年假时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn("leftHour","剩余时间"));
        if(workbook!=null){
            List<DutyAnnual> list = ExcelUtils.doRead(workbook.getSheetAt(0), simpleExcelColumnList, DutyAnnual.class);
            List<Account> accountList= CollectionUtil.extractToList(CollectionUtil.extractToList(list,"employee"),"account");
            List<String> loginNameList=CollectionUtil.extractToList(accountList,"loginName");
            accountList=accountMapper.findByLoginNameList(loginNameList);
            Map<String,Account> accountMap=CollectionUtil.extractToMap(accountList,"loginName");
            for(DutyAnnual dutyAnnual:list){
                Account account=accountMap.get(dutyAnnual.getEmployee().getAccount().getLoginName());
                if(account==null&&!dutyAnnual.getEmployee().getName().equals(account.getEmployee().getName())){
                    break;
                }
                dutyAnnual.setEmployeeId(account.getEmployeeId());
                dutyAnnual.setAnnualYear(annualYear.substring(0,annualYear.indexOf("-")));
                dutyAnnual.setRemarks(remarks);
            }
            dutyAnnualMapper.batchSave(list);
        }
    }
}
