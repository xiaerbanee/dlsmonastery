package net.myspring.basic.modules.hr.manager;

import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.basic.modules.hr.mapper.PositionMapper;
import net.myspring.basic.modules.hr.web.form.PositionForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Created by admin on 2017/4/5.
 */
@Component
@CacheConfig(cacheNames = "positions")
public class PositionManager {

    @Autowired
    private PositionMapper positionMapper;

    @Cacheable(key="#p0")
    public Position findOne(String id) {
        return positionMapper.findOne(id);
    }

    @CachePut(key="#p0.id")
    public Position save(Position position){
        positionMapper.save(position);
        return  position;
    }

    @CachePut(key="#p0.id")
    public Position update(Position position){
        positionMapper.update(position);
        return  positionMapper.findOne(position.getId());
    }

    @CachePut(key="#p0.id")
    public Position updateForm(PositionForm positionForm){
        positionMapper.updateForm(positionForm);
        return  positionMapper.findOne(positionForm.getId());
    }
}
