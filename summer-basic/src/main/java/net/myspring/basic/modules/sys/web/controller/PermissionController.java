package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.modules.sys.dto.PermissionDto;
import net.myspring.basic.modules.sys.service.MenuService;
import net.myspring.basic.modules.sys.service.PermissionService;
import net.myspring.basic.modules.sys.web.form.PermissionForm;
import net.myspring.basic.modules.sys.web.query.PermissionQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "sys/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private MenuService menuService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<PermissionDto> list(Pageable pageable, PermissionQuery permissionQuery){
        Page<PermissionDto> page = permissionService.findPage(pageable,permissionQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        permissionService.logicDeleteOne(id);
        RestResponse restResponse =new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(PermissionForm permissionForm) {
        permissionService.save(permissionForm);
        return new RestResponse("保存成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public PermissionDto findOne(String id) {
        PermissionDto permissionDto=permissionService.findDto(id);
        return permissionDto;
    }

    @RequestMapping(value="getFormProperty")
    public Map<String,Object> getFormProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("menu",menuService.findAll());
        return map;
    }

    @RequestMapping(value = "search")
    public List<Map<String, String>> search(String query){
        List<Permission> permissions=permissionService.findByPermissionLike(query);
        List<Map<String, String>> list = Lists.newArrayList();
        for (Permission permission : permissions) {
            Map<String, String> map = Maps.newHashMap();
            map.put("id", permission.getId());
            map.put("fullName",permission.getName()+" "+permission.getPermission());
            list.add(map);
        }
        return list;
    }
}
