package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.basic.modules.hr.dto.PositionDto;
import net.myspring.basic.modules.hr.repository.PositionRepository;
import net.myspring.basic.modules.hr.web.form.PositionForm;
import net.myspring.basic.modules.hr.web.query.PositionQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PositionService {
    
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private CacheUtils cacheUtils;


    public List<PositionDto> findAll(){
        List<Position> positionList=positionRepository.findAll();
        List<PositionDto> positionDtoList= BeanUtil.map(positionList,PositionDto.class);
        cacheUtils.initCacheInput(positionDtoList);
        return positionDtoList;
    }

    public Page<PositionDto> findPage(Pageable pageable, PositionQuery positionQuery) {
        Page<PositionDto> page = positionRepository.findPage(pageable, positionQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<PositionDto> findByNameLike(String name){
        List<Position> positionList=positionRepository.findByNameLike(name);
        List<PositionDto> positionDtoList= BeanUtil.map(positionList,PositionDto.class);
        cacheUtils.initCacheInput(positionDtoList);
        return  positionDtoList;
    }

    public List<PositionDto> findByIds(List<String> ids){
        List<Position> positionList = positionRepository.findByIdIn(ids);
        List<PositionDto> positionDtoList=BeanUtil.map(positionList,PositionDto.class);
        return positionDtoList;
    }

    public PositionDto findOne(PositionDto positionDto){
        if(!positionDto.isCreate()){
            Position position = positionRepository.findOne(positionDto.getId());
            positionDto= BeanUtil.map(position,PositionDto.class);
            cacheUtils.initCacheInput(positionDto);
        }
        return positionDto;
    }

    @Transactional
    public Position save(PositionForm positionForm){
        Position position;
        if(positionForm.isCreate()){
            position=BeanUtil.map(positionForm,Position.class);
            positionRepository.save(position);
        }else{
            position = positionRepository.findOne(positionForm.getId());
            ReflectionUtil.copyProperties(positionForm,position);
            positionRepository.save(position);
        }
        return position;
    }

    @Transactional
    public void delete(String id){
        positionRepository.logicDelete(id);
    }

}
