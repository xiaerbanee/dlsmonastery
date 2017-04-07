package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.BoolEnum;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.dto.MenuDto;
import net.myspring.basic.modules.sys.service.MenuCategoryService;
import net.myspring.basic.modules.sys.service.MenuService;
import net.myspring.basic.modules.sys.web.form.MenuForm;
import net.myspring.basic.modules.sys.web.query.MenuQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
    private SecurityUtils securityUtils;

    @RequestMapping(method = RequestMethod.GET)
    public Page<MenuDto> list(Pageable pageable, MenuQuery menuQuery){
        Page<MenuDto> page = menuService.findPage(pageable,menuQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(Menu menu, BindingResult bindingResult) {
        if(CollectionUtil.isNotEmpty(menu.getPermissionList())){
            return new RestResponse("菜单删除失败，请先删除下属权限",null);
        }
        menuService.delete(menu.getId());
        RestResponse restResponse=new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(MenuForm menuForm) {
        menuService.save(menuForm);
        return new RestResponse("保存成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public MenuDto findOne(String id){
        MenuDto menuDto=menuService.findDto(id);
        return menuDto;
    }

    @RequestMapping(value="getFormProperty")
    public Map<String,Object> getFormProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("menuCategory", menuCategoryService.findAll());
        map.put("category",menuService.findDistinctCategory());
        map.put("bools", BoolEnum.getMap());
        return map;
    }

    @RequestMapping(value="getListProperty")
    public Map<String,Object> getListProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("menuCategory", menuCategoryService.findAll());
        map.put("category",menuService.findDistinctCategory());
        return map;
    }

    @RequestMapping(value = "getMenus")
    public String getMenus(String requestClient){
        Boolean isMobile = "weixin".equals(requestClient);
        if(isMobile) {
            return ObjectMapperUtils.writeValueAsString(menuService.findMobileMenus(securityUtils.getAccountId()));
        } else {
            return ObjectMapperUtils.writeValueAsString(menuService.findMenus(securityUtils.getAccountId()));
        }
    }
}