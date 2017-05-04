package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.basic.modules.hr.dto.PositionDto;
import net.myspring.basic.modules.hr.mapper.RoleModuleMapper;
import net.myspring.basic.modules.hr.web.form.PositionForm;
import net.myspring.basic.modules.sys.domain.Role;
import net.myspring.basic.modules.sys.domain.RoleModule;
import net.myspring.basic.modules.sys.domain.RolePermission;
import net.myspring.basic.modules.sys.dto.RoleDto;
import net.myspring.basic.modules.sys.manager.RoleManager;
import net.myspring.basic.modules.sys.mapper.RoleMapper;
import net.myspring.basic.modules.sys.mapper.RolePermissionMapper;
import net.myspring.basic.modules.sys.web.form.RoleForm;
import net.myspring.basic.modules.sys.web.query.RoleQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangzm on 2017/5/2.
 */
@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private RoleModuleMapper roleModuleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private CacheUtils cacheUtils;


    public RoleForm findForm(RoleForm roleForm) {
        if(!roleForm.isCreate()) {
            Role Role =roleManager.findOne(roleForm.getId());
            roleForm= BeanUtil.map(Role,RoleForm.class);
            cacheUtils.initCacheInput(roleForm);
        }
        return roleForm;
    }

    public Role save(RoleForm roleForm){
        Role role;
        if(roleForm.isCreate()){
            role=BeanUtil.map(roleForm,Role.class);
            role=roleManager.save(role);
        }else{
            role=roleManager.updateForm(roleForm);
        }
        roleModuleMapper.deleteByRoleId(roleForm.getId());
        if(CollectionUtil.isNotEmpty(roleForm.getPermissionIdList())){
            List<RoleModule> positionModuleList= Lists.newArrayList();
            for(String moduleId:roleForm.getPermissionIdList()){
                positionModuleList.add(new RoleModule(role.getId(),moduleId));
            }
            roleModuleMapper.batchSave(positionModuleList);
        }
        return role;
    }

    public void saveRoleAndPermission(RoleForm roleForm){
        rolePermissionMapper.deleteByRoleId(roleForm.getId());
        if(CollectionUtil.isNotEmpty(roleForm.getPermissionIdList())){
            List<RolePermission> rolePermissions= Lists.newArrayList();
            for(String permissionId:roleForm.getPermissionIdList()){
                rolePermissions.add(new RolePermission(roleForm.getId(),permissionId));
            }
            rolePermissionMapper.batchSave(rolePermissions);
        }
    }

    public void logicDeleteOne(String id) {
        roleMapper.logicDeleteOne(id);
    }

    public Page<RoleDto> findPage(Pageable pageable, RoleQuery roleQuery) {
        Page<RoleDto> roleDtoPage= roleMapper.findPage(pageable, roleQuery);
        cacheUtils.initCacheInput(roleDtoPage.getContent());
        return roleDtoPage;
    }

    public List<RoleDto> findByNameLike(String name){
        List<Role> roleList=roleMapper.findByNameLike(name);
        List<RoleDto> roleDtoList= BeanUtil.map(roleList,RoleDto.class);
        cacheUtils.initCacheInput(roleDtoList);
        return  roleDtoList;
    }
}