package net.myspring.basic.modules.sys.manager;

import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.mapper.MenuMapper;
import net.myspring.basic.modules.sys.web.form.MenuForm;
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
@CacheConfig(cacheNames = "menus")
public class MenuManager {

    @Autowired
    private MenuMapper menuMapper;

    @Cacheable(key="#p0")
    public Menu findOne(String id) {
        return menuMapper.findOne(id);
    }

    @CachePut(key="#p0.id")
    public Menu save(Menu menu){
        menuMapper.save(menu);
        return  menu;
    }

    @CachePut(key="#p0.id")
    public Menu update(Menu menu){
        menuMapper.update(menu);
        return  menuMapper.findOne(menu.getId());
    }

    @CachePut(key="#p0.id")
    public Menu updateForm(MenuForm menuForm){
        menuMapper.updateForm(menuForm);
        return  menuMapper.findOne(menuForm.getId());
    }
}
