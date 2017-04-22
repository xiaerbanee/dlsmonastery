package net.myspring.basic.modules.sys.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.myspring.basic.common.enums.DataScopeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.hr.dto.AccountDto;
import net.myspring.basic.modules.sys.mapper.OfficeMapper;
import net.myspring.basic.modules.sys.web.form.OfficeForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Created by admin on 2017/4/6.
 */
@Component
public class OfficeManager {

    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private CacheUtils cacheUtils;

    @Cacheable(value = "offices",key="#p0")
    public Office findOne(String id) {
        return officeMapper.findOne(id);
    }

    @CachePut(value = "offices",key="#p0.id")
    public Office save(Office office){
        officeMapper.save(office);
        return  office;
    }

    @CachePut(value = "offices",key="#p0.id")
    public Office update(Office office){
        officeMapper.update(office);
        return  officeMapper.findOne(office.getId());
    }

    @CachePut(value = "offices",key="#p0.id")
    public Office updateForm(OfficeForm officeForm){
        officeMapper.updateForm(officeForm);
        return  officeMapper.findOne(officeForm.getId());
    }

    public List<String> officeFilter(Account account){
        List<String> officeIdList= Lists.newArrayList();
        AccountDto accountDto= BeanUtil.map(account,AccountDto.class);
        cacheUtils.initCacheInput(accountDto);

        Set<String> set = Sets.newHashSet();
        set.add(accountDto.getOfficeId());
        List<Office> offices = officeMapper.findByAccountId(account.getId());
        Integer dataScope=accountDto.getDataScope();
        if(CollectionUtil.isNotEmpty(offices)){
            set.addAll(CollectionUtil.extractToList(offices,"id"));
            if((DataScopeEnum.OFFICE_AND_CHILD.getValue().equals(dataScope))) {
                for(Office office:offices) {
                    List<Office> tempOffices = officeMapper.findByParentIdsLike("%," + office.getId() + ",%");
                    if(CollectionUtil.isNotEmpty(tempOffices)) {
                        set.addAll(CollectionUtil.extractToList(tempOffices,"id"));
                    }
                }
            }
        }else {
            if(DataScopeEnum.ALL.getValue().equals(dataScope)) {
                set = Sets.newHashSet();
            } else if (DataScopeEnum.OFFICE_AND_CHILD.getValue().equals(dataScope)) {
                List<Office> officeList = officeMapper.findByParentIdsLike("%," + account.getOfficeId() + ",%");
                if(CollectionUtil.isNotEmpty(officeList)) {
                    set.addAll(CollectionUtil.extractToList(officeList,"id"));
                }
            }
        }
        officeIdList=Lists.newArrayList(set);
        if(CollectionUtil.isNotEmpty(officeIdList)) {
            officeIdList.add("0");
        }
        return officeIdList;
    }
}
