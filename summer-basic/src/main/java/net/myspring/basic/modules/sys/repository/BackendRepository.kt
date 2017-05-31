package net.myspring.basic.modules.sys.repository

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import net.myspring.basic.common.repository.BaseRepository
import net.myspring.basic.modules.sys.domain.Backend
import net.myspring.basic.modules.sys.dto.*
import net.myspring.basic.modules.sys.web.query.BackendQuery
import net.myspring.util.repository.MySQLDialect
import net.myspring.util.text.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*

/**
 * Created by haos on 2017/5/24.
 */
@CacheConfig(cacheNames = arrayOf("backends"))
interface BackendRepository:BaseRepository<Backend,String>,BackendRepositoryCustom{
    @Cacheable
    override fun findOne(id: String): Backend

    @CachePut(key="#id")
    fun save(backend: Backend): Backend

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where t.enabled=1
        and t.name like %?1%
     """)
    fun findByNameLike(name:String):MutableList<Backend>

    @Query("""
        SELECT t
        FROM  #{#entityName} t
        where t.enabled=1
     """)
    fun findAllEnabled():MutableList<Backend>
}


interface BackendRepositoryCustom{

    fun findByMenuList(menuList:MutableList<String>):MutableList<BackendMenuDto>

    fun findPage(pageable: Pageable,backendQuery: BackendQuery): Page<BackendDto>?

    fun findByRoleId(roleId:String):MutableList<BackendMenuDto>

    fun findRolePermissionByRoleId(roleId:String):MutableList<BackendMenuDto>

}

class BackendRepositoryImpl @Autowired constructor(val jdbcTemplate:JdbcTemplate,val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): BackendRepositoryCustom{
    override fun findByRoleId(roleId: String): MutableList<BackendMenuDto> {
        return namedParameterJdbcTemplate.query("""
                    SELECT
                    t1.id,
                    t1.name,
                    t1.code,
                    t2.id as 'moduleId',
                    t2.name as 'moduleName',
                    t2.code as 'moduleCode',
                    t2.icon as 'moduleIcon',
                    t3.id as 'categoryId',
                    t3.name as 'categoryName',
                    t3.code as 'categoryCode',
                    t4.id as 'menuId',
                    t4.name as 'menuName',
                    t4.code as 'menuCode'
                    FROM
                    sys_backend t1,sys_backend_module t2,sys_menu_category t3,sys_menu t4,sys_role_module t5
                    where
                    t4.menu_category_id=t3.id
                    and t5.backend_module_id=t2.id
                    and t3.backend_module_id=t2.id
                    and t2.backend_id=t1.id
                    and t1.enabled=1
                    and t2.enabled=1
                    and t3.enabled=1
                    and t4.enabled=1
                    and t5.enabled=1
                    and t5.role_id=:roleId
                """, Collections.singletonMap("roleId",roleId), BeanPropertyRowMapper(BackendMenuDto::class.java));
    }

    override fun findRolePermissionByRoleId(roleId: String): MutableList<BackendMenuDto> {
        return namedParameterJdbcTemplate.query("""
                        SELECT
                        t1.id,
                        t1.name,
                        t1.code,
                        t2.id as 'moduleId',
                        t2.name as 'moduleName',
                        t2.code as 'moduleCode',
                        t2.icon as 'moduleIcon',
                        t3.id as 'categoryId',
                        t3.name as 'categoryName',
                        t3.code as 'categoryCode',
                        t4.id as 'menuId',
                        t4.name as 'menuName',
                        t4.code as 'menuCode'
                        FROM
                        sys_backend t1,sys_backend_module t2,sys_menu_category t3,sys_menu t4,sys_role_module t5,sys_role_permission t6,sys_permission t7
                        where
                        t4.menu_category_id=t3.id
                        and t5.backend_module_id=t2.id
                        and t3.backend_module_id=t2.id
                        and t2.backend_id=t1.id
                        and t5.role_id=t6.role_id
                        and t6.permission_id=t7.id
                        and t7.menu_id=t4.id
                        and t1.enabled=1
                        and t2.enabled=1
                        and t3.enabled=1
                        and t4.enabled=1
                        and t5.enabled=1
                        and t6.enabled=1
                        and t6.role_id=:roleId
                """, Collections.singletonMap("roleId",roleId), BeanPropertyRowMapper(BackendMenuDto::class.java));
    }

    override fun findByMenuList(menuList: MutableList<String>): MutableList<BackendMenuDto> {
        var list =  namedParameterJdbcTemplate.queryForList("""
                SELECT
                    t1.id,
                    t1.name,
                    t1.code,
                    t2.id as 'moduleId',
                    t2.name as 'moduleName',
                    t2.code as 'moduleCode',
                    t2.icon as 'moduleIcon',
                    t3.id as 'categoryId',
                    t3.name as 'categoryName',
                    t3.code as 'categoryCode',
                    t4.id as 'menuId',
                    t4.name as 'menuName',
                    t4.code as 'menuCode'
                FROM
                    sys_backend t1,sys_backend_module t2,sys_menu_category t3,sys_menu t4
                where
                t4.menu_category_id=t3.id
                and t3.backend_module_id=t2.id
                and t2.backend_id=t1.id
                and t1.enabled=1
                and t2.enabled=1
                and t3.enabled=1
                and t4.enabled=1
                and t4.id IN (:menuList)
            """, Collections.singletonMap("menuList",menuList));
        var backendMenuDtoMap = Maps.newLinkedHashMap<String,BackendMenuDto>();
        for(item in list) {
            var id = StringUtils.toString(item["id"]);
            var name = item["name"] as String;
            var code = item["code"] as String;
            var moduleId = StringUtils.toString(item["moduleId"]);
            var moduleName = item["moduleName"] as String;
            var moduleCode = item["moduleCode"] as String;
            var moduleIcon = item["moduleIcon"] as String;
            var categoryId = StringUtils.toString(item["categoryId"]);
            var categoryName = item["categoryName"] as String;
            var categoryCode = item["categoryCode"] as String;
            var menuId = StringUtils.toString(item["menuId"]);
            var menuName = item["menuName"] as String;
            var menuCode = item["menuCode"] as String;
            if(!backendMenuDtoMap.containsKey(id)) {
                var backendMenuDto = BackendMenuDto();
                backendMenuDto.id = id;
                backendMenuDto.name=name;
                backendMenuDto.code = code;
                backendMenuDtoMap.put(id,backendMenuDto);
            }
            if(!backendMenuDtoMap[id]!!.backendModuleMenuDtoMap!!.containsKey(moduleId)) {
                var backendModuleMenuDto = BackendModuleMenuDto();
                backendModuleMenuDto.id = moduleId;
                backendModuleMenuDto.name = moduleName;
                backendModuleMenuDto.code = moduleCode;
                backendModuleMenuDto.icon = moduleIcon;
                backendMenuDtoMap[id]!!.backendModuleMenuDtoMap!!.put(moduleId,backendModuleMenuDto);
            }
            if(!backendMenuDtoMap[id]!!.backendModuleMenuDtoMap[moduleId]!!.menuCategoryMenuDtoMap.containsKey(categoryId)) {
                var menuCategoryMenuDto = MenuCategoryMenuDto();
                menuCategoryMenuDto.id = categoryId;
                menuCategoryMenuDto.name = categoryName;
                menuCategoryMenuDto.code = categoryCode;
                backendMenuDtoMap[id]!!.backendModuleMenuDtoMap[moduleId]!!.menuCategoryMenuDtoMap.put(categoryId,menuCategoryMenuDto);
            }
            var frontendMenuDto = FrontendMenuDto();
            frontendMenuDto.id = menuId;
            frontendMenuDto.name = menuName;
            frontendMenuDto.code = menuCode;
            backendMenuDtoMap[id]!!.backendModuleMenuDtoMap[moduleId]!!.menuCategoryMenuDtoMap[categoryId]!!.frontendMenuDtoMap.put(menuId,frontendMenuDto);
        }
        return Lists.newArrayList(backendMenuDtoMap.values);
    }

    override fun findPage(pageable: Pageable, backendQuery: BackendQuery): Page<BackendDto>? {
        var sql = StringBuilder("""
                SELECT  t1.*
                FROM sys_backend t1
                where t1.enabled=1
            """);
        if(backendQuery.name!=null){
            sql.append("""
                    AND t1.name LIKE CONCAT('%',:name,'%')
                """);
        }
        val pageableSql = MySQLDialect.getInstance().getPageableSql(sql.toString(),pageable)
        val countSql = MySQLDialect.getInstance().getCountSql(sql.toString())
        val list = namedParameterJdbcTemplate.query(pageableSql, BeanPropertySqlParameterSource(backendQuery), BeanPropertyRowMapper(BackendDto::class.java))
        val count = namedParameterJdbcTemplate.queryForObject(countSql, BeanPropertySqlParameterSource(backendQuery),Long::class.java)
        return PageImpl(list,pageable,count)
    }
}