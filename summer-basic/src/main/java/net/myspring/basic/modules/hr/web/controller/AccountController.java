package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.dto.AccountDto;
import net.myspring.basic.modules.hr.dto.AuditFileDto;
import net.myspring.basic.modules.hr.dto.DutyDto;
import net.myspring.basic.modules.hr.service.*;
import net.myspring.basic.modules.hr.web.form.AccountForm;
import net.myspring.basic.modules.hr.web.form.AccountPositionForm;
import net.myspring.basic.modules.hr.web.query.AccountQuery;
import net.myspring.basic.modules.hr.web.query.AuditFileQuery;
import net.myspring.basic.modules.hr.web.validator.AccountValidator;
import net.myspring.basic.modules.sys.dto.AccountCommonDto;
import net.myspring.basic.modules.sys.dto.BackendMenuDto;
import net.myspring.basic.modules.sys.service.MenuService;
import net.myspring.basic.modules.sys.service.OfficeService;
import net.myspring.basic.modules.sys.service.PermissionService;
import net.myspring.basic.modules.sys.service.RoleService;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.common.enums.BoolEnum;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.common.tree.TreeNode;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDate;
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
    private RoleService roleService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private AccountValidator accountValidator;
    @Autowired
    private AuditFileService auditFileService;

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'hr:account:view')")
    public Page<AccountDto> list(Pageable pageable, AccountQuery accountQuery) {
        Page<AccountDto> page = accountService.findPage(pageable, accountQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    @PreAuthorize("hasPermission(null,'hr:account:delete')")
    public RestResponse delete(String id) {
        accountService.logicDelete(id);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    @PreAuthorize("hasPermission(null,'hr:account:edit')")
    public RestResponse save(AccountForm accountForm, BindingResult bindingResult) {
        accountValidator.validate(accountForm,bindingResult);
        if(bindingResult.hasErrors()){
            return new RestResponse(bindingResult,"保存失败", null);
        }
        RestResponse restResponse=new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        restResponse.getExtra().put("accountId",accountService.save(accountForm).getId());
        return restResponse;
    }

    @RequestMapping(value = "saveAccountPosition")
    public RestResponse saveAccountPosition(AccountPositionForm accountPositionForm) {
        accountService.saveAccountPosition(accountPositionForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public AccountDto findOne(AccountDto accountDto) {
        accountDto = accountService.findOne(accountDto);
        return accountDto;
    }

    @RequestMapping(value = "getForm")
    public AccountForm getForm(AccountForm accountForm) {
        accountForm.getExtra().put("positionDtoList", positionService.findAll());
        accountForm.getExtra().put("boolMap", BoolEnum.getMap());
        accountForm.getExtra().put("roleList", roleService.findByEnabledIsTrue());
        return accountForm;
    }

    @RequestMapping(value = "getQuery")
    public AccountQuery getQuery(AccountQuery accountQuery) {
        accountQuery.getExtra().put("positionList", positionService.findAll());
        return accountQuery;
    }

    @RequestMapping(value = "search")
    public List<AccountDto> search(String type, String key) {
        List<AccountDto> accountDtoList = accountService.findByLoginNameLikeAndType(type, key);
        return accountDtoList;
    }

    @RequestMapping(value = "findByIds")
    public List<AccountDto> findByIds(String idStr) {
        List<String> ids = StringUtils.getSplitList(idStr, CharConstant.COMMA);
        List<AccountDto> accountDtoList = accountService.findByIds(ids);
        return accountDtoList;
    }

    @RequestMapping(value = "checkLoginName")
    public RestResponse checkLoginName(AccountQuery accountQuery) {
        RestResponse restResponse = new RestResponse("登录名可用", null);
        if (!accountService.checkLoginName(accountQuery)) {
            restResponse = new RestResponse("登录名不能重复", null, false);
        }
        return restResponse;
    }


    @RequestMapping(value = "searchFilter")
    public List<AccountDto> searchFilter(AccountQuery accountQuery) {
        List<AccountDto> accountDtoList = accountService.findByFilter(accountQuery);
        return accountDtoList;
    }

    @RequestMapping(value = "findByEmployeeId")
    public AccountCommonDto findByEmployeeId(String employeeId) {
        Account account = accountService.findByEmployeeIdAndType(employeeId,"主账号");
        return BeanUtil.map(account,AccountCommonDto.class);
    }

    @RequestMapping(value = "findByEmployeeIdList")
    public List<AccountCommonDto> findByEmployeeIdList(@RequestBody List<String> employeeIdList) {
        List<Account> accountList = accountService.findByEmployeeIdInAndType(employeeIdList,"主账号");
        return BeanUtil.map(accountList, AccountCommonDto.class);
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export(AccountQuery accountQuery) throws IOException {
        SimpleExcelBook simpleExcelBook = accountService.findSimpleExcelSheet(accountQuery);
        ExcelView excelView = new ExcelView();
        return new ModelAndView(excelView,"simpleExcelBook",simpleExcelBook);
    }

    @RequestMapping(value = "getAccountInfo")
    public Map<String, Object> getAccountInfo(boolean isMobile) {
        String accountId = RequestUtils.getAccountId();
        List<String> roleIdList = RequestUtils.getRoleIdList();
        Map<String, Object> map = Maps.newHashMap();
        AccountDto accountDto = accountService.getAccountDto(accountId);
        List<String> authorityList = accountService.getAuthorityList(accountId);
        if(isMobile){
            List<Map<String, Object>> mobileMenus = menuService.findMobileMenus(accountId,roleIdList);
            map.put("menus", mobileMenus);
        }else {
            List<BackendMenuDto> menus = menuService.getMenusMap(accountId,roleIdList);
            map.put("menus", menus);
        }
        map.put("account", accountDto);
        map.put("authorityList", authorityList);
        return map;
    }

    @RequestMapping(value = "/home")
    public Map<String, Object> home() {
        Map<String, Object> map = Maps.newHashMap();
        AccountDto accountDto = accountService.getAccountDto(RequestUtils.getAccountId());
        cacheUtils.initCacheInput(accountDto);
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        List<DutyDto> dutyList = dutyService.findByAuditable(accountDto.getId(), AuditTypeEnum.APPLY.getValue(), lastMonth);
        Page<AuditFileDto> page = auditFileService.findPage(new PageRequest(0,10),new AuditFileQuery());
        map.put("dutySize", dutyList.size());
        map.put("auditFileSize", page.getTotalElements());
        //显示剩余的加班调休时间和年假时间
        String employeeId = RequestUtils.getEmployeeId();
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
        List<String> roleIdList = RequestUtils.getRoleIdList();
        TreeNode treeNode = permissionService.findRolePermissionTree(roleIdList);
        return treeNode;
    }

    @RequestMapping(value = "getTreeCheckData")
    public List<String> getTreeCheckData(String id) {
        List<String> permissionIdList = permissionService.getAccountPermissionCheckData(id);
        return permissionIdList;
    }

    @RequestMapping(value = "findByLoginNameList")
    public List<AccountCommonDto> findByLoginNameList(@RequestParam(value = "loginNameList") List<String> loginNameList) {
        List<AccountCommonDto> accountCommonDtoList = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(loginNameList)) {
            List<AccountDto> accountDtoList=accountService.findByLoginNameList(loginNameList);
            accountCommonDtoList= BeanUtil.map(accountDtoList,AccountCommonDto.class);
        }
        return accountCommonDtoList;
    }

    @RequestMapping(value = "findByLoginNameLike")
    public List<AccountCommonDto> findByLoginNameLike(@RequestParam(value = "loginName") String loginName) {
        List<AccountCommonDto> accountCommonDtoList = Lists.newArrayList();
        if (StringUtils.isNotBlank(loginName)) {
            List<AccountDto> accountDtoList=accountService.findByLoginNameLike(loginName);
            accountCommonDtoList= BeanUtil.map(accountDtoList,AccountCommonDto.class);
        }
        return accountCommonDtoList;
    }

    @RequestMapping(value = "getAccountId")
    public Map<String,Object> getAccountId() {
        Map<String,Object> map = Maps.newHashMap();
        map.put("accountId",RequestUtils.getAccountId());
        return map;
    }

    @RequestMapping(value = "findByOfficeId")
    public Map<String,Object> findByOfficeId(String officeId) {
        Map<String,Object> map=Maps.newHashMap();
        if (StringUtils.isNotBlank(officeId)){
            List<String> officeIdList=officeService.getSameAreaByOfficeId(officeId);
            map.put("allAccount",accountService.findByOfficeIdList(officeIdList));
            map.put("currentAccount",accountService.findByOfficeIdList(Lists.newArrayList(officeId)));
        }
        return map;
    }

    @RequestMapping(value="updatePwd")
    public RestResponse updatePwd(String password,String confirmPassword){
        if(StringUtils.isBlank(password)||StringUtils.isBlank(confirmPassword)){
            throw new ServiceException("请输入密码和确认密码");
        }else if(!password.equals(confirmPassword)){
            throw new ServiceException("密码不一致");
        }
        accountService.updatePwd(password);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());

    }

}
