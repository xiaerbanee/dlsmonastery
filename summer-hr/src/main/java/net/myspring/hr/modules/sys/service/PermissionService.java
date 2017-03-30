package net.myspring.hr.modules.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import net.myspring.hr.modules.sys.mapper.PermissionMapper;

@Service
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

}
