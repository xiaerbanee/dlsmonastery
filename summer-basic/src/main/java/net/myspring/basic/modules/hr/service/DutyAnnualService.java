package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.DutyAnnual;
import net.myspring.basic.modules.hr.domain.Employee;
import net.myspring.basic.modules.hr.dto.DutyAnnualDto;
import net.myspring.basic.modules.hr.dto.EmployeeDto;
import net.myspring.basic.modules.hr.repository.DutyAnnualRepository;
import net.myspring.basic.modules.hr.repository.EmployeeRepository;
import net.myspring.basic.modules.hr.web.form.DutyAnnualForm;
import net.myspring.basic.modules.hr.web.query.DutyAnnualQuery;
import net.myspring.basic.modules.sys.client.FolderFileClient;
import net.myspring.general.modules.sys.dto.FolderFileFeignDto;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static net.myspring.util.excel.ExcelUtils.*;

@Service
@Transactional(readOnly = true)
public class DutyAnnualService {

    @Autowired
    private DutyAnnualRepository dutyAnnualRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private FolderFileClient folderFileClient;

    public Double getAvailableHour(String  employeeId) {
        List<DutyAnnual> dutyAnnualList = dutyAnnualRepository.findByEmployeeId(employeeId);
        if(CollectionUtil.isNotEmpty(dutyAnnualList)){
            return dutyAnnualList .get(0).getLeftHour();
        }
        return 0.0;
    }

    public DutyAnnual findOne(String id){
        DutyAnnual dutyAnnual=dutyAnnualRepository.findOne(id);
        return dutyAnnual;
    }

    public Page<DutyAnnualDto> findPage(Pageable pageable, DutyAnnualQuery dutyAnnualQuery){
        Page<DutyAnnualDto> dutyAnnualDtoPage= dutyAnnualRepository.findPage(pageable,dutyAnnualQuery);
        cacheUtils.initCacheInput(dutyAnnualDtoPage.getContent());
        return dutyAnnualDtoPage;
    }

    public SimpleExcelBook findSimpleExcelSheet()  {
        Workbook workbook = new SXSSFWorkbook(10000);
        List<Employee> employeeList = employeeRepository.findByStatusAndregularDate("在职", LocalDate.now().minusYears(1));
        List<EmployeeDto> employeeDtoList= BeanUtil.map(employeeList,EmployeeDto.class);
        List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"name","用户名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"accountName","登录名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"","年假时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"","剩余时间"));
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("年假导入模版",employeeDtoList,simpleExcelColumnList);
        ExcelUtils.doWrite(workbook,simpleExcelSheet);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"年假导入模版"+ UUID.randomUUID()+".xlsx",simpleExcelSheet);
        return simpleExcelBook;
    }
    @Transactional
    public void save(String folderFileId, String annualYear, String remarks){
        FolderFileFeignDto folderFileFeignDto=folderFileClient.findById(folderFileId);
        Workbook workbook= ExcelUtils.getWorkbook(new File(folderFileFeignDto.getUploadPath(RequestUtils.getCompanyName())));
        List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn("employeeName","用户名"));
        simpleExcelColumnList.add(new SimpleExcelColumn("loginName","登录名"));
        simpleExcelColumnList.add(new SimpleExcelColumn("hour","年假时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn("leftHour","剩余时间"));
        if(workbook!=null){
            List<DutyAnnualForm> list = doRead(workbook.getSheetAt(0), simpleExcelColumnList, DutyAnnualForm.class);
            List<Employee> employeeList=employeeRepository.findByEnabledIsTrueAndNameIn(CollectionUtil.extractToList(list,"employeeName"));
            Map<String,Employee> employeeMap=CollectionUtil.extractToMap(employeeList,"name");
            List<DutyAnnual> dutyAnnualList=Lists.newArrayList();
            for(DutyAnnualForm dutyAnnualForm:list){
                if(StringUtils.isNotBlank(dutyAnnualForm.getEmployeeName())){
                    dutyAnnualForm.setAnnualYear(annualYear);
                    dutyAnnualForm.setEmployeeId(employeeMap.get(dutyAnnualForm.getEmployeeName()).getId());
                    dutyAnnualForm.setRemarks(remarks);
                    dutyAnnualList.add(BeanUtil.map(dutyAnnualForm,DutyAnnual.class));
                }
            }
            if(CollectionUtil.isNotEmpty(dutyAnnualList)){
                dutyAnnualRepository.deleteByEmployeeId(dutyAnnualList.stream().map(DutyAnnual::getEmployeeId).collect(Collectors.toList()));
                dutyAnnualRepository.save(dutyAnnualList);
            }
        }
    }
}
