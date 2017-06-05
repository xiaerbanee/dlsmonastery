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
import net.myspring.basic.modules.sys.repository.BackendRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BackendService {

    @Autowired
    private BackendRepository backendRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public BackendDto findOne(BackendDto backendDto) {
        if(!backendDto.isCreate()) {
            Backend backend =backendRepository.findOne(backendDto.getId());
            backendDto= BeanUtil.map(backend,BackendDto.class);
            cacheUtils.initCacheInput(backendDto);
        }
        return backendDto;
    }

    public List<BackendDto> findByNameLike(String name){
        List<Backend> backendList=backendRepository.findByNameLike(name);
        List<BackendDto> backendDtoList=BeanUtil.map(backendList,BackendDto.class);
        return backendDtoList;
    }

    public Backend save(BackendForm backendForm) {
        Backend backend;
        if(backendForm.isCreate()) {
            backend = BeanUtil.map(backendForm, Backend.class);
            backendRepository.save(backend);
        } else {
            backend = backendRepository.findOne(backendForm.getId());
            ReflectionUtil.copyProperties(backendForm,backend);
            backendRepository.save(backend);
        }
        return backend;
    }

    public BackendDto findOne(String id){
        Backend backend=backendRepository.findOne(id);
        BackendDto backendDto=BeanUtil.map(backend,BackendDto.class);
        return backendDto;
    }

    public List<BackendDto> findAll(){
        List<Backend> backendList = backendRepository.findAllEnabled();
        List<BackendDto> backendDtoList=BeanUtil.map(backendList,BackendDto.class);
        return backendDtoList;
    }


    public void logicDelete(String id) {
        backendRepository.logicDelete(id);
    }

    public Page<BackendDto> findPage(Pageable pageable, BackendQuery backendQuery) {
        Page<BackendDto> backendDtoPage= backendRepository.findPage(pageable, backendQuery);
        cacheUtils.initCacheInput(backendDtoPage.getContent());
        return backendDtoPage;
    }

}
