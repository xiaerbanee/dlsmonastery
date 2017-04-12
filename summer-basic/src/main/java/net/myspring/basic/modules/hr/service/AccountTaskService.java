package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.domain.AccountTask;
import net.myspring.basic.modules.hr.dto.AccountTaskDto;
import net.myspring.basic.modules.hr.manager.OfficeManager;
import net.myspring.basic.modules.hr.mapper.AccountTaskMapper;
import net.myspring.basic.modules.hr.web.query.AccountTaskQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountTaskService {

    @Autowired
    private AccountTaskMapper accountTaskMapper;
    @Autowired
    private OfficeManager officeManager;
    @Autowired
    private CacheUtils cacheUtils;

    public AccountTask findByNameAndExtendId(String name, String extendId){
        return accountTaskMapper.findByNameAndExtendId(name,extendId);
    }

    public Page<AccountTaskDto> findPage(Pageable pageable, AccountTaskQuery accountTaskQuery){
        Page<AccountTask> page=accountTaskMapper.findPage(pageable,accountTaskQuery);
        Page<AccountTaskDto> accountTaskDtoPage= BeanUtil.map(page,AccountTaskDto.class);
        cacheUtils.initCacheInput(accountTaskDtoPage.getContent());
        return accountTaskDtoPage;
    }

    public List<AccountTaskDto> findByPositionId(String positionId, Account account){
        List<AccountTaskDto> accountTaskDtoList= Lists.newArrayList();
        List<String> officeIds=officeManager.officeFilter(account);
        officeIds.add(account.getOfficeId());
        if(CollectionUtil.isNotEmpty(officeIds)&& StringUtils.isNotBlank(positionId)){
            List<AccountTask> accountTasks=accountTaskMapper.findByPositionAndOfficeIds(positionId,officeIds);
            accountTaskDtoList= BeanUtil.map(accountTasks,AccountTaskDto.class);
        }
        return accountTaskDtoList;
    }

    public void logicDeleteOne(String id) {
        accountTaskMapper.logicDeleteOne(id);
    }
}
