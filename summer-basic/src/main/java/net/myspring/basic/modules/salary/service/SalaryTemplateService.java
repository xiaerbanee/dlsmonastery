package net.myspring.basic.modules.salary.service;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.repository.EmployeeRepository;
import net.myspring.basic.modules.hr.repository.SalaryTemplateDetailRepository;
import net.myspring.basic.modules.hr.repository.SalaryTemplateRepository;
import net.myspring.basic.modules.salary.domain.SalaryTemplate;
import net.myspring.basic.modules.salary.domain.SalaryTemplateDetail;
import net.myspring.basic.modules.salary.dto.SalaryTemplateDetailDto;
import net.myspring.basic.modules.salary.dto.SalaryTemplateDto;
import net.myspring.basic.modules.salary.web.form.SalaryTemplateDetailForm;
import net.myspring.basic.modules.salary.web.form.SalaryTemplateForm;
import net.myspring.basic.modules.salary.web.query.SalaryTemplateQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SalaryTemplateService {

    @Autowired
    private SalaryTemplateRepository salaryTemplateRepository;
    @Autowired
    private SalaryTemplateDetailRepository salaryTemplateDetailRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public Page<SalaryTemplateDto> findPage(Pageable pageable, SalaryTemplateQuery salaryTemplateQuery) {
        Page<SalaryTemplateDto> page=salaryTemplateRepository.findPage(pageable,salaryTemplateQuery);
        return page;
    }

    public SalaryTemplate save(SalaryTemplateForm salaryTemplateForm) {
        SalaryTemplate salaryTemplate;
        if(salaryTemplateForm.isCreate()){
            salaryTemplate=BeanUtil.map(salaryTemplateForm,SalaryTemplate.class);
        }else {
            salaryTemplate=salaryTemplateRepository.findOne(salaryTemplateForm.getId());
            ReflectionUtil.copyProperties(salaryTemplateForm,salaryTemplate);
        }
        salaryTemplateRepository.save(salaryTemplate);
        List<SalaryTemplateDetail> salaryTemplateDetailList=salaryTemplateDetailRepository.findBySalaryTemplateId(salaryTemplate.getId());
        Map<String,SalaryTemplateDetail> salaryTemplateDetailMap=CollectionUtil.extractToMap(salaryTemplateDetailList,"name");
        for(SalaryTemplateDetailForm salaryTemplateDetailForm:salaryTemplateForm.getSalaryTemplateDetailFormList()){
            SalaryTemplateDetail salaryTemplateDetail=salaryTemplateDetailMap.get(salaryTemplateDetailForm.getName());
            if(salaryTemplateDetail==null){
                salaryTemplateDetail=BeanUtil.map(salaryTemplateDetail,SalaryTemplateDetail.class);
                salaryTemplateDetailList.add(salaryTemplateDetail);
            }else {
                ReflectionUtil.copyProperties(salaryTemplateDetailForm,salaryTemplateDetail);
            }
        }
        salaryTemplateDetailRepository.save(salaryTemplateDetailList);
        return salaryTemplate;
    }

    public SalaryTemplateDto findOne(SalaryTemplateDto salaryTemplateDto) {
        if(!salaryTemplateDto.isCreate()){
            SalaryTemplate salaryTemplate=salaryTemplateRepository.findOne(salaryTemplateDto.getId());
            List<SalaryTemplateDetail> salaryTemplateDetailList=salaryTemplateDetailRepository.findBySalaryTemplateId(salaryTemplateDto.getId());
            salaryTemplateDto= BeanUtil.map(salaryTemplate,SalaryTemplateDto.class);
            salaryTemplateDto.setSalaryTemplateDetailDtoList(BeanUtil.map(salaryTemplateDetailList, SalaryTemplateDetailDto.class));
        }
        return salaryTemplateDto;
    }

    public  void delete(String id){
        List<SalaryTemplateDetail> salaryTemplateDetailList=salaryTemplateDetailRepository.findBySalaryTemplateId(id);
        if(CollectionUtil.isNotEmpty(salaryTemplateDetailList)){
            for(SalaryTemplateDetail salaryTemplateDetail:salaryTemplateDetailList){
                salaryTemplateDetailRepository.logicDelete(salaryTemplateDetail.getId());
            }
        }
        salaryTemplateRepository.logicDelete(id);
    }

    public List<SalaryTemplateDto> findAll(){
        List<SalaryTemplate> salaryTemplateList=salaryTemplateRepository.findAll();
        List<SalaryTemplateDto> salaryTemplateDtoList= BeanUtil.map(salaryTemplateList,SalaryTemplateDto.class);
        return salaryTemplateDtoList;
    }

    public SimpleExcelBook findSimpleExcelSheet(String id)  {
        SalaryTemplate salaryTemplate=salaryTemplateRepository.findOne(id);
        Workbook workbook = new SXSSFWorkbook(100000);
        List<SimpleExcelColumn> simpleExcelColumnList= Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"","登录名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"","办事处"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"","部门"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"","姓名"));
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
