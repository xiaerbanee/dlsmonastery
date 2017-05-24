package net.myspring.cloud.modules.sys.service;

import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.common.utils.CacheUtils;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.dto.KingdeeBookDto;
import net.myspring.cloud.modules.sys.repository.KingdeeBookRepository;
import net.myspring.cloud.modules.sys.web.form.KingdeeBookForm;
import net.myspring.cloud.modules.sys.web.query.KingdeeBookQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@Service
@LocalDataSource
public class KingdeeBookService {
    @Autowired
    private KingdeeBookRepository kingdeeBookRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<KingdeeBookDto> findPage(Pageable pageable, KingdeeBookQuery kingdeeBookQuery){
        Page<KingdeeBook> page = null;
        Page<KingdeeBookDto> accountChangeDtoPage = BeanUtil.map(page,KingdeeBookDto.class);
        cacheUtils.initCacheInput(accountChangeDtoPage.getContent());
        return accountChangeDtoPage;
    }

    public KingdeeBookQuery getQuery(){
        KingdeeBookQuery kingdeeBookQuery = new KingdeeBookQuery();
        List<String> nameList = kingdeeBookRepository.findNames();
        List<String> typeList = kingdeeBookRepository.findTypes();
        kingdeeBookQuery.setNameList(nameList);
        kingdeeBookQuery.setTypeList(typeList);
        return kingdeeBookQuery;

    }

    public KingdeeBookForm getForm(KingdeeBookForm kingdeeBookForm){
        KingdeeBook kingdeeBook = kingdeeBookRepository.findOne(kingdeeBookForm.getId());
        kingdeeBookForm = BeanUtil.map(kingdeeBook,KingdeeBookForm.class);
        kingdeeBookForm.setNameList(kingdeeBookRepository.findNames());
        kingdeeBookForm.setTypeList(kingdeeBookRepository.findTypes());
        cacheUtils.initCacheInput(kingdeeBookForm);
        return kingdeeBookForm;
    }

    public void logicDeleteOne(String id) {
        kingdeeBookRepository.logicDeleteOne(id);
    }

    public void save(KingdeeBookForm kingdeeBookForm){
        int count;
        if (kingdeeBookForm.isCreate()){
            KingdeeBook kingdeeBook = BeanUtil.map(kingdeeBookForm,KingdeeBook.class);
             kingdeeBookRepository.save(kingdeeBook);
        } else {
            KingdeeBook kingdeeBook = kingdeeBookRepository.findOne(kingdeeBookForm.getId());
            ReflectionUtil.copyProperties(kingdeeBookForm,kingdeeBook);
            kingdeeBookRepository.save(kingdeeBook);
        }
    }

    public KingdeeBook findByAccountId(String accountId) {
        return kingdeeBookRepository.findByAccountId(accountId);
    }
}
