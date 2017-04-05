package net.myspring.basic.modules.hr.manager;

import net.myspring.basic.modules.hr.domain.Employee;
import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.basic.modules.hr.mapper.PositionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
public class PositionManager {

    @Autowired
    private PositionMapper positionMapper;

    @Cacheable(value = "positions",key="#p0")
    public Position findOne(String id) {
        return positionMapper.findOne(id);
    }

    @CachePut(value = "positions",key="#p0.id")
    public Position save(Position position){
        positionMapper.save(position);
        return  position;
    }

    @CachePut(value = "positions",key="#p0.id")
    public Position update(Position position){
        positionMapper.update(position);
        return  positionMapper.findOne(position.getId());
    }

    public List<Position> findAll(){
        return positionMapper.findAll();
    }
}
