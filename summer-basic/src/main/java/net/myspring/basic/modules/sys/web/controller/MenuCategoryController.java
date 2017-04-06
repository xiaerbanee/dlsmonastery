package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.sys.domain.MenuCategory;
import net.myspring.basic.modules.sys.dto.MenuCategoryDto;
import net.myspring.basic.modules.sys.service.MenuCategoryService;
import net.myspring.basic.modules.sys.web.form.MenuCategoryForm;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "sys/menuCategory")
public class MenuCategoryController {

    @Autowired
    private MenuCategoryService menuCategoryService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Page<MenuCategoryDto> page = menuCategoryService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "delete")
    public String delete(MenuCategory menuCategory) {
        if(CollectionUtil.isNotEmpty(menuCategory.getMenuList())){
            return ObjectMapperUtils.writeValueAsString(new RestResponse("菜单分类删除失败，请先删除下属菜单"));
        }
        menuCategoryService.logicDeleteOne(menuCategory.getId());
        RestResponse restResponse =new RestResponse("删除成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "save")
    public String save(MenuCategoryForm menuCategoryForm) {
        menuCategoryService.save(menuCategoryForm);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }

    @RequestMapping(value = "findOne")
    public String findOne(String id){
        MenuCategoryDto menuCategoryDto=menuCategoryService.findDto(id);
        return ObjectMapperUtils.writeValueAsString(menuCategoryDto);
    }

}
