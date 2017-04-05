package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.domain.MenuCategory;
import net.myspring.basic.modules.sys.mapper.MenuCategoryMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/5.
 */
@Component
public class MenuCategoryManager {

    @Autowired
    private MenuCategoryMapper menuCategoryMapper;

    @Cacheable(value = "menuCategorys",key="#p0")
    public MenuCategory findOne(String id) {
        return menuCategoryMapper.findOne(id);
    }

    @CachePut(value = "menuCategorys",key="#p0.id")
    public MenuCategory save(MenuCategory menuCategory){
        menuCategoryMapper.save(menuCategory);
        return  menuCategory;
    }

    @CachePut(value = "menuCategorys",key="#p0.id")
    public MenuCategory update(MenuCategory menuCategory){
        menuCategoryMapper.update(menuCategory);
        return  menuCategoryMapper.findOne(menuCategory.getId());
    }

    @CacheEvict(value = "menuCategorys",key="#p0")
    public int deleteById(String id) {
        return menuCategoryMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}
