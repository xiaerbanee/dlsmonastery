package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.config.ExcelView;
import net.myspring.basic.common.enums.AuditTypeEnum;
import net.myspring.basic.common.enums.BoolEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.dto.AccountDto;
import net.myspring.basic.modules.hr.dto.AccountMessageDto;
import net.myspring.basic.modules.hr.dto.AccountTaskDto;
import net.myspring.basic.modules.hr.dto.DutyDto;
import net.myspring.basic.modules.hr.service.*;
import net.myspring.basic.modules.hr.web.form.AccountForm;
import net.myspring.basic.modules.hr.web.query.AccountQuery;
import net.myspring.basic.modules.sys.model.MenuCategoryItem;
import net.myspring.basic.modules.sys.service.MenuService;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelSheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/3/19.
 */
@RestController
@RequestMapping(value = "hr/account")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private PositionService positionService;

    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private AccountTaskService accountTaskService;
    @Autowired
    private AccountMessageService accountMessageService;
    @Autowired
    private DutyService dutyService;
    @Autowired
    private DutyAnnualService dutyAnnualService;
    @Autowired
    private DutyOvertimeService dutyOvertimeService;
    @Autowired
    private MenuService menuService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AccountDto> list(Pageable pageable, AccountQuery accountQuery) {
        Page<AccountDto> page = accountService.findPage(pageable, accountQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        accountService.logicDeleteOne(id);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(AccountForm accountForm) {
        accountService.save(accountForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public AccountForm findOne(AccountForm accountForm) {
        accountForm = accountService.findForm(accountForm);
        accountForm.setPositionDtoList(positionService.findAll());
        accountForm.setBoolMap( BoolEnum.getMap());
        return accountForm;
    }

    @RequestMapping(value = "getQuery")
    public AccountQuery getQuery(AccountQuery accountQuery) {
        accountQuery.setPositionList(positionService.findAll());
        return accountQuery;
    }

    @RequestMapping(value = "search")
    public List<AccountDto> search(String type, String key) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("name", key);
        map.put("type", type);
        List<AccountDto> accountDtoList = accountService.findByLoginNameLikeAndType(map);
        return accountDtoList;
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export(AccountQuery accountQuery) {
        ExcelView excelView = new ExcelView();
        Workbook workbook = new SXSSFWorkbook(Const.DEFAULT_PAGE_SIZE);
        List<SimpleExcelSheet> simpleExcelSheetList = accountService.findSimpleExcelSheets(workbook, accountQuery);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook, "账户信息.xlsx", simpleExcelSheetList);
        return new ModelAndView(excelView, "simpleExcelBook", simpleExcelBook);
    }

    @RequestMapping(value = "getAuthorityList")
    public List<String> getAuthorityList() {
        List<String> authorityList = accountService.getAuthorityList();
        return authorityList;
    }

    @RequestMapping(value = "getAccountMessage")
    public Map<String, Object> getAccount() {
        Map<String, Object> map = Maps.newHashMap();
        AccountDto accountDto = accountService.getAccount();
        List<String> authorityList = accountService.getAuthorityList();
        List<MenuCategoryItem> menus = menuService.findMenus(SecurityUtils.getAccountId());
        map.put("account", accountDto);
        map.put("authorityList", authorityList);
        map.put("menus", menus);
        return map;
    }

    @RequestMapping(value = "/home")
    public Map<String, Object> home() {
        Map<String, Object> map = Maps.newHashMap();
        AccountDto accountDto = accountService.getAccount();
        cacheUtils.initCacheInput(accountDto);
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        List<DutyDto> dutyList = dutyService.findByAuditable(accountDto.getId(), AuditTypeEnum.APPLY.getValue(), lastMonth);
        List<AccountTaskDto> accountTasks = accountTaskService.findByPositionId(accountDto.getPositionId(), accountService.findOne(SecurityUtils.getAccountId()));
        map.put("accountTaskSize", accountTasks.size());
        List<AccountMessageDto> accountMessages = accountMessageService.findByAccount(accountDto.getId(), lastMonth);
        map.put("dutySize", dutyList.size());
        map.put("accountMessageSize", accountMessages.size());
        //显示剩余的加班调休时间和年假时间
        String employeeId = SecurityUtils.getEmployeeId();
        map.put("annualHour", dutyAnnualService.getAvailableHour(employeeId));
        map.put("overtimeHour", dutyOvertimeService.getAvailableHour(employeeId, LocalDateTime.now()));
        //显示快到期时间
        map.put("expiredHour", dutyOvertimeService.getExpiredHour(employeeId, LocalDateTime.now()));
        map.put("account", accountDto);
        return map;
    }

}
