package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.basic.common.enums.EmployeeStatusEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.domain.DutyAnnual;
import net.myspring.basic.modules.hr.domain.Employee;
import net.myspring.basic.modules.hr.dto.DutyAnnualDto;
import net.myspring.basic.modules.hr.dto.EmployeeDto;
import net.myspring.basic.modules.hr.mapper.AccountMapper;
import net.myspring.basic.modules.hr.mapper.DutyAnnualMapper;
import net.myspring.basic.modules.hr.mapper.EmployeeMapper;
import net.myspring.basic.modules.hr.web.form.DutyAnnualForm;
import net.myspring.basic.modules.hr.web.query.DutyAnnualQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
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
    private GridFsTemplate tempGridFsTemplate;
    @Autowired
    private GridFsTemplate storageGridFsTemplate;

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
        List<Employee> employeeList = employeeMapper.findByStatusAndregularDate("在职", LocalDateTime.now().minusYears(1));
        List<EmployeeDto> employeeDtoList= BeanUtil.map(employeeList,EmployeeDto.class);
        List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"name","用户名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"accountName","登录名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"","年假时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"","剩余时间"));
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("年假导入模版",employeeDtoList,simpleExcelColumnList);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"年假导入模版"+ UUID.randomUUID()+".xlsx",simpleExcelSheet);
        ByteArrayInputStream byteArrayInputStream=ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8",SecurityUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());
    }

    public void save(String mongoId, String annualYear, String remarks){
        GridFSDBFile gridFSDBFile = storageGridFsTemplate.findOne(new Query(Criteria.where("_id").is(mongoId)));
        Workbook workbook= ExcelUtils.getWorkbook(gridFSDBFile.getFilename(),gridFSDBFile.getInputStream());
        List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn("employeeName","用户名"));
        simpleExcelColumnList.add(new SimpleExcelColumn("loginName","登录名"));
        simpleExcelColumnList.add(new SimpleExcelColumn("hour","年假时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn("leftHour","剩余时间"));
        if(workbook!=null){
            List<DutyAnnualForm> list = doRead(workbook.getSheetAt(0), simpleExcelColumnList, DutyAnnualForm.class);
            List<Employee> employeeList=employeeMapper.findByNameList(CollectionUtil.extractToList(list,"employeeName"));
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
            dutyAnnualMapper.batchSave(dutyAnnualList);
        }
    }
}
