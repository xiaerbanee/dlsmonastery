package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.dto.NameValueDto;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.common.utils.InitDomainUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.domain.Office;
import net.myspring.basic.modules.hr.dto.AccountDto;
import net.myspring.basic.modules.hr.manager.AccountManager;
import net.myspring.basic.modules.hr.manager.EmployeeManager;
import net.myspring.basic.modules.hr.manager.OfficeManager;
import net.myspring.basic.modules.hr.mapper.AccountMapper;
import net.myspring.basic.modules.hr.mapper.EmployeeMapper;
import net.myspring.basic.modules.hr.mapper.OfficeMapper;
import net.myspring.basic.modules.hr.web.form.AccountForm;
import net.myspring.basic.modules.hr.web.query.AccountQuery;
import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.modules.sys.manager.PermissionManager;
import net.myspring.basic.modules.sys.mapper.PermissionMapper;
import net.myspring.basic.modules.sys.service.PermissionService;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.apache.commons.collections.CollectionUtils;
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
    private AccountManager accountManager;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private OfficeMapper officeMapper;

    @Autowired
    private InitDomainUtils initDomainUtils;

    public Account findOne(String id) {
        Account account = accountManager.findOne(id);
        return account;
    }

    public AccountDto findDto(String id) {
        Account account = accountMapper.findOne(id);
        List<NameValueDto> nameValueDtoList = accountMapper.findAccountOfficeByIds(Lists.newArrayList(account.getId()));
        initDomainUtils.initChildIdList(account,Office.class,nameValueDtoList);
        AccountDto accountDto = BeanUtil.map(account, AccountDto.class);
        cacheUtils.initCacheInput(accountDto);
        return accountDto;
    }

    public AccountDto findByLoginName(String loginName) {
        Account account = accountMapper.findByLoginName(loginName);
        AccountDto accountDto = BeanUtil.map(account, AccountDto.class);
        cacheUtils.initCacheInput(accountDto);
        return accountDto;
    }

    public Page<AccountDto> findPage(Pageable pageable, AccountQuery accountQuery) {
        Page<Account> page = accountMapper.findPage(pageable, accountQuery);
        List<NameValueDto> nameValueDtoList = accountMapper.findAccountOfficeByIds(CollectionUtil.extractToList(page.getContent(), "id"));
        initDomainUtils.initChildIdList(page.getContent(),Office.class,nameValueDtoList);
        Page<AccountDto> accountDtoPage = BeanUtil.map(page, AccountDto.class);
        cacheUtils.initCacheInput(accountDtoPage.getContent());
        return accountDtoPage;
    }

    public List<AccountDto> findByOffice(String officeId) {
        List<AccountDto> accountDtoList = Lists.newArrayList();
        if (StringUtils.isNotBlank(officeId)) {
            List<Account> accountList = accountMapper.findByOfficeIds(Lists.newArrayList(officeId));
            accountDtoList = BeanUtil.map(accountList, AccountDto.class);
            cacheUtils.initCacheInput(accountList);
        }
        return accountDtoList;
    }

    public AccountForm save(AccountForm accountForm) {
        boolean isCreate = StringUtils.isBlank(accountForm.getId());
        if (isCreate) {
            accountForm.setPassword(StringUtils.getEncryptPassword(Const.DEFAULT_PASSWORD));
            Account account = BeanUtil.map(accountForm, Account.class);
            account.setCompanyId(SecurityUtils.getCompanyId());
            accountManager.save(account);
        } else {
            if (StringUtils.isNotBlank(accountForm.getPassword())) {
                accountForm.setPassword(StringUtils.getEncryptPassword(accountForm.getPassword()));
            } else {
                accountForm.setPassword(accountManager.findOne(accountForm.getId()).getPassword());
            }
            accountManager.updateForm(accountForm);
        }
        accountMapper.deleteAccountOffice(accountForm.getId());
        if (CollectionUtil.isNotEmpty(accountForm.getOfficeIdList())) {
            accountMapper.saveAccountOffice(accountForm.getId(), accountForm.getOfficeIdList());
        }
        if ("主账号".equals(accountForm.getType())) {
            employeeMapper.updateAccountId(accountForm.getEmployeeId(), accountForm.getId());
        }
        return accountForm;
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
        List<String> authorityList = Lists.newArrayList();
        String positionId=SecurityUtils.getPositionId();
        if (StringUtils.isNotBlank(positionId)) {
            List<Permission> permissionList=permissionMapper.findByPositionId(positionId);
            authorityList= CollectionUtil.extractToList(permissionList,"permission");
        }
        return authorityList;
    }

    public AccountDto getAccount(){
        String accountId=SecurityUtils.getAccountId();
        if(StringUtils.isNotBlank(accountId)){
            AccountDto accountDto=findDto(accountId);
            return accountDto;
        }else {
            return null;
        }
    }

}
