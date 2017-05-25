package net.myspring.basic.modules.sys.repository

import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.Menu
import net.myspring.basic.modules.sys.dto.MenuDto
import net.myspring.basic.modules.sys.web.query.MenuQuery
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query

/**
 * Created by haos on 2017/5/24.
 */
interface  MenuRepository :BaseRepository<Menu,String>{
    @CachePut(key="#id")
    fun save(menu: Menu): Menu

    @Cacheable
    override fun findOne(id: String): Menu

    @Query("""
         SELECT
        t1.*
        FROM
        sys_menu t1
        where
        t1.enabled=1 and t1.id not in (select DISTINCT menu_id from sys_permission)
     """, nativeQuery = true)
    fun findByPermissionIsEmpty():List<Menu>

    @Query("""
        SELECT
        t1.*
        FROM
        sys_menu t1
        where
        t1.enabled=1 and t1.id  in (select DISTINCT menu_id from sys_permission)
     """, nativeQuery = true)
    fun findByPermissionIsNotEmpty(): List<Menu>

    @Query("""
            select t1.*
            from sys_menu t1
            where t1.enabled = 1 and t1.menu_category_id = ?1
     """, nativeQuery = true)
    fun findByMenuCategoryId(menuCategoryId:String):List<Menu>

    @Query("""
       SELECT t1.*
        FROM sys_menu t1
        where t1.enabled=1
        and t1.visible=1
        <if test="isMobile">
            and t1.mobile=?2
            and t1.mobile_href is not null
        </if>
        and t1.id in ?1
     """, nativeQuery = true)
    fun findByMenuIdsAndMobile(menuIds:List<String>,isMobile:Boolean):List<Menu>

    fun findPage(pageable: Pageable, menuQuery: MenuQuery): Page<MenuDto>

    fun findAllEnabled():List<Menu>

}