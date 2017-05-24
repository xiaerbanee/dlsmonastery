package net.myspring.basic.modules.sys.repository

import net.myspring.basic.modules.sys.domain.MenuCategory
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Query

/**
 * Created by haos on 2017/5/24.
 */
interface  MenuCategoryRepository{
    @CachePut(key="#id")
    fun save(menuCategory: MenuCategory): MenuCategory

    @Cacheable
    fun findOne(id: String): MenuCategory

    @Query("""
       SELECT
        t1.*
        FROM
        sys_menu_category t1
        WHERE
        t1.enabled=1
        AND t1.name =?1
     """, nativeQuery = true)
    fun findByName(name:String):MenuCategory

    @Query("""
        SELECT
        t1.*
        FROM
        sys_menu_category t1
        WHERE
        t1.enabled=1
        and t1.backend_module_id IN ?1
     """, nativeQuery = true)
    fun findByBackendModuleIds(backendModuleIds:List<String>):List<MenuCategory>

}