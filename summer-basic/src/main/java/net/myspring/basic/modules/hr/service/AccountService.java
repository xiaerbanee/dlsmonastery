package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.domain.AccountPermission;
import net.myspring.basic.modules.hr.dto.AccountDto;
import net.myspring.basic.modules.hr.mapper.AccountMapper;
import net.myspring.basic.modules.hr.mapper.AccountPermissionMapper;
import net.myspring.basic.modules.hr.mapper.EmployeeMapper;
import net.myspring.basic.modules.hr.web.form.AccountForm;
import net.myspring.basic.modules.hr.web.query.AccountQuery;
import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.modules.sys.mapper.PermissionMapper;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/3/19.
 */
@Service
public class AccountService {

    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private AccountPermissionMapper accountPermissionMapper;

    public Account findOne(String id) {
        Account account = accountMapper.findOne(id);
        return account;
    }

    public AccountForm findForm(AccountForm accountForm) {
        if(!accountForm.isCreate()){
            Account account = accountMapper.findOne(accountForm.getId());
            accountForm = BeanUtil.map(account, AccountForm.class);
            cacheUtils.initCacheInput(accountForm);
        }
        return accountForm;
    }

    public AccountDto findByLoginName(String loginName) {
        Account account = accountMapper.findByLoginName(loginName);
        AccountDto accountDto = BeanUtil.map(account, AccountDto.class);
        cacheUtils.initCacheInput(accountDto);
        return accountDto;
    }

    public Page<AccountDto> findPage(Pageable pageable, AccountQuery accountQuery) {
        Page<AccountDto> accountDtoPage = accountMapper.findPage(pageable, accountQuery);
        cacheUtils.initCacheInput(accountDtoPage.getContent());
        return accountDtoPage;
    }

    public List<AccountDto> findByFilter(AccountQuery accountQuery) {
        List<Account> accountList = accountMapper.findByFilter(accountQuery);
        List<AccountDto> accountDtoList = BeanUtil.map(accountList, AccountDto.class);
        cacheUtils.initCacheInput(accountList);
        return accountDtoList;
    }

    public Account save(AccountForm accountForm) {
        Account account;
        if (accountForm.isCreate()) {
            accountForm.setPassword(StringUtils.getEncryptPassword(Const.DEFAULT_PASSWORD));
            account = BeanUtil.map(accountForm, Account.class);
            accountMapper.save(account);
        } else {
            if (StringUtils.isNotBlank(accountForm.getPassword())) {
                accountForm.setPassword(StringUtils.getEncryptPassword(accountForm.getPassword()));
            } else {
                accountForm.setPassword(accountMapper.findOne(accountForm.getId()).getPassword());
            }
            account = accountMapper.findOne(accountForm.getId());
            ReflectionUtil.copyProperties(accountForm,account);
            accountMapper.update(account);
        }
        if ("主账号".equals(accountForm.getType())) {
            employeeMapper.updateAccountId(accountForm.getEmployeeId(), account.getId());
        }
        return account;
    }


    public void logicDeleteOne(String id) {
        accountMapper.logicDeleteOne(id);
    }

    public List<SimpleExcelSheet> findSimpleExcelSheets(Workbook workbook, AccountQuery accountQuery) {
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "type", "类型"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "loginName", "登录名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "employee.name", "姓名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "leader.loginName", "上级"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "office.name", "部门"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "position.dataScope", "数据部门"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "position.dataScopeLabel", "数据范围"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "employee.status", "是否在职"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "employee.entryDate", "入职日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "employee.regularDate", "转正日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "employee.leaveDate", "离职日期"));

        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<Account> accountList = accountMapper.findByFilter(accountQuery);
        List<AccountDto> accountDtoList = BeanUtil.map(accountList, AccountDto.class);
        cacheUtils.initCacheInput(accountDtoList);
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("账户信息", accountList, simpleExcelColumnList);
        simpleExcelSheetList.add(simpleExcelSheet);
        return simpleExcelSheetList;
    }

    public List<AccountDto> findByLoginNameLikeAndType(Map<String, Object> map) {
        List<Account> accountList = accountMapper.findByLoginNameLikeAndType(map);
        List<AccountDto> accountDtoList = BeanUtil.map(accountList, AccountDto.class);
        return accountDtoList;
    }

    public List<String> getAuthorityList() {
        String roleId = SecurityUtils.getRoleId();
        String accountId=SecurityUtils.getAccountId();
        List<String> authorityList;
        List<Permission> permissionList;
        if(Const.HR_ACCOUNT_ADMIN_LIST.contains(SecurityUtils.getAccountId())){
            permissionList=permissionMapper.findAllEnabled();
        }else {
            List<String> accountPermissions=accountPermissionMapper.findPermissionIdByAccount(accountId);
            if(CollectionUtil.isNotEmpty(accountPermissions)){
                permissionList=permissionMapper.findByRoleAndAccount(roleId,accountId);
            }else {
                permissionList=permissionMapper.findByRoleId(roleId);
            }
        }
        authorityList= CollectionUtil.extractToList(permissionList,"permission");
        return authorityList;
    }

    public AccountDto getAccountDto(String accountId){
        AccountDto accountDto=BeanUtil.map(accountMapper.findOne(accountId),AccountDto.class);
        cacheUtils.initCacheInput(accountDto);
        return accountDto;
    }

    public void saveAccountAndPermission(AccountForm accountForm){
        List<String> permissionIdList=accountPermissionMapper.findPermissionIdByAccount(accountForm.getId());
        List<String>removeIdList=CollectionUtil.subtract(permissionIdList,accountForm.getPermissionIdList());
        List<String> addIdList=CollectionUtil.subtract(accountForm.getPermissionIdList(),permissionIdList);
        List<AccountPermission> accountPermissions=Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(removeIdList)){
            accountPermissionMapper.removeByPermissionList(removeIdList);
        }
        if(CollectionUtil.isNotEmpty(addIdList)){
            for(String permissionId:addIdList){
                accountPermissions.add(new AccountPermission(accountForm.getId(), permissionId));
            }
            accountPermissionMapper.batchSave(accountPermissions);
        }
    }

}
