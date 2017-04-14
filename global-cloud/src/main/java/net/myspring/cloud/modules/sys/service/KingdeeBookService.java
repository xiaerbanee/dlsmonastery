package net.myspring.cloud.modules.sys.service;

import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.common.utils.CacheUtils;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.dto.KingdeeBookDto;
import net.myspring.cloud.modules.sys.mapper.KingdeeBookMapper;
import net.myspring.cloud.modules.sys.web.form.KingdeeBookForm;
import net.myspring.cloud.modules.sys.web.query.KingdeeBookQuery;
import net.myspring.util.mapper.BeanUtil;
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
    private KingdeeBookMapper kingdeeBookMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<KingdeeBookDto> findPage(Pageable pageable, KingdeeBookQuery kingdeeBookQuery){
        Page<KingdeeBook> page = kingdeeBookMapper.findPage(pageable,kingdeeBookQuery);
        Page<KingdeeBookDto> accountChangeDtoPage = BeanUtil.map(page,KingdeeBookDto.class);
        cacheUtils.initCacheInput(accountChangeDtoPage.getContent());
        return accountChangeDtoPage;
    }

    public KingdeeBookDto findOne(String id){
        KingdeeBook kingdeeBook = kingdeeBookMapper.findOne(id);
        KingdeeBookDto kingdeeBookDto = BeanUtil.map(kingdeeBook,KingdeeBookDto.class);
        cacheUtils.initCacheInput(kingdeeBookDto);
        return kingdeeBookDto;
    }

    public void deleteById(String id) {
        kingdeeBookMapper.deleteById(id);
    }

    public void save(KingdeeBookForm kingdeeBookForm){
        kingdeeBookMapper.saveForm(kingdeeBookForm);
    }
}
