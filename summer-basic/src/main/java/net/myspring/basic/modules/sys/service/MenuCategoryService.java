package net.myspring.basic.modules.sys.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.domain.MenuCategory;
import net.myspring.basic.modules.sys.dto.MenuCategoryDto;
import net.myspring.basic.modules.sys.manager.MenuCategoryManager;
import net.myspring.basic.modules.sys.mapper.MenuCategoryMapper;
import net.myspring.basic.modules.sys.web.form.MenuCategoryForm;
import net.myspring.basic.modules.sys.web.query.MenuCategoryQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
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
    @Autowired
    private MenuCategoryManager menuCategoryManager;

    public MenuCategory findOne(String id){
        MenuCategory menuCategory=menuCategoryMapper.findOne(id);
        return menuCategory;
    }

    public MenuCategoryForm findForm(MenuCategoryForm menuCategoryForm){
        if(!menuCategoryForm.isCreate()){
            MenuCategory menuCategory=menuCategoryMapper.findOne(menuCategoryForm.getId());
            menuCategoryForm= BeanUtil.map(menuCategory,MenuCategoryForm.class);
            cacheUtils.initCacheInput(menuCategoryForm);
        }
        return menuCategoryForm;
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
            menuCategory=menuCategoryManager.save(menuCategory);
        } else {
            menuCategory=menuCategoryManager.updateForm(menuCategoryForm);
        }
        return menuCategory;
    }

    public List<MenuCategoryDto> findAll(){
        List<MenuCategory> menuCategoryList =menuCategoryMapper.findAllEnabled();
        List<MenuCategoryDto> menuCategoryDtoList = BeanUtil.map(menuCategoryList,MenuCategoryDto.class);
        cacheUtils.initCacheInput(menuCategoryDtoList);
        return menuCategoryDtoList;
    }


}
