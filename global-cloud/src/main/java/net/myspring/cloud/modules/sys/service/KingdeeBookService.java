package net.myspring.cloud.modules.sys.service;

import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.common.utils.CacheUtils;
import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.dto.KingdeeBookDto;
import net.myspring.cloud.modules.sys.repository.KingdeeBookRepository;
import net.myspring.cloud.modules.sys.web.form.KingdeeBookForm;
import net.myspring.cloud.modules.sys.web.query.KingdeeBookQuery;
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
 * Created by lihx on 2017/4/5.
 */
@Service
@LocalDataSource
@Transactional(readOnly = true)
public class KingdeeBookService {
    @Autowired
    private KingdeeBookRepository kingdeeBookRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<KingdeeBookDto> findPage(Pageable pageable, KingdeeBookQuery kingdeeBookQuery){
        Page<KingdeeBook> page = kingdeeBookRepository.findPage(pageable,kingdeeBookQuery);
        Page<KingdeeBookDto> accountChangeDtoPage = BeanUtil.map(page,KingdeeBookDto.class);
        cacheUtils.initCacheInput(accountChangeDtoPage.getContent());
        return accountChangeDtoPage;
    }

    public KingdeeBookQuery getQuery(){
        KingdeeBookQuery kingdeeBookQuery = new KingdeeBookQuery();
        List<String> nameList = kingdeeBookRepository.findNamesByCompanyName(RequestUtils.getCompanyName());
        List<String> typeList = kingdeeBookRepository.findTypesByCompanyName(RequestUtils.getCompanyName());
        Map<String,Object> map = Maps.newHashMap();
        map.put("nameList",nameList);
        map.put("typeList",typeList);
        kingdeeBookQuery.setExtra(map);
        return kingdeeBookQuery;

    }

    public KingdeeBookForm getForm(KingdeeBookForm kingdeeBookForm){
        if (StringUtils.isNotBlank(kingdeeBookForm.getId())){
            KingdeeBook kingdeeBook = kingdeeBookRepository.findOne(kingdeeBookForm.getId());
            kingdeeBookForm = BeanUtil.map(kingdeeBook,KingdeeBookForm.class);
        }
        List<String> typeList = kingdeeBookRepository.findTypesByCompanyName(RequestUtils.getCompanyName());
        kingdeeBookForm.setTypeList(typeList);
        return kingdeeBookForm;
    }

    @Transactional
    public void logicDelete(String id) {
        kingdeeBookRepository.logicDelete(id);
    }

    @Transactional
    public KingdeeBook save(KingdeeBookForm kingdeeBookForm){
        KingdeeBook kingdeeBook;
        if (kingdeeBookForm.isCreate()){
            kingdeeBook = BeanUtil.map(kingdeeBookForm,KingdeeBook.class);
            kingdeeBookRepository.save(kingdeeBook);
        } else {
            kingdeeBook = kingdeeBookRepository.findOne(kingdeeBookForm.getId());
            ReflectionUtil.copyProperties(kingdeeBookForm,kingdeeBook);
            kingdeeBookRepository.save(kingdeeBook);
        }
        return kingdeeBook;
    }

    public KingdeeBook findOne(String id){
        return kingdeeBookRepository.findOne(id);
    }

    public KingdeeBook findByCompanyName(String companyName) {
        return kingdeeBookRepository.findByCompanyName(companyName);
    }
}
