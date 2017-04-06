package net.myspring.basic.modules.hr.manager;

import net.myspring.basic.modules.hr.domain.Office;
import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.basic.modules.hr.mapper.OfficeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Created by admin on 2017/4/6.
 */
@Component
public class OfficeManager {

    @Autowired
    private OfficeMapper officeMapper;

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
}
