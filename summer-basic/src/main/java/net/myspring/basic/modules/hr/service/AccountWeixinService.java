package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.domain.AccountWeixin;
import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.basic.modules.hr.dto.AccountWeixinDto;
import net.myspring.basic.modules.hr.dto.PositionDto;
import net.myspring.basic.modules.hr.repository.AccountWeixinRepository;
import net.myspring.basic.modules.hr.web.form.AccountWeixinForm;
import net.myspring.basic.modules.hr.web.form.PositionForm;
import net.myspring.basic.modules.hr.web.query.AccountQuery;
import net.myspring.basic.modules.hr.web.query.AccountWeixinQuery;
import net.myspring.basic.modules.hr.web.query.PositionQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AccountWeixinService {

    @Autowired
    private AccountWeixinRepository accountWeixinRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<AccountWeixinDto> findPage(Pageable pageable, AccountWeixinQuery accountWeixinQuery) {
        Page<AccountWeixinDto> page = accountWeixinRepository.findPage(pageable, accountWeixinQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public AccountWeixinDto findOne(String id){
        return BeanUtil.map(accountWeixinRepository.findOne(id),AccountWeixinDto.class);
    }

    public AccountWeixinDto findByAccountId(String accountId) {
        AccountWeixin accountWeixinList =  accountWeixinRepository.findByAccountId(accountId);
        return BeanUtil.map(accountWeixinList,AccountWeixinDto.class);
    }

    @Transactional
    public AccountWeixin save(AccountWeixinForm accountWeixinForm){
        AccountWeixin accountWeixin;
        if(accountWeixinForm.isCreate()){
            accountWeixin=BeanUtil.map(accountWeixinForm,AccountWeixin.class);
            accountWeixinRepository.save(accountWeixin);
        }else{
            accountWeixin = accountWeixinRepository.findOne(accountWeixinForm.getId());
            ReflectionUtil.copyProperties(accountWeixinForm,accountWeixin);
            accountWeixinRepository.save(accountWeixin);
        }
        return accountWeixin;
    }

    @Transactional
    public void logicDelete(String id){
        accountWeixinRepository.delete(id);
    }
}
