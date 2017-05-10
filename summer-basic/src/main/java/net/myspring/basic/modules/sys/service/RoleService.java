package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.enums.OfficeTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.mapper.RoleModuleMapper;
import net.myspring.basic.modules.sys.domain.OfficeBusiness;
import net.myspring.basic.modules.sys.domain.Role;
import net.myspring.basic.modules.sys.domain.RoleModule;
import net.myspring.basic.modules.sys.domain.RolePermission;
import net.myspring.basic.modules.sys.dto.RoleDto;
import net.myspring.basic.modules.sys.mapper.RoleMapper;
import net.myspring.basic.modules.sys.mapper.RolePermissionMapper;
import net.myspring.basic.modules.sys.web.form.RoleForm;
import net.myspring.basic.modules.sys.web.query.RoleQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
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
    private RoleModuleMapper roleModuleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private CacheUtils cacheUtils;


    public RoleForm findForm(RoleForm roleForm) {
        if (!roleForm.isCreate()) {
            Role Role = roleMapper.findOne(roleForm.getId());
            roleForm = BeanUtil.map(Role, RoleForm.class);
            cacheUtils.initCacheInput(roleForm);
        }
        return roleForm;
    }

    public Role save(RoleForm roleForm) {
        Role role;
        if (roleForm.isCreate()) {
            role = BeanUtil.map(roleForm, Role.class);
            roleMapper.save(role);
        } else {
            role = roleMapper.findOne(roleForm.getId());
            ReflectionUtil.copyProperties(roleForm, role);
            roleMapper.update(role);
        }
        List<RoleModule> roleModuleList = roleModuleMapper.findAllByRoleId(role.getId());
        if (CollectionUtil.isNotEmpty(roleForm.getModuleIdList())) {
            List<String> roleModuleIdList = CollectionUtil.extractToList(roleModuleList, "backendModuleId");
            List<String> removeIdList = CollectionUtil.subtract(roleModuleIdList, roleForm.getModuleIdList());
            List<String> addIdList = CollectionUtil.subtract(roleForm.getModuleIdList(), roleModuleIdList);
            List<RoleModule> addRoleModules = Lists.newArrayList();
            for (String moduleId : addIdList) {
                addRoleModules.add(new RoleModule(role.getId(), moduleId));
            }
            roleModuleMapper.setEnabledByRoleId(true, role.getId());
            if (CollectionUtil.isNotEmpty(removeIdList)) {
                roleModuleMapper.setEnabledByModuleIdList(false,removeIdList);
            }
            if (CollectionUtil.isNotEmpty(addIdList)) {
                roleModuleMapper.batchSave(addRoleModules);
            }
        } else if (CollectionUtil.isNotEmpty(roleModuleList)) {
            roleModuleMapper.setEnabledByRoleId(false, role.getId());
        }
        return role;
    }

    public void saveRoleAndPermission(RoleForm roleForm) {
        List<RolePermission> rolePermissionList = rolePermissionMapper.findAllByRoleId(roleForm.getId());
        if (CollectionUtil.isNotEmpty(roleForm.getPermissionIdList())) {
            List<String> rolePermissionIdList = CollectionUtil.extractToList(rolePermissionList, "permissionId");
            List<String> removeIdList = CollectionUtil.subtract(rolePermissionIdList, roleForm.getPermissionIdList());
            List<String> addIdList = CollectionUtil.subtract(roleForm.getPermissionIdList(), rolePermissionIdList);
            List<RolePermission> rolePermissions = Lists.newArrayList();
            for (String permissionId : addIdList) {
                rolePermissions.add(new RolePermission(roleForm.getId(), permissionId));
            }
            rolePermissionMapper.setEnabledByRoleId(true, roleForm.getId());
            if (CollectionUtil.isNotEmpty(removeIdList)) {
                rolePermissionMapper.setEnabledByPermissionIdList(false,removeIdList);
            }
            if (CollectionUtil.isNotEmpty(addIdList)) {
                rolePermissionMapper.batchSave(rolePermissions);
            }
        } else if (CollectionUtil.isNotEmpty(rolePermissionList)) {
            rolePermissionMapper.setEnabledByRoleId(false, roleForm.getId());
        }
    }

    public void logicDeleteOne(String id) {
        roleMapper.logicDeleteOne(id);
    }

    public Page<RoleDto> findPage(Pageable pageable, RoleQuery roleQuery) {
        Page<RoleDto> roleDtoPage = roleMapper.findPage(pageable, roleQuery);
        cacheUtils.initCacheInput(roleDtoPage.getContent());
        return roleDtoPage;
    }

    public List<RoleDto> findByNameLike(String name) {
        List<Role> roleList = roleMapper.findByNameLike(name);
        List<RoleDto> roleDtoList = BeanUtil.map(roleList, RoleDto.class);
        cacheUtils.initCacheInput(roleDtoList);
        return roleDtoList;
    }

    public List<RoleDto> findAll(){
        List<Role> roleList=roleMapper.findAll();
        List<RoleDto> roleDtoList = BeanUtil.map(roleList, RoleDto.class);
        cacheUtils.initCacheInput(roleDtoList);
        return  roleDtoList;
    }
}
