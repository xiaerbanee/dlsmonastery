package net.myspring.basic.modules.sys.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.BackendModule;
import net.myspring.basic.modules.sys.dto.BackendModuleDto;
import net.myspring.basic.modules.sys.web.form.BackendModuleForm;
import net.myspring.basic.modules.sys.web.query.BackendModuleQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.basic.modules.sys.repository.BackendModuleRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly =true)
public class BackendModuleService {

    @Autowired
    private BackendModuleRepository backendModuleRepository;

    @Autowired
    private CacheUtils cacheUtils;

    public BackendModuleDto findOne(BackendModuleDto backendModuleDto) {
        if(!backendModuleDto.isCreate()) {
            BackendModule backendModule =backendModuleRepository.findOne(backendModuleDto.getId());
            backendModuleDto= BeanUtil.map(backendModule,BackendModuleDto.class);
            cacheUtils.initCacheInput(backendModuleDto);
        }
        return backendModuleDto;
    }

    @Transactional
    public BackendModule save(BackendModuleForm backendModuleForm) {
        BackendModule backendModule;
        if(backendModuleForm.isCreate()) {
            backendModule = BeanUtil.map(backendModuleForm, BackendModule.class);
            backendModuleRepository.save(backendModule);
        } else {
            backendModule = backendModuleRepository.findOne(backendModuleForm.getId());
            ReflectionUtil.copyProperties(backendModuleForm,backendModule);
            backendModuleRepository.save(backendModule);
        }
        return backendModule;
    }

    public List<String> findBackendModuleIdByRoleId(String roleId){
        List<BackendModule> backendModuleList=backendModuleRepository.findByRoleId(roleId);
        List<String> backendModuleIds= CollectionUtil.extractToList(backendModuleList,"id");
        return backendModuleIds;
    }

    @Transactional
    public void logicDelete(String id) {
        backendModuleRepository.logicDelete(id);
    }

    public Page<BackendModuleDto> findPage(Pageable pageable, BackendModuleQuery backendModuleQuery) {
        Page<BackendModuleDto> backendModuleDtoPage= backendModuleRepository.findPage(pageable, backendModuleQuery);
        cacheUtils.initCacheInput(backendModuleDtoPage.getContent());
        return backendModuleDtoPage;
    }

    public List<BackendModuleDto> findAll(){
        List<BackendModule> backendModuleList = backendModuleRepository.findAllEnabled();
        List<BackendModuleDto> backendModuleDtoList=BeanUtil.map(backendModuleList,BackendModuleDto.class);
        return backendModuleDtoList;
    }

}
