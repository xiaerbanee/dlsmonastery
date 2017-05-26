package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.MenuCategory
import net.myspring.basic.modules.sys.dto.MenuCategoryDto
import net.myspring.basic.modules.sys.web.query.MenuCategoryQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import javax.persistence.EntityManager

/**
 * Created by haos on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("menuCategorys"))
interface  MenuCategoryRepository :BaseRepository<MenuCategory,String>,MenuCategoryRepositoryCustom{
    @CachePut(key="#id")
    fun save(menuCategory: MenuCategory): MenuCategory

    @Cacheable
    override fun findOne(id: String): MenuCategory

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        WHERE
        t.enabled=1
        AND t.name =?1
     """)
    fun findByName(name:String):MenuCategory

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        WHERE
        t.enabled=1
        and t.backendModuleId IN ?1
     """)
    fun findByBackendModuleIds(backendModuleIds:MutableList<String>):MutableList<MenuCategory>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        WHERE
        t.enabled=1
     """)
    fun findAllEnabled():MutableList<MenuCategory>
}


interface MenuCategoryRepositoryCustom{
    fun findPage(pageable: Pageable, menuCategoryQuery: MenuCategoryQuery): Page<MenuCategoryDto>?
}

class MenuCategoryRepositoryImpl @Autowired constructor(val entityManager: EntityManager): MenuCategoryRepositoryCustom{
    override fun findPage(pageable: Pageable, menuCategoryQuery: MenuCategoryQuery): Page<MenuCategoryDto>? {
        return null
    }


}