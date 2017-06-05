package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.dto.AccountDto;
import net.myspring.basic.modules.hr.dto.AccountMessageDto;
import net.myspring.basic.modules.hr.dto.DutyDto;
import net.myspring.basic.modules.hr.service.*;
import net.myspring.basic.modules.hr.web.form.AccountForm;
import net.myspring.basic.modules.hr.web.query.AccountQuery;
import net.myspring.basic.modules.sys.dto.BackendMenuDto;
import net.myspring.basic.modules.sys.manager.RoleManager;
import net.myspring.basic.modules.sys.service.MenuService;
import net.myspring.basic.modules.sys.service.PermissionService;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.common.enums.BoolEnum;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.common.tree.TreeNode;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    private AccountMessageService accountMessageService;
    @Autowired
    private DutyService dutyService;
    @Autowired
    private DutyAnnualService dutyAnnualService;
    @Autowired
    private DutyOvertimeService dutyOvertimeService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RoleManager roleManager;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AccountDto> list(Pageable pageable, AccountQuery accountQuery) {
        Page<AccountDto> page = accountService.findPage(pageable, accountQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        accountService.logicDelete(id);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(AccountForm accountForm) {
        accountService.save(accountForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public AccountDto findOne(AccountDto accountDto) {
        accountDto = accountService.findOne(accountDto);
        return accountDto;
    }

    @RequestMapping(value = "getForm")
    public AccountForm getForm(AccountForm accountForm) {
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
        List<AccountDto> accountDtoList = accountService.findByLoginNameLikeAndType(type,key);
        return accountDtoList;
    }

    @RequestMapping(value = "findByIds")
    public List<AccountDto> findByIds(String idStr) {
        List<String> ids=StringUtils.getSplitList(idStr,CharConstant.COMMA);
        List<AccountDto> accountDtoList =accountService.findByIds(ids);
        return accountDtoList;
    }

    @RequestMapping(value="checkLoginName")
        public RestResponse checkLoginName(AccountQuery accountQuery){
        RestResponse restResponse=new RestResponse("登录名可用", null);
        if(!accountService.checkLoginName(accountQuery)) {
            restResponse = new RestResponse("登录名不能重复", null,false);
        }
        return restResponse;
    }


    @RequestMapping(value = "searchFilter")
    public List<AccountDto> searchFilter(AccountQuery accountQuery) {
        List<AccountDto> accountDtoList = accountService.findByFilter(accountQuery);
        return accountDtoList;
    }


    @RequestMapping(value = "export", method = RequestMethod.GET)
    public String export(AccountQuery accountQuery) throws IOException {
        Workbook workbook = new SXSSFWorkbook(10000);
        return accountService.findSimpleExcelSheet(workbook,accountQuery);
    }

    @RequestMapping(value = "getAccountInfo")
    public Map<String, Object> getAccountInfo() {
        String accountId = RequestUtils.getAccountId();
        Map<String, Object> map = Maps.newHashMap();
        AccountDto accountDto = accountService.getAccountDto(accountId);
        List<String> authorityList = accountService.getAuthorityList();
        List<BackendMenuDto> menus = menuService.getMenuMap();
        map.put("account", accountDto);
        map.put("authorityList", authorityList);
        map.put("menus", menus);
        return map;
    }

    @RequestMapping(value = "/home")
    public Map<String, Object> home() {
        Map<String, Object> map = Maps.newHashMap();
        AccountDto accountDto = accountService.getAccountDto(RequestUtils.getAccountId());
        cacheUtils.initCacheInput(accountDto);
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        List<DutyDto> dutyList = dutyService.findByAuditable(accountDto.getId(), AuditTypeEnum.APPLYING.toString(), lastMonth);
        List<AccountMessageDto> accountMessages = accountMessageService.findByAccount(accountDto.getId(), lastMonth);
        map.put("dutySize", dutyList.size());
        map.put("accountMessageSize", accountMessages.size());
        //显示剩余的加班调休时间和年假时间
        String employeeId = RequestUtils.getRequestEntity().getEmployeeId();
        map.put("annualHour", dutyAnnualService.getAvailableHour(employeeId));
        map.put("overtimeHour", dutyOvertimeService.getAvailableHour(employeeId, LocalDate.now()));
        //显示快到期时间
        map.put("expiredHour", dutyOvertimeService.getExpiredHour(employeeId, LocalDate.now()));
        map.put("account", accountDto);
        return map;
    }

    @RequestMapping(value = "saveAuthorityList")
    public RestResponse saveAuthorityList(AccountForm accountForm) {
        accountService.saveAccountAndPermission(accountForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getTreeNode")
    public TreeNode getTreeNode() {
        String roleId=roleManager.findIdByAccountId(RequestUtils.getAccountId());
        TreeNode treeNode=permissionService.findRolePermissionTree(roleId);
        return treeNode;
    }

    @RequestMapping(value = "getTreeCheckData")
    public List<String> getTreeCheckData(String id) {
        List<String> permissionIdList=permissionService.getAccountPermissionCheckData(id);
        return permissionIdList;
    }

}
