package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.repository.RoleModuleRepository;
import net.myspring.basic.modules.sys.domain.Role;
import net.myspring.basic.modules.sys.domain.RoleModule;
import net.myspring.basic.modules.sys.domain.RolePermission;
import net.myspring.basic.modules.sys.dto.RoleDto;
import net.myspring.basic.modules.sys.repository.RoleRepository;
import net.myspring.basic.modules.sys.repository.RolePermissionRepository;
import net.myspring.basic.modules.sys.web.form.RoleForm;
import net.myspring.basic.modules.sys.web.query.RoleQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wangzm on 2017/5/2.
 */
@Service
@Transactional(readOnly = true)
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleModuleRepository roleModuleRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    @Autowired
    private CacheUtils cacheUtils;


    public RoleDto findOne(RoleDto roleDto) {
        if (!roleDto.isCreate()) {
            Role Role = roleRepository.findOne(roleDto.getId());
            roleDto = BeanUtil.map(Role, RoleDto.class);
            cacheUtils.initCacheInput(roleDto);
        }
        return roleDto;
    }

    @Transactional
    public Role save(RoleForm roleForm) {
        Role role;
        if (roleForm.isCreate()) {
            role = BeanUtil.map(roleForm, Role.class);
            roleRepository.save(role);
        } else {
            role = roleRepository.findOne(roleForm.getId());
            ReflectionUtil.copyProperties(roleForm, role);
            roleRepository.save(role);
        }
        List<RoleModule> roleModuleList = roleModuleRepository.findAllByRoleId(role.getId());
        if (CollectionUtil.isNotEmpty(roleForm.getModuleIdList())) {
            List<String> roleModuleIdList = CollectionUtil.extractToList(roleModuleList, "backendModuleId");
            List<String> removeIdList = CollectionUtil.subtract(roleModuleIdList, roleForm.getModuleIdList());
            List<String> addIdList = CollectionUtil.subtract(roleForm.getModuleIdList(), roleModuleIdList);
            List<RoleModule> addRoleModules = Lists.newArrayList();
            for (String moduleId : addIdList) {
                addRoleModules.add(new RoleModule(role.getId(), moduleId));
            }
            roleModuleRepository.setEnabledByRoleId(true, role.getId());
            if (CollectionUtil.isNotEmpty(removeIdList)) {
                roleModuleRepository.setEnabledByModuleIdList(false,removeIdList);
            }
            if (CollectionUtil.isNotEmpty(addIdList)) {
                roleModuleRepository.save(addRoleModules);
            }
        } else if (CollectionUtil.isNotEmpty(roleModuleList)) {
            roleModuleRepository.setEnabledByRoleId(false, role.getId());
        }
        return role;
    }

    @Transactional
    public void saveRoleAndPermission(RoleForm roleForm) {
        List<RolePermission> rolePermissionList = rolePermissionRepository.findAllByRoleId(roleForm.getId());
        if (CollectionUtil.isNotEmpty(roleForm.getPermissionIdList())) {
            List<String> rolePermissionIdList = CollectionUtil.extractToList(rolePermissionList, "permissionId");
            List<String> removeIdList = CollectionUtil.subtract(rolePermissionIdList, roleForm.getPermissionIdList());
            List<String> addIdList = CollectionUtil.subtract(roleForm.getPermissionIdList(), rolePermissionIdList);
            List<RolePermission> rolePermissions = Lists.newArrayList();
            for (String permissionId : addIdList) {
                rolePermissions.add(new RolePermission(roleForm.getId(), permissionId));
            }
            rolePermissionRepository.setEnabledByRoleId(true, roleForm.getId());
            if (CollectionUtil.isNotEmpty(removeIdList)) {
                rolePermissionRepository.setEnabledByPermissionIdList(false,removeIdList);
            }
            if (CollectionUtil.isNotEmpty(addIdList)) {
                rolePermissionRepository.save(rolePermissions);
            }
        } else if (CollectionUtil.isNotEmpty(rolePermissionList)) {
            rolePermissionRepository.setEnabledByRoleId(false, roleForm.getId());
        }
    }

    @Transactional
    public void logicDelete(String id) {
        roleRepository.logicDelete(id);
    }

    public Page<RoleDto> findPage(Pageable pageable, RoleQuery roleQuery) {
        Page<RoleDto> roleDtoPage = roleRepository.findPage(pageable, roleQuery);
        cacheUtils.initCacheInput(roleDtoPage.getContent());
        return roleDtoPage;
    }

    public List<RoleDto> findByNameLike(String name) {
        List<Role> roleList = roleRepository.findByNameLike(name);
        List<RoleDto> roleDtoList = BeanUtil.map(roleList, RoleDto.class);
        cacheUtils.initCacheInput(roleDtoList);
        return roleDtoList;
    }

    public List<RoleDto> findAll(){
        List<Role> roleList=roleRepository.findAll();
        List<RoleDto> roleDtoList = BeanUtil.map(roleList, RoleDto.class);
        cacheUtils.initCacheInput(roleDtoList);
        return  roleDtoList;
    }
}
