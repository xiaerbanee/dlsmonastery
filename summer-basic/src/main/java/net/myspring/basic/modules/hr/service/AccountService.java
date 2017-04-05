package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.dto.AccountDto;
import net.myspring.basic.modules.hr.manager.AccountManager;
import net.myspring.basic.modules.hr.manager.EmployeeManager;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanMapper;
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
    private StringUtils stringUtils;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private EmployeeManager employeeManager;

    public Account findOne(String id) {
        Account account = accountManager.findOne(id);
        return account;
    }

    public Account findByLoginName(String loginName) {
        Account account = accountManager.findByLoginName(loginName);
        AccountDto accountDto= BeanMapper.convertDto(account,AccountDto.class);
        cacheUtils.initCacheInput(accountDto);
        return accountDto;
    }

    public Page<AccountDto> findPage(Pageable pageable, Map<String, Object> map) {
        Page<Account> page = accountManager.findPage(pageable, map);
        Page<AccountDto> accountDtoPage=BeanMapper.convertPage(page,AccountDto.class);
        cacheUtils.initCacheInput(accountDtoPage);
        return accountDtoPage;
    }

    public List<AccountDto> findByOffice(String officeId){
        List<AccountDto> accountDtoList=Lists.newArrayList();
        if(StringUtils.isNotBlank(officeId)){
            List<Account> accountList=accountManager.findByOfficeId(officeId);
            accountDtoList=BeanMapper.convertDtoList(accountList,AccountDto.class);
            cacheUtils.initCacheInput(accountList);
        }
        return accountDtoList;
    }

    public Account save(Account account) {
        boolean isCreate = StringUtils.isBlank(account.getId());
        if (isCreate) {
            account.setPassword(StringUtils.getEncryptPassword(Const.DEFAULT_PASSWORD));
            accountManager.save(account);
        } else {
            if(StringUtils.isNotBlank(account.getPassword())){
                account.setPassword(StringUtils.getEncryptPassword(account.getPassword()));
            }else {
                account.setPassword(accountManager.findOne(account.getId()).getPassword());
            }
            accountManager.update(account);
        }
        accountManager.deleteAccountOffice(account.getId());
        if (CollectionUtil.isNotEmpty(account.getOfficeIdList())) {
            accountManager.saveAccountOffice(account.getId(), account.getOfficeIdList());
        }
        if("主账号".equals(account.getType())){
            employeeManager.updateAccountId(account.getEmployeeId(),account.getId());
        }
        return account;
    }


    public void logicDeleteOne(String id) {
        accountManager.logicDeleteOne(id);
    }

    public List<SimpleExcelSheet> findSimpleExcelSheets(Workbook workbook, Map<String, Object> map) {
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
        List<Account> accountList=accountManager.findByFilter(map);
        List<AccountDto> accountDtoList=BeanMapper.convertDtoList(accountList,AccountDto.class);
        cacheUtils.initCacheInput(accountDtoList);
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("账户信息", accountList, simpleExcelColumnList);
        simpleExcelSheetList.add(simpleExcelSheet);
        return simpleExcelSheetList;
    }

    public List<AccountDto> findByLoginNameLikeAndType(Map<String,Object> map) {
        List<Account> accountList = accountManager.findByLoginNameLikeAndType(map);
        List<AccountDto> accountDtoList=BeanMapper.convertDtoList(accountList,AccountDto.class);
        return accountDtoList;
    }

}
