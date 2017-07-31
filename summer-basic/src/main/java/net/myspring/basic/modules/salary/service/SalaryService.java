package net.myspring.basic.modules.salary.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.dto.AccountDto;
import net.myspring.basic.modules.hr.repository.SalaryRepository;
import net.myspring.basic.modules.hr.repository.SalaryTemplateDetailRepository;
import net.myspring.basic.modules.hr.service.AccountService;
import net.myspring.basic.modules.salary.domain.Salary;
import net.myspring.basic.modules.salary.domain.SalaryTemplateDetail;
import net.myspring.basic.modules.salary.dto.SalaryDto;
import net.myspring.basic.modules.salary.web.form.SalaryForm;
import net.myspring.basic.modules.salary.web.query.SalaryQuery;
import net.myspring.basic.modules.sys.client.FolderFileClient;
import net.myspring.common.response.RestResponse;
import net.myspring.general.modules.sys.dto.FolderFileFeignDto;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly=true)
public class SalaryService {

    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private SalaryRepository salaryRepository;
    @Autowired
    private SalaryTemplateDetailRepository salaryTemplateDetailRepository;
    @Autowired
    private FolderFileClient folderFileClient;
    @Autowired
    private AccountService accountService;

    public Page<SalaryDto> findPage(Pageable pageable, SalaryQuery salaryQuery) {
        Page<SalaryDto> page = salaryRepository.findPage(pageable, salaryQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    @Transactional
    public RestResponse save(SalaryForm salaryForm) {
        StringBuilder sb = new StringBuilder();
        List<SalaryTemplateDetail> salaryTemplateDetailList = salaryTemplateDetailRepository.findBySalaryTemplateId(salaryForm.getSalaryTemplateId());
        Set<String> headerSet = CollectionUtil.extractToSet(salaryTemplateDetailList, "name");
        FolderFileFeignDto folderFileFeignDto = folderFileClient.findById(salaryForm.getFolderFileId());
        Workbook workbook = ExcelUtils.getWorkbook(new File(folderFileFeignDto.getUploadPath(RequestUtils.getCompanyName())));
        Sheet sheetAt = workbook.getSheetAt(0);
        int rowCount = sheetAt.getLastRowNum();
        if (rowCount >=1) {
            List<String> headers = Lists.newArrayList();
            for (int i = 0; i <= sheetAt.getRow(0).getLastCellNum(); i++) {
                headers.add((String) ExcelUtils.getCellValue(sheetAt.getRow(0).getCell(i)));
            }
            for (Object object : headerSet) {
                if (!headers.contains(object)) {
                    sb.append("列：" + object + "在导入数据中不存在<br/>");
                }
            }
            if (StringUtils.isBlank(sb.toString())) {
                List<Salary> salaryList = Lists.newArrayList();
                List<String> employeeIds = Lists.newArrayList();
                Row topRow = sheetAt.getRow(0);
                for (int i = 1; i <= rowCount; i++) {
                    Row row = sheetAt.getRow(i);
                    String loginName = (String) ExcelUtils.getCellValue(row.getCell(0));
                    AccountDto account = accountService.findByLoginName(loginName);
                    if (account != null) {
                        for (int j = 1; j < row.getLastCellNum(); j++) {
                            String projectName = (String) ExcelUtils.getCellValue(topRow.getCell(j));
                            String projectValue = String.valueOf(row.getCell(j));
                            if (headerSet.contains(projectName)) {
                                Salary salary = new Salary();
                                salary.setEmployeeId(account.getEmployeeId());
                                salary.setRemarks(salaryForm.getRemarks());
                                salary.setMonth(salaryForm.getMonth());
                                salary.setProjectName(projectName);
                                salary.setProjectValue(projectValue);
                                salary.setSalaryTemplateId(salaryForm.getSalaryTemplateId());
                                salaryList.add(salary);
                                employeeIds.add(account.getEmployeeId());
                            }
                        }
                    }
                }
                if (CollectionUtil.isNotEmpty(salaryList)) {
                    salaryRepository.deleteBySalaryTemplateId(salaryForm.getSalaryTemplateId(),employeeIds,salaryForm.getMonth());
                    salaryRepository.save(salaryList);
                }
            }
        } else {
            sb.append("数据为空");
        }
        RestResponse restResponse = new RestResponse("保存成功", null);
        if (StringUtils.isNotBlank(sb.toString())) {
            restResponse = new RestResponse(sb.toString(), null, false);
        }
        return restResponse;
    }
}
