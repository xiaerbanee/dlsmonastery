package net.myspring.basic.modules.hr.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.myspring.basic.common.enums.DataScopeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.domain.Office;
import net.myspring.basic.modules.hr.dto.AccountDto;
import net.myspring.basic.modules.hr.mapper.OfficeMapper;
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

    @Cacheable(value = "accountOffices", key = "p0.id")
    public List<String> officeFilter(Account account){
        List<String> officeIdList= Lists.newArrayList();
        List<Office> officeList=Lists.newArrayList();
        AccountDto accountDto= BeanUtil.map(account,AccountDto.class);
        cacheUtils.initCacheInput(accountDto);
        List<Office> offices = officeMapper.findByAccountId(account.getId());
        officeList.addAll(offices);
        officeList.add(account.getOffice());
        String dataScope=accountDto.getDataScope();
        if(CollectionUtil.isNotEmpty(offices)){
            if((DataScopeEnum.OFFICE_AND_CHILD.getValue().equals(dataScope))) {
                for(Office office:offices) {
                    List<Office> tempOffices = officeMapper.findByParentIdsLike("%," + office.getId() + ",%");
                    if(CollectionUtil.isNotEmpty(tempOffices)) {
                        officeList.addAll(tempOffices);
                    }
                }
            }
        }else {
            if(DataScopeEnum.ALL.getValue().equals(dataScope)) {
                officeList = Lists.newArrayList();
            } else if (DataScopeEnum.OFFICE_AND_CHILD.getValue().equals(dataScope)) {
                officeList = officeMapper.findByParentIdsLike("%," + account.getOfficeId() + ",%");
            }
        }
        Set<Office> set = Sets.newHashSet(officeList);
        officeList=Lists.newArrayList(set);
        List<String> officeIds = CollectionUtil.extractToList(officeList,"id");
        if(CollectionUtil.isNotEmpty(officeIds)) {
            officeIds.add("0");
        }
        return officeIdList;
    }
}
