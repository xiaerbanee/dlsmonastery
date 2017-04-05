package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.modules.sys.mapper.MenuMapper;
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
public class MenuManager {

    @Autowired
    private MenuMapper menuMapper;

    @Cacheable(value = "menus",key="#p0")
    public Menu findOne(String id) {
        return menuMapper.findOne(id);
    }

    @CachePut(value = "menus",key="#p0.id")
    public Menu save(Menu menu){
        menuMapper.save(menu);
        return  menu;
    }

    @CachePut(value = "menus",key="#p0.id")
    public Menu update(Menu menu){
        menuMapper.update(menu);
        return  menuMapper.findOne(menu.getId());
    }

    @CacheEvict(value = "menus",key="#p0")
    public int deleteById(String id) {
        return menuMapper.deleteById(id);
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }
}
