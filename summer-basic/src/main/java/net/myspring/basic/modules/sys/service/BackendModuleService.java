package net.myspring.basic.modules.sys.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.Backend;
import net.myspring.basic.modules.sys.domain.BackendModule;
import net.myspring.basic.modules.sys.dto.BackendDto;
import net.myspring.basic.modules.sys.dto.BackendModuleDto;
import net.myspring.basic.modules.sys.manager.BackendModuleManager;
import net.myspring.basic.modules.sys.web.form.BackendForm;
import net.myspring.basic.modules.sys.web.form.BackendModuleForm;
import net.myspring.basic.modules.sys.web.query.BackendModuleQuery;
import net.myspring.basic.modules.sys.web.query.BackendQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
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
    private BackendModuleManager backendModuleManager;

    @Autowired
    private CacheUtils cacheUtils;

    public BackendModuleForm findForm(BackendModuleForm backendModuleForm) {
        if(!backendModuleForm.isCreate()) {
            BackendModule backendModule =backendModuleManager.findOne(backendModuleForm.getId());
            backendModuleForm= BeanUtil.map(backendModule,BackendModuleForm.class);
            cacheUtils.initCacheInput(backendModuleForm);
        }
        return backendModuleForm;
    }

    public BackendModule save(BackendModuleForm backendModuleForm) {
        BackendModule backendModule;
        if(backendModuleForm.isCreate()) {
            backendModule = BeanUtil.map(backendModuleForm, BackendModule.class);
            backendModule=backendModuleManager.save(backendModule);
        } else {
            backendModule=backendModuleManager.updateForm(backendModuleForm);
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
        List<BackendModule> backendModuleList=backendModuleMapper.findAllEnabled();
        List<BackendModuleDto> backendModuleDtoList=BeanUtil.map(backendModuleList,BackendModuleDto.class);
        return backendModuleDtoList;
    }

}
