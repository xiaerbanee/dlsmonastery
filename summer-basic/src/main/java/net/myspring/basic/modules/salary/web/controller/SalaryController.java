package net.myspring.basic.modules.salary.web.controller;

import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.dto.EmployeeDto;
import net.myspring.basic.modules.hr.service.AccountService;
import net.myspring.basic.modules.hr.service.EmployeeService;
import net.myspring.basic.modules.salary.dto.SalaryDto;
import net.myspring.basic.modules.salary.service.SalaryService;
import net.myspring.basic.modules.salary.service.SalaryTemplateService;
import net.myspring.basic.modules.salary.web.form.SalaryForm;
import net.myspring.basic.modules.salary.web.query.SalaryQuery;
import net.myspring.common.response.RestResponse;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "hr/salary")
public class SalaryController{
	@Autowired
	private SalaryService salaryService;
	@Autowired
	private SalaryTemplateService salaryTemplateService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private AccountService accountService;

	@RequestMapping( method = RequestMethod.GET)
	public Page<SalaryDto> data(Pageable pageable, SalaryQuery salaryQuery) {
		Page<SalaryDto> page=null;
		if(StringUtils.isNotBlank(salaryQuery.getPassword())){
			EmployeeDto employee=employeeService.findOne(salaryQuery.getEmployeeId());
			Account account=accountService.findById(employee.getAccountId());
			if(account!=null&&StringUtils.validatePassword(salaryQuery.getPassword(),account.getPassword())){
				page=salaryService.findPage(pageable,salaryQuery);
			}
		}
		return page;
	}

	@RequestMapping(value = "getQury", method = RequestMethod.GET)
	public SalaryQuery form(SalaryQuery salaryQuery) {
		return salaryQuery;
	}

	@RequestMapping(value = "getForm", method = RequestMethod.GET)
	public SalaryForm form(SalaryForm salaryForm) {
		salaryForm.getExtra().put("salaryTemplates",salaryTemplateService.findAll());
		return salaryForm;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public RestResponse save(@RequestParam(value ="folderFileId", required = true) String folderFileId, String month, String salaryTemplateId,String remarks) {
		RestResponse restResponse=salaryService.save(folderFileId,month,salaryTemplateId,remarks);
		return restResponse;
	}
}
