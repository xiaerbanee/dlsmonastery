package net.myspring.basic.modules.sys.repository

import com.google.common.collect.Maps
import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.common.utils.RequestUtils
import net.myspring.basic.modules.sys.domain.Menu
import net.myspring.basic.modules.sys.dto.MenuDto
import net.myspring.basic.modules.sys.web.query.MenuQuery
import net.myspring.util.repository.MySQLDialect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*

/**
 * Created by haos on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("menuRepositorys"))
interface  MenuRepository :BaseRepository<Menu,String>,MenuRepositoryCustom{
    @CachePut(key="#p0.id")
    fun save(menu: Menu): Menu

    @Cacheable
    override fun findOne(id: String): Menu

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where
        t.enabled=1 and t.id not in (select DISTINCT menuId from Permission)
     """)
    fun findByPermissionIsEmpty():MutableList<Menu>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where
        t.enabled=1 and t.id  in (select DISTINCT menuId from Permission)
     """)
    fun findByPermissionIsNotEmpty(): MutableList<Menu>

    @Query("""
            SELECT t
            FROM  #{#entityName} t
            where t.enabled = 1 and t.menuCategoryId = ?1
     """)
    fun findByMenuCategoryId(menuCategoryId:String):MutableList<Menu>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where t.enabled=1
     """)
    fun findAllEnabled():MutableList<Menu>

}


interface MenuRepositoryCustom{

    fun findPage(pageable: Pageable, menuQuery: MenuQuery): Page<MenuDto>?

    fun findByMenuIdsAndMobile(menuIds:MutableList<String>,isMobile:Boolean,roleId: String):MutableList<Menu>

}

class MenuRepositoryImpl @Autowired constructor(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): MenuRepositoryCustom{
    override fun findByMenuIdsAndMobile(menuIds: MutableList<String>, isMobile: Boolean,roleId:String): MutableList<Menu> {
            var sb = StringBuilder("""
                   SELECT DISTINCT t4.*
                   FROM
                    sys_backend t1,sys_backend_module t2,sys_menu_category t3,sys_menu t4,sys_role_module t6
                where
                t4.menu_category_id=t3.id
                and t6.backend_module_id=t2.id
                and t3.backend_module_id=t2.id
                and t2.backend_id=t1.id
                and t1.enabled=1
                and t2.enabled=1
                and t3.enabled=1
                and t4.enabled=1
                and t4.visible=1
                and t6.enabled=1
            """);
            if(isMobile) {
                sb.append("""
                    and t4.mobile=1
                    and t4.mobile_href is not null
                """);
            }
        if(!RequestUtils.getAdmin()) {
            sb.append("""
                    and t6.role_id=:roleId
                """);
        }
        sb.append("  and t4.id in (:menuIds)    ");
        var paramMap= Maps.newHashMap<String,Any>();
        paramMap.put("menuIds",menuIds);
        paramMap.put("roleId",roleId);
        return namedParameterJdbcTemplate.query(sb.toString(),paramMap, BeanPropertyRowMapper(Menu::class.java));
    }

    override fun findPage(pageable: Pageable, menuQuery: MenuQuery): Page<MenuDto>? {
        var sql = StringBuilder("""
                SELECT
                t1.*
                FROM
                sys_menu t1
                WHERE
                t1.enabled=1
            """);
        if(menuQuery.name!=null){
            sql.append("""
                    AND t1.name LIKE CONCAT('%',:name,'%')
                """);
        }
        if(menuQuery.menuCategoryId!=null){
            sql.append("""
                    AND t1.menu_category_id =:menuCategoryId
                """);
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sql.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sql.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(menuQuery), BeanPropertyRowMapper(MenuDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(menuQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }


}