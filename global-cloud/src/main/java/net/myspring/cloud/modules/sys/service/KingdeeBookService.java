package net.myspring.cloud.modules.sys.service;

import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.common.enums.KingdeeNameEnum;
import net.myspring.cloud.common.utils.CacheUtils;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.dto.KingdeeBookDto;
import net.myspring.cloud.modules.sys.mapper.KingdeeBookMapper;
import net.myspring.cloud.modules.sys.web.form.KingdeeBookForm;
import net.myspring.cloud.modules.sys.web.query.KingdeeBookQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
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

    public KingdeeBookQuery getQuery(){
        KingdeeBookQuery kingdeeBookQuery = new KingdeeBookQuery();
        List<String> nameList = kingdeeBookMapper.findNames();
        List<String> typeList = kingdeeBookMapper.findTypes();
        kingdeeBookQuery.setNameList(nameList);
        kingdeeBookQuery.setTypeList(typeList);
        return kingdeeBookQuery;

    }

    public KingdeeBookForm findForm(KingdeeBookForm kingdeeBookForm){
        KingdeeBook kingdeeBook = kingdeeBookMapper.findOne(kingdeeBookForm.getId());
        kingdeeBookForm = BeanUtil.map(kingdeeBook,KingdeeBookForm.class);
        kingdeeBookForm.setNameList(kingdeeBookMapper.findNames());
        kingdeeBookForm.setTypeList(kingdeeBookMapper.findTypes());
        cacheUtils.initCacheInput(kingdeeBookForm);
        return kingdeeBookForm;
    }

    public void deleteById(String id) {
        kingdeeBookMapper.deleteById(id);
    }

    public int save(KingdeeBookForm kingdeeBookForm){
        int count;
        if (kingdeeBookForm.isCreate()){
            KingdeeBook kingdeeBook = BeanUtil.map(kingdeeBookForm,KingdeeBook.class);
            count = kingdeeBookMapper.save(kingdeeBook);
        } else {
            count = kingdeeBookMapper.updateForm(kingdeeBookForm);
        }
        return count;
    }

    public String getNameByCompanyId(String companyId){
        return kingdeeBookMapper.findNameByCompanyId(companyId);
    }

    public KingdeeBook getByCompanyId(String companyId){
        return kingdeeBookMapper.findByCompanyId(companyId);
    }

    public boolean isWZOPPO(String companyId){
        if (StringUtils.isNotBlank(companyId)){
            String kingdeeBookName = kingdeeBookMapper.findNameByCompanyId(companyId);
            if(KingdeeNameEnum.WZOPPO.name().equals(kingdeeBookName) || KingdeeNameEnum.WZOPPO2016.name().equals(kingdeeBookName)){
                return true;
            }
        }
        return false;
    }

    public String getTypeByCompanyId(String companyId){
        return kingdeeBookMapper.findTypeByCompanyId(companyId);
    }
}
