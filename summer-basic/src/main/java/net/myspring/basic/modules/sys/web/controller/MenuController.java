package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.BoolEnum;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.dto.MenuDto;
import net.myspring.basic.modules.sys.service.MenuCategoryService;
import net.myspring.basic.modules.sys.service.MenuService;
import net.myspring.basic.modules.sys.web.form.MenuForm;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Page<MenuDto> page = menuService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "delete")
    public String delete(Menu menu, BindingResult bindingResult) {
        if(CollectionUtil.isNotEmpty(menu.getPermissionList())){
            return ObjectMapperUtils.writeValueAsString(new RestResponse("菜单删除失败，请先删除下属权限"));
        }
        menuService.delete(menu.getId());
        RestResponse restResponse=new RestResponse("删除成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "save")
    public String save(MenuForm menuForm) {
        menuService.save(menuForm);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }

    @RequestMapping(value = "findOne")
    public String findOne(String id){
        MenuDto menuDto=menuService.findDto(id);
        return ObjectMapperUtils.writeValueAsString(menuDto);
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("menuCategory", menuCategoryService.findAll());
        map.put("category",menuService.findDistinctCategory());
        map.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("menuCategory", menuCategoryService.findAll());
        map.put("category",menuService.findDistinctCategory());
        return ObjectMapperUtils.writeValueAsString(map);
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