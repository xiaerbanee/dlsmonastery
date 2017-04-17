package net.myspring.basic.modules.sys.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.domain.MenuCategory;
import net.myspring.basic.modules.sys.dto.MenuCategoryDto;
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

    public MenuCategory findOne(String id){
        MenuCategory menuCategory=menuCategoryMapper.findOne(id);
        return menuCategory;
    }

    public MenuCategoryForm findForm(String id){
        MenuCategory menuCategory=menuCategoryMapper.findOne(id);
        MenuCategoryForm menuCategoryForm= BeanUtil.map(menuCategory,MenuCategoryForm.class);
        cacheUtils.initCacheInput(menuCategoryForm);
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

    public void save(MenuCategoryForm menuCategoryForm){
        boolean isCreated= StringUtils.isBlank(menuCategoryForm.getId());
        if(isCreated) {
            menuCategoryMapper.save(BeanUtil.map(menuCategoryForm, MenuCategory.class));
        } else {
            menuCategoryMapper.updateForm(menuCategoryForm);
        }
    }

    public List<MenuCategoryDto> findAll(){
        List<MenuCategory> menuCategoryList =menuCategoryMapper.findAll();
        List<MenuCategoryDto> menuCategoryDtoList = BeanUtil.map(menuCategoryList,MenuCategoryDto.class);
        cacheUtils.initCacheInput(menuCategoryDtoList);
        return menuCategoryDtoList;
    }


}
