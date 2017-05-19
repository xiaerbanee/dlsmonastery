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
import net.myspring.basic.modules.sys.mapper.BackendModuleMapper;

import java.util.List;

@Service
public class BackendModuleService {

    @Autowired
    private BackendModuleMapper backendModuleMapper;

    @Autowired
    private CacheUtils cacheUtils;

    public BackendModuleForm getFormProperty(BackendModuleForm backendModuleForm) {
        if(!backendModuleForm.isCreate()) {
            BackendModule backendModule =backendModuleMapper.findOne(backendModuleForm.getId());
            backendModuleForm= BeanUtil.map(backendModule,BackendModuleForm.class);
            cacheUtils.initCacheInput(backendModuleForm);
        }
        return backendModuleForm;
    }

    public BackendModule save(BackendModuleForm backendModuleForm) {
        BackendModule backendModule;
        if(backendModuleForm.isCreate()) {
            backendModule = BeanUtil.map(backendModuleForm, BackendModule.class);
            backendModuleMapper.save(backendModule);
        } else {
            backendModule = backendModuleMapper.findOne(backendModuleForm.getId());
            ReflectionUtil.copyProperties(backendModuleForm,backendModule);
            backendModuleMapper.update(backendModule);
        }
        return backendModule;
    }

    public List<String> findBackendModuleIdByRoleId(String roleId){
        List<BackendModule> backendModuleList=backendModuleMapper.findByRoleId(roleId);
        List<String> backendModuleIds= CollectionUtil.extractToList(backendModuleList,"id");
        return backendModuleIds;
    }

    public void logicDeleteOne(String id) {
        backendModuleMapper.logicDeleteOne(id);
    }

    public Page<BackendModuleDto> findPage(Pageable pageable, BackendModuleQuery backendModuleQuery) {
        Page<BackendModuleDto> backendModuleDtoPage= backendModuleMapper.findPage(pageable, backendModuleQuery);
        cacheUtils.initCacheInput(backendModuleDtoPage.getContent());
        return backendModuleDtoPage;
    }

    public List<BackendModuleDto> findAll(){
        List<BackendModule> backendModuleList = backendModuleMapper.findAllEnabled();
        List<BackendModuleDto> backendModuleDtoList=BeanUtil.map(backendModuleList,BackendModuleDto.class);
        return backendModuleDtoList;
    }

}
