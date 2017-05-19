package net.myspring.basic.modules.sys.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.MenuCategory;
import net.myspring.basic.modules.sys.dto.MenuCategoryDto;
import net.myspring.basic.modules.sys.mapper.MenuCategoryMapper;
import net.myspring.basic.modules.sys.web.form.MenuCategoryForm;
import net.myspring.basic.modules.sys.web.query.MenuCategoryQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuCategoryService {

    @Autowired
    private MenuCategoryMapper menuCategoryMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public MenuCategoryDto findOne(MenuCategoryDto menuCategoryDto){
        if(!menuCategoryDto.isCreate()){
            MenuCategory menuCategory = menuCategoryMapper.findOne(menuCategoryDto.getId());
            menuCategoryDto= BeanUtil.map(menuCategory,MenuCategoryDto.class);
            cacheUtils.initCacheInput(menuCategoryDto);
        }
        return menuCategoryDto;
    }

    public Page<MenuCategoryDto> findPage(Pageable pageable,MenuCategoryQuery menuCategoryQuery) {
        Page<MenuCategoryDto> menuCategoryDtoPage= menuCategoryMapper.findPage(pageable, menuCategoryQuery);
        cacheUtils.initCacheInput(menuCategoryDtoPage.getContent());
        return menuCategoryDtoPage;
    }

    public void logicDeleteOne(String id) {
        menuCategoryMapper.logicDeleteOne(id);
    }

    public MenuCategory save(MenuCategoryForm menuCategoryForm){
        MenuCategory menuCategory;
        if(menuCategoryForm.isCreate()) {
            menuCategory=BeanUtil.map(menuCategoryForm, MenuCategory.class);
            menuCategoryMapper.save(menuCategory);
        } else {
            menuCategory = menuCategoryMapper.findOne(menuCategoryForm.getId());
            ReflectionUtil.copyProperties(menuCategoryForm,menuCategory);
            menuCategoryMapper.update(menuCategory);
        }
        return menuCategory;
    }

    public List<MenuCategoryDto> findAll(){
        List<MenuCategory> menuCategoryList = menuCategoryMapper.findAllEnabled();
        List<MenuCategoryDto> menuCategoryDtoList = BeanUtil.map(menuCategoryList,MenuCategoryDto.class);
        cacheUtils.initCacheInput(menuCategoryDtoList);
        return menuCategoryDtoList;
    }


}
