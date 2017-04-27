package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import jdk.internal.util.xml.impl.Input;
import net.myspring.basic.common.enums.EmployeeStatusEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.client.ExcelExportClient;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.domain.DutyAnnual;
import net.myspring.basic.modules.hr.domain.Employee;
import net.myspring.basic.modules.hr.dto.DutyAnnualDto;
import net.myspring.basic.modules.hr.dto.EmployeeDto;
import net.myspring.basic.modules.hr.mapper.AccountMapper;
import net.myspring.basic.modules.hr.mapper.DutyAnnualMapper;
import net.myspring.basic.modules.hr.mapper.EmployeeMapper;
import net.myspring.basic.modules.hr.web.query.DutyAnnualQuery;
import net.myspring.general.modules.sys.form.ExcelExportForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.time.LocalDateTimeUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.myspring.util.excel.ExcelUtils.*;

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
    @Autowired
    private ExcelExportClient excelExportClient;

    public Double getAvailableHour(String  employeeId) {
        DutyAnnual dutyAnnual = dutyAnnualMapper.findByEmployee(employeeId);
        return dutyAnnual==null ? 0.0 : dutyAnnual.getLeftHour();
    }

    public DutyAnnual findOne(String id){
        DutyAnnual dutyAnnual=dutyAnnualMapper.findOne(id);
        return dutyAnnual;
    }

    public Page<DutyAnnualDto> findPage(Pageable pageable, DutyAnnualQuery dutyAnnualQuery){
        Page<DutyAnnualDto> dutyAnnualDtoPage= dutyAnnualMapper.findPage(pageable,dutyAnnualQuery);
        cacheUtils.initCacheInput(dutyAnnualDtoPage.getContent());
        return dutyAnnualDtoPage;
    }

    public String findSimpleExcelSheet(Workbook workbook) throws IOException {
        List<Employee> employeeList = Lists.newArrayList();
        List<EmployeeDto> employeeDtoList= BeanUtil.map(employeeList,EmployeeDto.class);
        List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"name","用户名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"accountName","登录名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"","年假时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"","剩余时间"));
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("年假导入模版",employeeDtoList,simpleExcelColumnList);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"年假导入模版"+ UUID.randomUUID()+".xlsx",simpleExcelSheet);
        ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        simpleExcelBook.getWorkbook().write(outputStream);
        ExcelExportForm excelExportForm=new ExcelExportForm();
        excelExportForm.setOutputStream(outputStream);
        excelExportForm.setFileName(simpleExcelBook.getName());
        String mongoDbId = excelExportClient.uploadToMongoDb(excelExportForm);
        return mongoDbId.substring(1,mongoDbId.length()-1);
    }

    public void save(File file, String annualYear, String remarks){
        Workbook workbook= getWorkbook(file);
        List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn("name","用户名"));
        simpleExcelColumnList.add(new SimpleExcelColumn("loginName","登录名"));
        simpleExcelColumnList.add(new SimpleExcelColumn("hour","年假时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn("leftHour","剩余时间"));
        if(workbook!=null){
            List<DutyAnnual> list = doRead(workbook.getSheetAt(0), simpleExcelColumnList, DutyAnnual.class);
            List<DutyAnnualDto> dutyAnnualDtoList=BeanUtil.map(list,DutyAnnualDto.class);
            cacheUtils.initCacheInput(dutyAnnualDtoList);
            List<Account> accountList= CollectionUtil.extractToList(CollectionUtil.extractToList(list,"employee"),"account");
            List<String> loginNameList=CollectionUtil.extractToList(accountList,"loginName");
            accountList=accountMapper.findByLoginNameList(loginNameList);
            Map<String,Account> accountMap=CollectionUtil.extractToMap(accountList,"loginName");
            for(DutyAnnualDto dutyAnnualDto:dutyAnnualDtoList){
                Account account=accountMap.get(dutyAnnualDto.getLoginName());
                dutyAnnualDto.setEmployeeId(account.getEmployeeId());
                dutyAnnualDto.setAnnualYear(annualYear.substring(0,annualYear.indexOf("-")));
                dutyAnnualDto.setRemarks(remarks);
            }
            dutyAnnualMapper.batchSave(list);
        }
    }
}
