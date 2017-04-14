package net.myspring.basic.modules.hr.manager;

import net.myspring.basic.modules.hr.domain.Employee;
import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.basic.modules.hr.mapper.PositionMapper;
import net.myspring.basic.modules.hr.web.form.PositionForm;
import net.myspring.basic.modules.hr.web.query.PositionQuery;
import net.myspring.util.mapper.BeanUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/5.
 */
@Component
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

    @CachePut(value = "positions",key="#p0.id")
    public Position updateForm(PositionForm positionForm){
        positionMapper.updateForm(positionForm);
        return  positionMapper.findOne(positionForm.getId());
    }
}
