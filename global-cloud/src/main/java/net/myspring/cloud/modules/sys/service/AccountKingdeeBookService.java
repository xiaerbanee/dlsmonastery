package net.myspring.cloud.modules.sys.service;

import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.common.utils.Base64Utils;
import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.dto.AccountKingdeeBookDto;
import net.myspring.cloud.modules.sys.repository.AccountKingdeeBookRepository;
import net.myspring.cloud.modules.sys.repository.KingdeeBookRepository;
import net.myspring.cloud.modules.sys.web.form.AccountKingdeeBookForm;
import net.myspring.cloud.modules.sys.web.query.AccountKingdeeBookQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by lihx on 2017/5/9.
 */
@Service
@LocalDataSource
@Transactional(readOnly = true)
public class AccountKingdeeBookService {
    @Autowired
    private AccountKingdeeBookRepository accountKingdeeBookRepository;
    @Autowired
    private KingdeeBookRepository kingdeeBookRepository;

    public Page<AccountKingdeeBookDto> findPage(Pageable pageable, AccountKingdeeBookQuery accountKingdeeBookQuery){
        Page<AccountKingdeeBookDto> page = accountKingdeeBookRepository.findPage(pageable,accountKingdeeBookQuery);
        return page;
    }

    public AccountKingdeeBook findByAccountIdAndCompanyName(String accountId,String companyName){
        return accountKingdeeBookRepository.findByAccountIdAndCompanyName(accountId,companyName);
    }

    public AccountKingdeeBookQuery getQuery(){
        AccountKingdeeBookQuery accountKingdeeBookQuery = new AccountKingdeeBookQuery();
        List<String> nameList = kingdeeBookRepository.findNamesByCompanyName(RequestUtils.getCompanyName());
        List<String> typeList = kingdeeBookRepository.findTypesByCompanyName(RequestUtils.getCompanyName());
        Map<String,Object> map = Maps.newHashMap();
        map.put("kingdeeBookNameList",nameList);
        map.put("kingdeeBookTypeList",typeList);
        accountKingdeeBookQuery.setExtra(map);
        return accountKingdeeBookQuery;
    }

    public AccountKingdeeBookForm getForm(AccountKingdeeBookForm accountKingdeeBookForm){
        if (StringUtils.isNotBlank(accountKingdeeBookForm.getId())){
            AccountKingdeeBook accountKingdeeBook =  accountKingdeeBookRepository.findOne(accountKingdeeBookForm.getId());
            accountKingdeeBookForm = BeanUtil.map(accountKingdeeBook,AccountKingdeeBookForm.class);
            accountKingdeeBookForm.setPassword(null);
        }
        return accountKingdeeBookForm;
    }

    public AccountKingdeeBook getCurrentOne(){
        String accountId = RequestUtils.getAccountId();
        if (StringUtils.isNotBlank(accountId)){
            AccountKingdeeBookQuery accountKingdeeBookQuery = new AccountKingdeeBookQuery();
            accountKingdeeBookQuery.setAccountId(accountId);
            accountKingdeeBookQuery.setCompanyName(RequestUtils.getCompanyName());
            List<AccountKingdeeBook> accountKingdeeBookList =  accountKingdeeBookRepository.findFilter(accountKingdeeBookQuery);
            if (CollectionUtil.isNotEmpty(accountKingdeeBookList)) {
                accountKingdeeBookList.get(0).setPassword(null);
                return accountKingdeeBookList.get(0);
            }
        }
        return null;
    }

    @Transactional
    public AccountKingdeeBook save(AccountKingdeeBookForm accountKingdeeBookForm){
        AccountKingdeeBook accountKingdeeBook;
        if (accountKingdeeBookForm.isCreate()){//创建用户
            accountKingdeeBook = BeanUtil.map(accountKingdeeBookForm,AccountKingdeeBook.class);
            KingdeeBook kingdeeBook = kingdeeBookRepository.findByCompanyName(RequestUtils.getCompanyName());
            accountKingdeeBook.setKingdeeBookId(kingdeeBook.getId());
            accountKingdeeBook.setPassword(Base64Utils.encode(accountKingdeeBook.getPassword().getBytes()));
            accountKingdeeBookRepository.save(accountKingdeeBook);
        }else if(accountKingdeeBookForm.getPassword() != null){//修改密码
            accountKingdeeBook = accountKingdeeBookRepository.findOne(accountKingdeeBookForm.getId());
            ReflectionUtil.copyProperties(accountKingdeeBookForm,accountKingdeeBook);
            accountKingdeeBook.setPassword(Base64Utils.encode(accountKingdeeBook.getPassword().getBytes()));
            accountKingdeeBookRepository.save(accountKingdeeBook);
        }else {//修改其他（除密码之外）
            accountKingdeeBook = accountKingdeeBookRepository.findOne(accountKingdeeBookForm.getId());
            String password = accountKingdeeBook.getPassword();
            ReflectionUtil.copyProperties(accountKingdeeBookForm,accountKingdeeBook);
            accountKingdeeBook.setPassword(password);
            accountKingdeeBookRepository.save(accountKingdeeBook);
        }
        return accountKingdeeBook;
    }

    @Transactional
    public void logicDelete(String id) {
        accountKingdeeBookRepository.logicDelete(id);
    }
}
