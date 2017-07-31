package net.myspring.basic.modules.salary.service;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.repository.EmployeeRepository;
import net.myspring.basic.modules.hr.repository.SalaryTemplateDetailRepository;
import net.myspring.basic.modules.hr.repository.SalaryTemplateRepository;
import net.myspring.basic.modules.salary.domain.SalaryTemplate;
import net.myspring.basic.modules.salary.domain.SalaryTemplateDetail;
import net.myspring.basic.modules.salary.dto.SalaryTemplateDto;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaryTemplateService {

    @Autowired
    private SalaryTemplateRepository salaryTemplateRepository;
    @Autowired
    private SalaryTemplateDetailRepository salaryTemplateDetailRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<SalaryTemplateDto> findAll(){
        List<SalaryTemplate> salaryTemplateList=salaryTemplateRepository.findAll();
        List<SalaryTemplateDto> salaryTemplateDtoList= BeanUtil.map(salaryTemplateList,SalaryTemplateDto.class);
        return salaryTemplateDtoList;
    }

    public SimpleExcelBook findSimpleExcelSheet(String id)  {
        SalaryTemplate salaryTemplate=salaryTemplateRepository.findOne(id);
        Workbook workbook = new SXSSFWorkbook(100000);
        List<SimpleExcelColumn> simpleExcelColumnList= Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"accountName","登录名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"areaName","办事处"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"officeName","部门"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"name","姓名"));
        List<SalaryTemplateDetail> salaryTemplateDetailList=salaryTemplateDetailRepository.findBySalaryTemplateId(id);
        for(SalaryTemplateDetail salaryTemplateDetail:salaryTemplateDetailList) {
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"",salaryTemplateDetail.getName()));
        }
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("工资导入模版",Lists.newArrayList(),simpleExcelColumnList);
        ExcelUtils.doWrite(workbook,simpleExcelSheet);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"工资导入模版-" +salaryTemplate.getName()+ ".xlsx",simpleExcelSheet);
        return simpleExcelBook;
    }
}
