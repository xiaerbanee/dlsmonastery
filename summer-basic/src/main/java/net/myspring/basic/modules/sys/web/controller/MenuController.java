package net.myspring.basic.modules.sys.web.controller;

import net.myspring.common.enums.BoolEnum;
import net.myspring.basic.modules.sys.dto.MenuDto;
import net.myspring.basic.modules.sys.dto.PermissionDto;
import net.myspring.basic.modules.sys.service.MenuCategoryService;
import net.myspring.basic.modules.sys.service.MenuService;
import net.myspring.basic.modules.sys.service.PermissionService;
import net.myspring.basic.modules.sys.web.form.MenuForm;
import net.myspring.basic.modules.sys.web.query.MenuQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by liuj on 2016/11/23.
 */
@RestController
@RequestMapping(value = "sys/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuCategoryService menuCategoryService;
    @Autowired
    private PermissionService permissionService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<MenuDto> list(Pageable pageable, MenuQuery menuQuery){
        Page<MenuDto> page = menuService.findPage(pageable,menuQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(MenuForm menuForm, BindingResult bindingResult) {
        List<PermissionDto> permissionDtoList=permissionService.findByMenuId(menuForm.getId());
        if(CollectionUtil.isNotEmpty(permissionDtoList)){
            return new RestResponse("菜单删除失败，请先删除下属权限",null);
        }
        menuService.delete(menuForm.getId());
        RestResponse restResponse=new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(MenuForm menuForm) {
        menuService.save(menuForm);
        return new RestResponse("保存成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public MenuDto findOne(MenuDto menuDto){
        menuDto = menuService.findOne(menuDto);
        return menuDto;
    }

    @RequestMapping(value = "getForm")
    public MenuForm getForm(MenuForm menuForm){
        menuForm.getExtra().put("menuCategoryList",menuCategoryService.findAll());
        return menuForm;
    }

    @RequestMapping(value="getQuery")
    public MenuQuery getQuery(MenuQuery menuQuery){
        menuQuery.getExtra().put("menuCategoryList", menuCategoryService.findAll());
        menuQuery.getExtra().put("categoryList",CollectionUtil.extractToList(menuCategoryService.findAll(),"name"));
        return menuQuery;
    }
}