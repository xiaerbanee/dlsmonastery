package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.MenuCategory;
import net.myspring.basic.modules.sys.mapper.MenuCategoryMapper;
import net.myspring.basic.modules.sys.web.form.MenuCategoryForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
@Component
@CacheConfig(cacheNames = "menuCategorys")
public class MenuCategoryManager {

    @Autowired
    private MenuCategoryMapper menuCategoryMapper;

    @Cacheable(key="#p0")
    public MenuCategory findOne(String id) {
        return menuCategoryMapper.findOne(id);
    }

    @CachePut(key="#p0.id")
    public MenuCategory save(MenuCategory menuCategory){
        menuCategoryMapper.save(menuCategory);
        return  menuCategory;
    }

    @CachePut(key="#p0.id")
    public MenuCategory update(MenuCategory menuCategory){
        menuCategoryMapper.update(menuCategory);
        return  menuCategoryMapper.findOne(menuCategory.getId());
    }

    @CachePut(key="#p0.id")
    public MenuCategory updateForm(MenuCategoryForm menuCategoryForm){
        menuCategoryMapper.updateForm(menuCategoryForm);
        return  menuCategoryMapper.findOne(menuCategoryForm.getId());
    }

    @CacheEvict(key="#p0")
    public int deleteById(String id) {
        return menuCategoryMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}
