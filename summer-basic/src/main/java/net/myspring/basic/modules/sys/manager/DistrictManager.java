package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.DictMap;
import net.myspring.basic.modules.sys.domain.District;
import net.myspring.basic.modules.sys.mapper.DistrictMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
@Component
public class DistrictManager {

    @Autowired
    private DistrictMapper districtMapper;

    @Cacheable(value = "districts",key="#p0")
    public District findOne(String id) {
        return districtMapper.findOne(id);
    }

    @Cacheable(value = "districts",key="#p0.id")
    public District save(District district){
        districtMapper.save(district);
        return  district;
    }

    @CachePut(value = "districts",key="#p0.id")
    public District update(District district){
        districtMapper.update(district);
        return  districtMapper.findOne(district.getId());
    }

    @CacheEvict(value = "districts",key="#p0")
    public int deleteById(String id) {
        return districtMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}
