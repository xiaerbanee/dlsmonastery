package net.myspring.basic.modules.sys.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.Backend;
import net.myspring.basic.modules.sys.dto.BackendDto;
import net.myspring.basic.modules.sys.web.form.BackendForm;
import net.myspring.basic.modules.sys.web.query.BackendQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.basic.modules.sys.mapper.BackendMapper;

import java.util.List;

@Service
public class BackendService {

    @Autowired
    private BackendMapper backendMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public BackendForm getFormProperty(BackendForm backendForm) {
        if(!backendForm.isCreate()) {
            Backend backend =backendMapper.findOne(backendForm.getId());
            backendForm= BeanUtil.map(backend,BackendForm.class);
            cacheUtils.initCacheInput(backendForm);
        }
        return backendForm;
    }

    public List<BackendDto> findByNameLike(String name){
        List<Backend> backendList=backendMapper.findByNameLike(name);
        List<BackendDto> backendDtoList=BeanUtil.map(backendList,BackendDto.class);
        return backendDtoList;
    }

    public Backend save(BackendForm backendForm) {
        Backend backend;
        if(backendForm.isCreate()) {
            backend = BeanUtil.map(backendForm, Backend.class);
            backendMapper.save(backend);
        } else {
            backend = backendMapper.findOne(backendForm.getId());
            ReflectionUtil.copyProperties(backendForm,backend);
            backendMapper.update(backend);
        }
        return backend;
    }

    public List<BackendDto> findAll(){
        List<Backend> backendList = backendMapper.findAllEnabled();
        List<BackendDto> backendDtoList=BeanUtil.map(backendList,BackendDto.class);
        return backendDtoList;
    }


    public void logicDeleteOne(String id) {
        backendMapper.logicDeleteOne(id);
    }

    public Page<BackendDto> findPage(Pageable pageable, BackendQuery backendQuery) {
        Page<BackendDto> backendDtoPage= backendMapper.findPage(pageable, backendQuery);
        cacheUtils.initCacheInput(backendDtoPage.getContent());
        return backendDtoPage;
    }

}
