package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.basic.modules.hr.dto.PositionDto;
import net.myspring.basic.modules.hr.manager.PositionManager;
import net.myspring.basic.modules.hr.mapper.PositionMapper;
import net.myspring.basic.modules.hr.web.form.PositionForm;
import net.myspring.basic.modules.hr.web.query.PositionQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class PositionService {

    @Autowired
    private PositionManager positionManager;
    @Autowired
    private PositionMapper positionMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private SecurityUtils securityUtils;

    public List<PositionDto> findAll(){
        List<Position> positionList=positionMapper.findAll();
        List<PositionDto> positionDtoList= BeanUtil.map(positionList,PositionDto.class);
        cacheUtils.initCacheInput(positionDtoList);
        return positionDtoList;
    }

    public Page<PositionDto> findPage(Pageable pageable, PositionQuery positionQuery) {
        Page<Position> page = positionMapper.findPage(pageable, positionQuery);
        Page<PositionDto> positionDtoPage= BeanUtil.map(page,PositionDto.class);
        cacheUtils.initCacheInput(positionDtoPage.getContent());
        return positionDtoPage;
    }

    public List<PositionDto> findByNameLike(String name){
        List<Position> positionList=positionMapper.findByNameLike(name);
        List<PositionDto> positionDtoList= BeanUtil.map(positionList,PositionDto.class);
        cacheUtils.initCacheInput(positionDtoList);
        return  positionDtoList;
    }

    public List<String> findPermissionByPosition(String positionId){
        return positionMapper.findPermissionByPosition(positionId);
    }

    public Position findOne(String id){
        Position position = positionManager.findOne(id);
        return position;
    }

    public PositionDto findDto(String id){
        Position position = findOne(id);
        PositionDto positionDto= BeanUtil.map(position,PositionDto.class);
        return positionDto;
    }

    public void save(PositionForm positionForm){
        boolean isCreate= StringUtils.isBlank(positionForm.getId());
        if(isCreate){
            positionManager.saveForm(positionForm);
        }else{
            positionManager.updateForm(positionForm);
        }
        positionMapper.deleteByPosition(positionForm.getId());
        if(CollectionUtil.isNotEmpty(positionForm.getPermissionIdList())){
            positionMapper.savePositionAndPermission(positionForm.getId(),positionForm.getPermissionIdList());
        }

    }

    public void delete(String id){
        positionMapper.logicDeleteOne(id);
    }

}
