package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.MenuCategory
import net.myspring.basic.modules.sys.dto.MenuCategoryDto
import net.myspring.basic.modules.sys.dto.MenuDto
import net.myspring.basic.modules.sys.web.query.MenuCategoryQuery
import net.myspring.basic.modules.sys.web.query.MenuQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import javax.persistence.EntityManager

/**
 * Created by haos on 2017/5/24.
 */
interface  MenuCategoryRepository :BaseRepository<MenuCategory,String>,MenuCategoryRepositoryCustom{
    @CachePut(key="#id")
    fun save(menuCategory: MenuCategory): MenuCategory

    @Cacheable
    override fun findOne(id: String): MenuCategory

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

    @Query("""
        SELECT
        t1.*
        FROM
        sys_menu_category t1
        WHERE
        t1.enabled=1
     """, nativeQuery = true)
    fun findAllEnabled():List<MenuCategory>
}


interface MenuCategoryRepositoryCustom{

    fun findPage(pageable: Pageable, menuCategoryQuery: MenuCategoryQuery): Page<MenuCategoryDto>?


}

class MenuCategoryRepositoryImpl @Autowired constructor(val entityManager: EntityManager): MenuCategoryRepositoryCustom{
    override fun findPage(pageable: Pageable, menuCategoryQuery: MenuCategoryQuery): Page<MenuCategoryDto>? {
        return null
    }


}